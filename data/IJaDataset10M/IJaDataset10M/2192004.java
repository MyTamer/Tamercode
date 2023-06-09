package dr.inference.model;

/**
 * @version $Id: ReciprocalStatistic.java,v 1.2 2005/05/24 20:26:00 rambaut Exp $
 *
 * @author Alexei Drummond
 * @author Andrew Rambaut
 */
public class ReciprocalStatistic extends Statistic.Abstract {

    private Statistic statistic = null;

    public ReciprocalStatistic(String name, Statistic statistic) {
        super(name);
        this.statistic = statistic;
    }

    public int getDimension() {
        return statistic.getDimension();
    }

    /** @return mean of contained statistics */
    public double getStatisticValue(int dim) {
        return 1.0 / statistic.getStatisticValue(dim);
    }
}
