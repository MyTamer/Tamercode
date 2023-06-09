package org.ejml.alg.dense.linsol.chol;

import org.ejml.alg.dense.decomposition.TriangularSolver;
import org.ejml.alg.dense.decomposition.chol.CholeskyDecompositionLDL;
import org.ejml.alg.dense.linsol.LinearSolverAbstract;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.SpecializedOps;

/**
 * @author Peter Abeles
 */
public class LinearSolverCholLDL extends LinearSolverAbstract {

    private CholeskyDecompositionLDL decomp;

    private int n;

    private double vv[];

    private double el[];

    private double d[];

    public LinearSolverCholLDL(CholeskyDecompositionLDL decomp) {
        this.decomp = decomp;
    }

    public LinearSolverCholLDL() {
        this.decomp = new CholeskyDecompositionLDL();
    }

    @Override
    public boolean setA(DenseMatrix64F A) {
        _setA(A);
        if (decomp.decompose(A)) {
            n = A.numCols;
            vv = decomp._getVV();
            el = decomp.getL().data;
            d = decomp.getD();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public double quality() {
        return Math.abs(SpecializedOps.diagProd(decomp.getL()));
    }

    /**
     * <p>
     * Using the decomposition, finds the value of 'X' in the linear equation below:<br>
     *
     * A*x = b<br>
     *
     * where A has dimension of n by n, x and b are n by m dimension.
     * </p>
     * <p>
     * *Note* that 'b' and 'x' can be the same matrix instance.
     * </p>
     *
     * @param B A matrix that is n by m.  Not modified.
     * @param X An n by m matrix where the solution is writen to.  Modified.
     */
    @Override
    public void solve(DenseMatrix64F B, DenseMatrix64F X) {
        if (B.numCols != X.numCols && B.numRows != n && X.numRows != n) {
            throw new IllegalArgumentException("Unexpected matrix size");
        }
        int numCols = B.numCols;
        double dataB[] = B.data;
        double dataX[] = X.data;
        for (int j = 0; j < numCols; j++) {
            for (int i = 0; i < n; i++) vv[i] = dataB[i * numCols + j];
            solveInternal();
            for (int i = 0; i < n; i++) dataX[i * numCols + j] = vv[i];
        }
    }

    /**
     * Used internally to find the solution to a single column vector.
     */
    private void solveInternal() {
        TriangularSolver.solveL(el, vv, n);
        for (int i = 0; i < n; i++) {
            vv[i] /= d[i];
        }
        TriangularSolver.solveTranL(el, vv, n);
    }

    /**
     * Sets the matrix 'inv' equal to the inverse of the matrix that was decomposed.
     *
     * @param inv Where the value of the inverse will be stored.  Modified.
     */
    @Override
    public void invert(DenseMatrix64F inv) {
        if (inv.numRows != n || inv.numCols != n) {
            throw new RuntimeException("Unexpected matrix dimension");
        }
        double a[] = inv.data;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = (i == j) ? 1.0 : 0.0;
                for (int k = i - 1; k >= j; k--) {
                    sum -= el[i * n + k] * a[j * n + k];
                }
                a[j * n + i] = sum;
            }
        }
        for (int i = 0; i < n; i++) {
            double inv_d = 1.0 / d[i];
            for (int j = 0; j <= i; j++) {
                a[j * n + i] *= inv_d;
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                double sum = (i < j) ? 0 : a[j * n + i];
                for (int k = i + 1; k < n; k++) {
                    sum -= el[k * n + i] * a[j * n + k];
                }
                a[i * n + j] = a[j * n + i] = sum;
            }
        }
    }

    @Override
    public boolean modifiesA() {
        return decomp.inputModified();
    }

    @Override
    public boolean modifiesB() {
        return false;
    }
}
