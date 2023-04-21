package fr.cpbrennestt.metier.entite;

import static javax.persistence.GenerationType.IDENTITY;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * Joueur generated by hbm2java
 */
@Entity
@Table(name = "joueur", catalog = "cpbrennestt")
public class Joueur implements java.io.Serializable {

    public static final String ACTIVITE_EXTERIEUR = "EXTERIEUR";

    public static final String ACTIVITE_LOISIR_JEUNE = "LOISIR_JEUNE";

    public static final String ACTIVITE_LOISIR_ADULTE = "LOISIR_ADULTE";

    public static final String ACTIVITE_COMPET_JEUNE = "COMPET_JEUNE";

    public static final String ACTIVITE_COMPET_ADULTE = "COMPET_ADULTE";

    public static final String ACTIVITE_ARBITRE = "ARBITRE";

    public static final String ACTIVITE_MEMBRE_BUREAU = "MEMBRE_BUREAU";

    public static final Map<String, String> listeActivites() {
        Map<String, String> listeActivites = new LinkedHashMap<String, String>();
        listeActivites.put(Joueur.ACTIVITE_COMPET_ADULTE, "Comp�tition Adulte");
        listeActivites.put(Joueur.ACTIVITE_COMPET_JEUNE, "Comp�tition Jeune");
        listeActivites.put(Joueur.ACTIVITE_LOISIR_ADULTE, "Loisir Adulte");
        listeActivites.put(Joueur.ACTIVITE_LOISIR_JEUNE, "Loisir Jeune");
        listeActivites.put(Joueur.ACTIVITE_MEMBRE_BUREAU, "Membre du Bureau");
        listeActivites.put(Joueur.ACTIVITE_ARBITRE, "Arbitre");
        listeActivites.put(Joueur.ACTIVITE_EXTERIEUR, "Ext�rieur");
        return listeActivites;
    }

    public static final String VETERAN_1 = "V1";

    public static final String VETERAN_2 = "V2";

    public static final String VETERAN_3 = "V3";

    public static final String VETERAN_3D = "V3D";

    public static final String VETERAN_4 = "V4";

    public static final String SENIOR = "S";

    public static final String JUNIOR_1 = "J1";

    public static final String JUNIOR_2 = "J2";

    public static final String JUNIOR_3 = "J3";

    public static final String CADET_1 = "C1";

    public static final String CADET_2 = "C2";

    public static final String MINIME_1 = "M1";

    public static final String MINIME_2 = "M2";

    public static final String BENJAMIN_1 = "B1";

    public static final String BENJAMIN_2 = "B2";

    public static final String POUSSIN = "P";

    public static final List<String> listeCategories() {
        List<String> listeCategories = new LinkedList<String>();
        listeCategories.add(Joueur.POUSSIN);
        listeCategories.add(Joueur.BENJAMIN_1);
        listeCategories.add(Joueur.BENJAMIN_2);
        listeCategories.add(Joueur.MINIME_1);
        listeCategories.add(Joueur.MINIME_2);
        listeCategories.add(Joueur.CADET_1);
        listeCategories.add(Joueur.CADET_2);
        listeCategories.add(Joueur.JUNIOR_1);
        listeCategories.add(Joueur.JUNIOR_2);
        listeCategories.add(Joueur.JUNIOR_3);
        listeCategories.add(Joueur.SENIOR);
        listeCategories.add(Joueur.VETERAN_1);
        listeCategories.add(Joueur.VETERAN_2);
        listeCategories.add(Joueur.VETERAN_3);
        listeCategories.add(Joueur.VETERAN_3D);
        listeCategories.add(Joueur.VETERAN_4);
        return listeCategories;
    }

    private Integer idJoueur;

    private Equipe equipe;

    private Fiche fiche;

    private String numLicence;

    private Integer annee;

    private String nom;

    private String prenom;

    private boolean sexe;

    private String categorie;

    private String classement;

    private int points;

    private String activite;

    private Mutation mutation;

    private MembreBureau membreBureau;

    private Set<Brulage> brulages = new HashSet<Brulage>(0);

    private Set<Modification> modifications = new HashSet<Modification>(0);

    public Joueur() {
    }

    public Joueur(Fiche fiche, String numLicence, Integer annee, String nom, String prenom, boolean sexe, String categorie, String classement, int points, String activite) {
        this.fiche = fiche;
        this.numLicence = numLicence;
        this.annee = annee;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.categorie = categorie;
        this.classement = classement;
        this.points = points;
        this.activite = activite;
    }

    public Joueur(Equipe equipe, Fiche fiche, String numLicence, Integer annee, String nom, String prenom, boolean sexe, String categorie, String classement, int points, String activite, Mutation mutation, MembreBureau membreBureau, Set<Modification> modifications, Set<Brulage> brulages) {
        this.equipe = equipe;
        this.fiche = fiche;
        this.numLicence = numLicence;
        this.nom = nom;
        this.prenom = prenom;
        this.annee = annee;
        this.sexe = sexe;
        this.categorie = categorie;
        this.classement = classement;
        this.points = points;
        this.activite = activite;
        this.mutation = mutation;
        this.membreBureau = membreBureau;
        this.modifications = modifications;
        this.brulages = brulages;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_joueur", unique = true, nullable = false)
    public Integer getIdJoueur() {
        return this.idJoueur;
    }

    public void setIdJoueur(Integer idJoueur) {
        this.idJoueur = idJoueur;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipe")
    public Equipe getEquipe() {
        return this.equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fiche", nullable = false)
    public Fiche getFiche() {
        return this.fiche;
    }

    public void setFiche(Fiche fiche) {
        this.fiche = fiche;
    }

    @Column(name = "num_licence", unique = true, nullable = false)
    public String getNumLicence() {
        return this.numLicence;
    }

    public void setNumLicence(String numLicence) {
        this.numLicence = numLicence;
    }

    @Column(name = "annee")
    public Integer getAnnee() {
        return this.annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    @Column(name = "nom", nullable = false, length = 150)
    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Column(name = "prenom", nullable = false, length = 150)
    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Column(name = "sexe", nullable = false)
    public boolean isSexe() {
        return this.sexe;
    }

    public void setSexe(boolean sexe) {
        this.sexe = sexe;
    }

    @Column(name = "categorie", nullable = false, length = 50)
    public String getCategorie() {
        return this.categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Column(name = "classement", nullable = false, length = 10)
    public String getClassement() {
        return this.classement;
    }

    public void setClassement(String classement) {
        this.classement = classement;
    }

    @Column(name = "points", nullable = false)
    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Column(name = "activite", nullable = false, length = 50)
    public String getActivite() {
        return this.activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "joueur")
    public Mutation getMutation() {
        return this.mutation;
    }

    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "joueur")
    @LazyToOne(LazyToOneOption.PROXY)
    public MembreBureau getMembreBureau() {
        return this.membreBureau;
    }

    public void setMembreBureau(MembreBureau membreBureau) {
        this.membreBureau = membreBureau;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "joueur")
    public Set<Modification> getModifications() {
        return this.modifications;
    }

    public void setModifications(Set<Modification> modifications) {
        this.modifications = modifications;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "joueur")
    public Set<Brulage> getBrulages() {
        return this.brulages;
    }

    public void setBrulages(Set<Brulage> brulages) {
        this.brulages = brulages;
    }
}