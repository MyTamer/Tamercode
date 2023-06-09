package com.rapidminer.operator.validation;

import java.util.List;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.MissingIOObjectException;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.PortPairExtender;
import com.rapidminer.tools.math.AverageVector;

/**
 * Tools class for validation operators. This class provides methods for average
 * building of performance vectors and other average vectors.
 * 
 * @author Ingo Mierswa
 */
public class Tools {

    /**
	 * Searches for the average vectors in the given IOContainer and fills the
	 * list if it is empty or build the averages. Only performance vectors are
	 * averaged.
	 * @deprecated This method is no longer needed.
	 */
    @Deprecated
    public static void handleAverages(IOContainer evalOutput, List<AverageVector> averageVectors) throws OperatorException {
        handleAverages(evalOutput, averageVectors, true);
    }

    /**
	 * Searches for the average vectors in the given IOContainer and fills the
	 * list if it is empty or build the averages. The boolean flag
	 * onlyPerformanceVectors indicates if the average should be built from
	 * PerformanceVectors only or from other AverageVectors too. Throws a
	 * NullPointerException if averageVectors is null.
	 * @deprecated This method is no longer needed.
	 */
    @Deprecated
    public static void handleAverages(IOContainer evalOutput, List<AverageVector> averageVectors, boolean onlyPerformanceVectors) throws OperatorException {
        Class<? extends IOObject> requestClass = AverageVector.class;
        if (onlyPerformanceVectors) requestClass = PerformanceVector.class;
        if (averageVectors.size() == 0) {
            boolean inputOk = true;
            while (inputOk) {
                try {
                    AverageVector currentAverage = (AverageVector) evalOutput.remove(requestClass);
                    averageVectors.add(currentAverage);
                    for (int i = 0; i < currentAverage.getSize(); i++) currentAverage.getAveragable(i).setAverageCount(0);
                } catch (MissingIOObjectException e) {
                    inputOk = false;
                }
            }
        } else {
            for (int n = 0; n < averageVectors.size(); n++) {
                AverageVector currentAverage = (AverageVector) evalOutput.remove(requestClass);
                AverageVector oldVector = averageVectors.get(n);
                if (!oldVector.getClass().isInstance(currentAverage)) throw new OperatorException("ValidationChain: Average vector mismatch! Fatal error.");
                for (int i = 0; i < oldVector.size(); i++) {
                    oldVector.getAveragable(i).buildAverage(currentAverage.getAveragable(i));
                }
            }
        }
    }

    /**
     * Returns the first performance vector in the given list or null if no
     * performance vectors exist.
     * @deprecated This method is no longer needed.
     */
    @Deprecated
    public static PerformanceVector getPerformanceVector(List<AverageVector> averageVectors) {
        java.util.Iterator<AverageVector> i = averageVectors.iterator();
        while (i.hasNext()) {
            AverageVector currentAverage = i.next();
            if (currentAverage instanceof PerformanceVector) return (PerformanceVector) currentAverage;
        }
        return null;
    }

    /** Iterates {@link #buildAverages(InputPort, OutputPort)} 
	 *  over pairs generated by this extender.
	 * @throws OperatorException 
	 */
    public static void buildAverages(PortPairExtender portExtender) throws OperatorException {
        for (PortPairExtender.PortPair pair : portExtender.getManagedPairs()) {
            buildAverages(pair.getInputPort(), pair.getOutputPort());
        }
    }

    public static void buildAverages(InputPort inputPort, OutputPort outputPort) throws OperatorException {
        AverageVector performance = inputPort.getDataOrNull();
        if (performance == null) {
            return;
        }
        if (outputPort.getDataOrNull() == null) {
            for (int i = 0; i < performance.size(); i++) {
                performance.getAveragable(i).setAverageCount(0);
            }
            outputPort.deliver(performance);
        } else {
            AverageVector average = outputPort.getData(AverageVector.class);
            if (!average.getClass().isInstance(performance)) {
                throw new RuntimeException("Average vector mismatch!");
            }
            for (int i = 0; i < average.size(); i++) {
                average.getAveragable(i).buildAverage(performance.getAveragable(i));
            }
            outputPort.deliver(average);
        }
    }
}
