package playground.scnadine.gpsCompareWithFlammData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.core.config.Config;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.scenario.ScenarioLoaderImpl;
import playground.scnadine.gpsProcessing.GPSActivities;
import playground.scnadine.gpsProcessing.GPSCoordFactory;
import playground.scnadine.gpsProcessing.GPSCoordFactoryFlamm;
import playground.scnadine.gpsProcessing.GPSFileFilterFLAMM;
import playground.scnadine.gpsProcessing.GPSTrips;
import playground.scnadine.gpsProcessing.activityAlgorithms.GPSActivityCalcCharacteristics;
import playground.scnadine.gpsProcessing.activityAlgorithms.GPSActivityWriteActivityStatistics;
import playground.scnadine.gpsProcessing.coordAlgorithms.GPSCoordDetermineActivityBreakPoints;
import playground.scnadine.gpsProcessing.coordAlgorithms.GPSCoordFilteringAndSmoothing;
import playground.scnadine.gpsProcessing.coordAlgorithms.GPSCoordWriteCoordsFlamm;
import playground.scnadine.gpsProcessing.coordAlgorithms.GPSCoordWriteCoordsForAnalysis;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripCalcMostProbableModes;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripCalcStageCharacteristics;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripCalcTripCharacteristics;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripCorrectModes;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripDetermineModeChangingPoints;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripGenerateStages;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripModeDetection;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripWriteStageStatistics;
import playground.scnadine.gpsProcessing.tripAlgorithms.GPSTripWriteTripStatistics;

public class GPSCompareWithFlammData {

    static File[] allFiles;

    public static void main(String[] args) throws IOException, ParseException {
        Gbl.startMeasurement();
        final ScenarioImpl scenario = new ScenarioLoaderImpl(args[0]).getScenario();
        final Config config = scenario.getConfig();
        final String CONFIG_MODULE = "GPS";
        System.out.println(config.getModule(CONFIG_MODULE));
        File sourcedir = new File(config.findParam(CONFIG_MODULE, "sourcedir"));
        GPSCoordFactory gpsFactory = new GPSCoordFactoryFlamm();
        GPSFileFilterFLAMM filter = new GPSFileFilterFLAMM();
        allFiles = sourcedir.listFiles(filter);
        GPSActivities activities = new GPSActivities();
        FlammActivities flammActivities = new FlammActivities();
        GPSTrips trips = new GPSTrips(config, CONFIG_MODULE);
        FlammTrips flammTrips = new FlammTrips();
        FlammSPPEpisodes flammSPPs = new FlammSPPEpisodes();
        FlammMTPs mtps = new FlammMTPs(config, CONFIG_MODULE);
        FlammTPAs tpas = new FlammTPAs();
        ActivityMatchings activityMatchings = new ActivityMatchings(config, CONFIG_MODULE, mtps);
        for (int i = 0; i < allFiles.length; i++) {
            System.out.println("Processing file no " + (i + 1) + "\n");
            System.out.println("Reading GPS points...");
            gpsFactory.createGPSCoords(allFiles[i]);
            System.out.println("... done. \nNumber of GPS coords: " + gpsFactory.getCoords().size());
            System.out.println();
            System.out.println("Filtering and smoothing GPS points...");
            GPSCoordFilteringAndSmoothing filteringAndSmoothing = new GPSCoordFilteringAndSmoothing(config, CONFIG_MODULE);
            filteringAndSmoothing.run(gpsFactory);
            System.out.println("... done. \nNumber of GPS coords: " + gpsFactory.getCoords().size());
            System.out.println();
            System.out.println("Determining trips and activities...");
            GPSCoordDetermineActivityBreakPoints activityBreakPoints = new GPSCoordDetermineActivityBreakPoints(config, CONFIG_MODULE);
            activityBreakPoints.run(gpsFactory);
            trips.createGPSTrips(gpsFactory);
            activities.createActivities(gpsFactory);
            GPSActivityCalcCharacteristics activityCharacteristicsCalculator = new GPSActivityCalcCharacteristics(config, CONFIG_MODULE);
            activityCharacteristicsCalculator.run(activities);
            GPSTripCalcTripCharacteristics tripCharacteristicsCalculator = new GPSTripCalcTripCharacteristics();
            tripCharacteristicsCalculator.run(trips);
            System.out.println("... done.");
            System.out.println("Number of trips: " + trips.getTrips().size());
            System.out.println("Number of activities: " + activities.getActivities().size());
            System.out.println();
            System.out.println("Determining Flamm trips, activities and SPPs...");
            flammTrips.createFlammTrips(gpsFactory);
            flammActivities.createFlammActivities(gpsFactory);
            flammSPPs.createSPPs(gpsFactory);
            GPSTripCalcTripCharacteristics flammTripCharacteristicsCalculator = new GPSTripCalcTripCharacteristics();
            flammTripCharacteristicsCalculator.run(flammTrips);
            System.out.println("... done.");
            System.out.println("Number of Flamm trips: " + flammTrips.getTrips().size());
            System.out.println("Number of Flamm activities: " + flammActivities.getActivities().size());
            System.out.println("Number of Flamm SPPs: " + flammSPPs.getSPPs().size());
            System.out.println();
            System.out.println("Determining stages and modes...");
            GPSTripDetermineModeChangingPoints determineMTPs = new GPSTripDetermineModeChangingPoints(config, CONFIG_MODULE);
            determineMTPs.run(trips);
            GPSTripGenerateStages generateStages = new GPSTripGenerateStages();
            generateStages.run(trips);
            GPSTripCalcStageCharacteristics stageCharacteristicsCalculator = new GPSTripCalcStageCharacteristics();
            stageCharacteristicsCalculator.run(trips);
            GPSTripModeDetection detectModes = new GPSTripModeDetection(config, CONFIG_MODULE);
            detectModes.run(trips);
            GPSTripCorrectModes correctingModeChains = new GPSTripCorrectModes(config, CONFIG_MODULE);
            correctingModeChains.run(trips);
            GPSTripCalcMostProbableModes mostProbableModeChain = new GPSTripCalcMostProbableModes();
            mostProbableModeChain.run(trips);
            System.out.println("... done.");
            System.out.println();
            System.out.println("Determining Flamm stages, mtps, tpas and modes...");
            flammTrips.createFlammStages();
            mtps.createMTPs(gpsFactory);
            flammTrips.assignFlammMTPs(mtps);
            tpas.createTPAs(gpsFactory);
            flammTrips.assignFlammTPAs(tpas);
            System.out.println("... done.");
            System.out.println();
            System.out.println("Determine trip matchings...");
            activityMatchings.createActivityMatchings(activities, flammActivities);
            System.out.println("... done.");
            System.out.println();
            System.out.println("Write individual output...");
            GPSCoordWriteCoordsFlamm writeCoordsFlamm = new GPSCoordWriteCoordsFlamm(config, CONFIG_MODULE);
            writeCoordsFlamm.run(gpsFactory);
            GPSCoordWriteCoordsForAnalysis writeCoordsForAnalysis = new GPSCoordWriteCoordsForAnalysis((i != 0), config, CONFIG_MODULE);
            writeCoordsForAnalysis.run(gpsFactory);
            GPSTripWriteTripStatistics writeTripStatistics = new GPSTripWriteTripStatistics((i != 0), config, CONFIG_MODULE);
            writeTripStatistics.run(trips);
            GPSActivityWriteActivityStatistics writeActivityStatistics = new GPSActivityWriteActivityStatistics((i != 0), config, CONFIG_MODULE, "ActivityStatistics");
            writeActivityStatistics.run(activities);
            GPSActivityWriteActivityStatistics writeFlammActivityStatistics = new GPSActivityWriteActivityStatistics((i != 0), config, CONFIG_MODULE, "FlammActivityStatistics");
            writeFlammActivityStatistics.run(flammActivities);
            GPSTripWriteStageStatistics writeStageStatistics = new GPSTripWriteStageStatistics((i != 0), config, CONFIG_MODULE);
            writeStageStatistics.run(trips);
            WriteActivityMatchings writeActivityMatchings = new WriteActivityMatchings((i != 0), config, CONFIG_MODULE);
            writeActivityMatchings.run(activityMatchings);
            System.out.println("... done.");
            System.out.println();
            gpsFactory.initialiseFactory();
            activities.initialiseActivities();
            flammActivities.initialiseActivities();
            trips.initialiseTrips();
            activityMatchings.clearAll();
        }
        System.out.println("All done.");
        System.out.println("=========================================================================================");
    }
}
