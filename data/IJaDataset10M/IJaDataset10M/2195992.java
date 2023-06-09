package jmbench.tools.runtime.generator;

import jmbench.interfaces.BenchmarkMatrix;
import jmbench.interfaces.MatrixFactory;
import jmbench.tools.OutputError;
import jmbench.tools.runtime.InputOutputGenerator;
import jmbench.tools.stability.StabilityBenchmark;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;
import java.util.Random;
import static jmbench.misc.RandomizeMatrices.convertToEjml;
import static jmbench.misc.RandomizeMatrices.randomize;

/**
 * @author Peter Abeles
 */
public class QrGenerator implements InputOutputGenerator {

    DenseMatrix64F A;

    @Override
    public BenchmarkMatrix[] createInputs(MatrixFactory factory, Random rand, boolean checkResults, int size) {
        BenchmarkMatrix[] inputs = new BenchmarkMatrix[1];
        inputs[0] = factory.create(size, size);
        randomize(inputs[0], -1, 1, rand);
        if (checkResults) {
            A = convertToEjml(inputs[0]);
        }
        return inputs;
    }

    @Override
    public OutputError checkResults(BenchmarkMatrix[] output, double tol) {
        if (output[0] == null || output[1] == null) {
            return OutputError.MISC;
        }
        SimpleMatrix Q = SimpleMatrix.wrap(convertToEjml(output[0]));
        SimpleMatrix R = SimpleMatrix.wrap(convertToEjml(output[1]));
        if (Q.hasUncountable() || R.hasUncountable()) {
            return OutputError.UNCOUNTABLE;
        }
        SimpleMatrix A_found = Q.mult(R);
        double error = StabilityBenchmark.residualError(A_found.getMatrix(), A);
        if (error > tol) {
            return OutputError.LARGE_ERROR;
        }
        return OutputError.NO_ERROR;
    }

    @Override
    public int numOutputs() {
        return 2;
    }

    @Override
    public long getRequiredMemory(int matrixSize) {
        return 8L * matrixSize * matrixSize * 6L;
    }
}
