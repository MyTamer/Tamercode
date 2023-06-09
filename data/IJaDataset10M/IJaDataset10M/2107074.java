package playground.meisterk.kti.controler.listeners;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.ScoringEvent;
import org.matsim.core.controler.events.ShutdownEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.ScoringListener;
import org.matsim.core.controler.listener.ShutdownListener;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.scoring.EventsToScore;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.ScoringFunctionAccumulator;
import org.matsim.core.utils.io.IOUtils;

public class ScoreElements implements StartupListener, ScoringListener, ShutdownListener {

    public static final String[] SCORE_ELEMENT_NAMES = new String[] { "sum", "perf", "wait", "short", "facilityPenalty", "negative" };

    private final String filename;

    private BufferedWriter out;

    public ScoreElements(final String filename) {
        super();
        this.filename = filename;
    }

    public void notifyStartup(StartupEvent event) {
        try {
            this.out = IOUtils.getBufferedWriter(event.getControler().getControlerIO().getOutputFilename(this.filename));
            this.out.write("#iteration");
            for (String str : ScoreElements.SCORE_ELEMENT_NAMES) {
                this.out.write("\t" + str);
            }
            this.out.write(System.getProperty("line.separator"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyScoring(ScoringEvent event) {
        playground.meisterk.kti.scoring.ActivityScoringFunction asf = null;
        TreeMap<String, Double> sumScoreParts = new TreeMap<String, Double>();
        for (String str : ScoreElements.SCORE_ELEMENT_NAMES) {
            sumScoreParts.put(str, 0.0);
        }
        double d;
        Controler c = event.getControler();
        for (Person p : c.getPopulation().getPersons().values()) {
            ScoringFunction sf = c.getPlansScoring().getScoringFunctionForAgent(p.getId());
            if (sf instanceof ScoringFunctionAccumulator) {
                if (((ScoringFunctionAccumulator) sf).getActivityScoringFunctions().get(0) instanceof playground.meisterk.kti.scoring.ActivityScoringFunction) {
                    asf = (playground.meisterk.kti.scoring.ActivityScoringFunction) ((ScoringFunctionAccumulator) sf).getActivityScoringFunctions().get(0);
                    d = sumScoreParts.get("sum") + sf.getScore();
                    sumScoreParts.put("sum", d);
                    d = sumScoreParts.get("perf") + asf.getPerformanceScore();
                    sumScoreParts.put("perf", d);
                    d = sumScoreParts.get("wait") + asf.getWaitingTimeScore();
                    sumScoreParts.put("wait", d);
                    d = sumScoreParts.get("short") + asf.getTooShortDurationScore();
                    sumScoreParts.put("short", d);
                    d = sumScoreParts.get("facilityPenalty") + asf.getFacilityPenaltiesScore();
                    sumScoreParts.put("facilityPenalty", d);
                    d = sumScoreParts.get("negative") + asf.getNegativeDurationScore();
                    sumScoreParts.put("negative", d);
                }
            }
        }
        int popSize = c.getPopulation().getPersons().size();
        try {
            this.out.write(Integer.toString(event.getIteration()));
            for (String str : ScoreElements.SCORE_ELEMENT_NAMES) {
                out.write("\t" + Double.toString(sumScoreParts.get(str) / popSize));
            }
            this.out.write(System.getProperty("line.separator"));
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyShutdown(ShutdownEvent event) {
        try {
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
