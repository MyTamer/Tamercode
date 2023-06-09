package net.seagis.observation;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.lang.reflect.UndeclaredThrowableException;
import java.awt.Component;
import java.sql.SQLException;
import net.seagis.catalog.CatalogException;
import net.seagis.catalog.ServerException;
import net.seagis.coverage.catalog.*;
import net.seagis.coverage.model.Descriptor;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.observation.sampling.SamplingFeature;
import org.geotools.resources.Utilities;
import org.geotools.gui.swing.ExceptionMonitor;
import org.geotools.coverage.SpatioTemporalCoverage3D;
import net.seagis.coverage.model.DescriptorTable;
import net.seagis.resources.i18n.Resources;

/**
 * Remplit une table des {@linkplain Observation observations} à partir des données satellitaires.
 * La table {@code "Measurements"} contient les valeurs de descripteurs du paysage océanique tels
 * que la température, chlorophylle-<var>a</var>, hauteur de l'eau, <cite>etc.</cite> aux positions
 * des observations. Lorsque {@code MeasurementTableFiller} trouve une données environnementale à une
 * position d'une observation, il ajoute un enregistrement à la table {@code "Measurements"}.
 *
 * @version $Id: MeasurementTableFiller.java 432 2008-02-22 17:08:33Z glegal $
 * @author Martin Desruisseaux
 * @author Antoine Hnawia
 */
public class MeasurementTableFiller implements Runnable {

    /**
     * Mis à {@code true} quand l'utilisateur a demandé à annuler l'exécution de {@link #execute}.
     */
    private volatile boolean cancel;

    /**
     * Ensemble des stations concernées.
     */
    private final Set<SamplingFeature> samplingFeatures = new LinkedHashSet<SamplingFeature>();

    /**
     * Ensemble des descripteurs à utiliser pour remplir la table des mesures.
     */
    private final Set<Descriptor> descriptors = new LinkedHashSet<Descriptor>();

    /**
     * La table des mesures à modifier.
     */
    private final MeasurementTable measures;

    /**
     * Composante graphique qui a lancé ce calcul, ou {@code null} si aucun.
     * Utilisé uniquement pour l'affichage éventuel d'un message d'erreur.
     */
    private transient Component owner;

    /**
     * Construit un objet qui procèdera au remplissage de la table des mesures spécifiée.
     */
    public MeasurementTableFiller(final MeasurementTable measures) {
        this.measures = measures;
    }

    /**
     * Retourne l'ensemble des stations pour lesquelles on voudra calculer les descripteurs du
     * paysage océanique. L'ensemble retourné est modifiable; il est possible d'ajouter ou de
     * retirer des stations à prendre en compte en appelant {@link Set#add} ou {@link Set#remove}.
     */
    public Set<SamplingFeature> samplingFeatures() {
        return samplingFeatures;
    }

    /**
     * Retourne l'ensemble des descripteurs à évaluer pour chaque station. L'ensemble retourné est
     * modifiable; il est possible d'ajouter ou de retirer des descripteurs à prendre en compte en
     * appelant {@link Set#add} ou {@link Set#remove}.
     */
    public Set<Descriptor> descriptors() {
        return descriptors;
    }

    /**
     * Utilise un ensemble de stations par défaut pour lesquelles on voudra calculer les
     * descripteurs du paysage océanique. Cet ensemble est constitué de stations pour lesquelles
     * {@link MeasurementTable} pourrait avoir des données.
     *
     * @throws CatalogException si l'interrogation de la base de données a échouée.
     */
    public synchronized void addDefaultStations() throws CatalogException {
        Descriptor.LOGGER.info("Obtient l'ensemble des stations.");
    }

    /**
     * Utilise un ensemble de descripteurs par défaut à évaluer pour chaque station.
     *
     * @throws CatalogException si l'interrogation de la base de données a échouée.
     */
    public synchronized void addDefaultDescriptors() throws CatalogException {
        Descriptor.LOGGER.info("Obtient l'ensemble des descripteurs.");
        try {
            descriptors.addAll(measures.getDatabase().getTable(DescriptorTable.class).getEntries());
        } catch (SQLException exception) {
            throw new ServerException(exception);
        }
    }

    /**
     * Classe les éléments du tableau spécifié en ordre croissant.
     */
    private static void sort(final Object[] array) throws CatalogException {
        try {
            Arrays.sort(array);
        } catch (UndeclaredThrowableException exception) {
            final Throwable cause = exception.getUndeclaredThrowable();
            if (cause instanceof CatalogException) {
                throw (CatalogException) cause;
            }
            throw exception;
        }
    }

    /**
     * Lance le remplissage de la table {@code "Measurements"}.
     *
     * @throws CatalogException si un problème est survenu lors des accès au catalogue.
     */
    public synchronized void execute() throws CatalogException {
        final Set<SamplingFeature> samplingFeatures = samplingFeatures();
        final Set<Descriptor> descriptors = descriptors();
        if (descriptors.isEmpty()) {
            Descriptor.LOGGER.warning("L'ensemble des descripteurs est vide.");
            return;
        }
        if (samplingFeatures.isEmpty()) {
            Descriptor.LOGGER.warning("L'ensemble des stations est vide.");
            return;
        }
        int withoutPosition = 0;
        final LinkedList<Descriptor> remaining = new LinkedList<Descriptor>(descriptors);
        final MeasurementInserts updater = new MeasurementInserts(measures);
        updater.start();
        while (!remaining.isEmpty()) {
            final Layer layer = remaining.getFirst().getLayer();
            final List<Descriptor> descriptorList = new ArrayList<Descriptor>(remaining.size());
            for (final Iterator<Descriptor> it = remaining.iterator(); it.hasNext(); ) {
                final Descriptor descriptor = it.next();
                if (Utilities.equals(descriptor.getLayer(), layer)) {
                    descriptorList.add(descriptor);
                    it.remove();
                }
            }
            if (descriptorList.isEmpty()) {
                throw new AssertionError(layer);
            }
            assert Collections.disjoint(descriptorList, remaining) : layer;
            assert descriptors.containsAll(descriptorList) : layer;
            Descriptor.LOGGER.info("Traitement de la couche \"" + layer.getName() + "\" (" + descriptorList.size() + " descripteurs)");
            SamplingFeatureDescriptorPair[] pairs = new SamplingFeatureDescriptorPair[descriptorList.size() * samplingFeatures.size()];
            final Map<Descriptor, SpatioTemporalCoverage3D> coverages = new IdentityHashMap<Descriptor, SpatioTemporalCoverage3D>();
            int index = 0;
            for (final Descriptor descriptor : descriptorList) {
                if (coverages.put(descriptor, new SpatioTemporalCoverage3D(null, descriptor.getCoverage())) != null) {
                    throw new AssertionError(descriptor);
                }
                for (final SamplingFeature samplingFeature : samplingFeatures) {
                    pairs[index++] = new SamplingFeatureDescriptorPair(samplingFeature, descriptor);
                }
            }
            sort(pairs);
            Descriptor.LOGGER.info("Évaluation de " + index + " valeurs.");
            cancel = false;
            float[] values = null;
            for (index = 0; index < pairs.length; index++) {
                final SamplingFeatureDescriptorPair pair = pairs[index];
                final SamplingFeature station = pair.samplingFeature;
                final Descriptor descriptor = pair.descriptor;
                final SpatioTemporalCoverage3D coverage = coverages.get(descriptor);
                pair.value = values[0];
                updater.add(pair);
                if (cancel) {
                    updater.finished();
                    Descriptor.LOGGER.info("Remplissage interrompu.");
                    return;
                }
            }
        }
        if (withoutPosition != 0) {
            Descriptor.LOGGER.warning("Les coordonnées de " + withoutPosition + " couple(s) (station, descripteur) sont incomplètes.");
        }
        updater.finished();
        Descriptor.LOGGER.info("Remplissage de la table des mesures terminé.");
    }

    /**
     * Lance le remplissage de la table {@code "Measurements"}. Cette méthode est identique
     * à {@link #execute}, excepté qu'elle attrape les éventuelles exceptions et les fait
     * apparaître dans une interface utilisateur.
     */
    public void run() {
        try {
            execute();
        } catch (Exception exception) {
            ExceptionMonitor.show(owner, exception);
        }
    }

    /**
     * Appelle {@link #run} dans un thread en arrière-plan.
     */
    public void start() {
        final Thread thread = new Thread(this, "Remplissage de la table des mesures");
        thread.setPriority(Thread.MIN_PRIORITY + 2);
        thread.start();
    }

    /**
     * Écrit un message dans le journal avec le niveau "info".
     */
    private static void info(final int key, final Object arg) {
        LogRecord record = Resources.getResources(null).getLogRecord(Level.INFO, key, arg);
        record.setSourceClassName("MeasurementTableFiller");
        record.setSourceMethodName("execute");
        Descriptor.LOGGER.log(record);
    }

    /**
     * Indique qu'un point est en dehors de la région des données couvertes.
     * Cette méthode écrit un avertissement dans le journal, à la condition
     * qu'il n'y en avait pas déjà un.
     */
    private static void warning(final SpatioTemporalCoverage3D source, final PointOutsideCoverageException exception) {
        final LogRecord record = new LogRecord(Level.WARNING, exception.getLocalizedMessage());
        record.setSourceClassName("MeasurementTableFiller");
        record.setSourceMethodName("execute");
        record.setThrown(exception);
    }

    /**
     * Interrompt l'exécution de {@link #execute}. Cette méthode peut être appelée à partir de
     * n'importe quel thread.
     */
    public void cancel() {
        cancel = true;
    }
}
