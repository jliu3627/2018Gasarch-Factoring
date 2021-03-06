package test.factor;

import java.util.ArrayList;

public class ConfidenceIntervalCalculator {
    private final double ZSCORE = 2.0;

    private ArrayList<Long> values;
    private double outlierBound;
    private long mean, median, q1, q3, sd;
    private boolean statsUpToDate;

    public ConfidenceIntervalCalculator(ArrayList<Long> values, double outlierBound) {
        this.values = values;
        this.outlierBound = outlierBound;
        statsUpToDate = false;
    }

    public ConfidenceIntervalCalculator(ArrayList<Long> values) {
        this(values, 1.5);
    }

    public ConfidenceIntervalCalculator(double outlierBound) {
        this(new ArrayList<>(), outlierBound);
    }

    public ConfidenceIntervalCalculator() {
        this(new ArrayList<>(), 1.5);
    }

    /**
     * Updates statistics and removes outliers using the outlier bound.
     */
    public void removeOutliers() {
        int initialLength = values.size();
        if (values.size() <= 10) {return;}
        if (!statsUpToDate) { updateStatistics(); }
        long minBound = q1 - (long) (outlierBound * (q3 - q1));
        long maxBound = q3 + (long) (outlierBound * (q3 - q1));
        while (values.get(0) < minBound) {values.remove(0);}
        while (values.get(values.size()-1) > maxBound) {values.remove(values.size()-1);}
        if (values.size() != initialLength) { statsUpToDate = false; }
    }

    public void updateStatistics() {
        calculateMean();
        calculateMedian();
        calculateSD();

        statsUpToDate = true;
    }

    /**
     * Calculates the mean of the accumulated values
     */
    private void calculateMean() {
        long mean = 0; int numValues = values.size();
        for (Long v : values) {
            mean += v / values.size();
        }
        this.mean = mean;
    }

    /**
     * Calculates the median, first quartile, and third quartile of the accumulated values
     */
    private void calculateMedian() {
        this.median = values.get(values.size() / 2);
        this.q1 = values.get(values.size() / 4);
        this.q3 = values.get(values.size() / 4 * 3);
    }

    /**
     * Calculates the standard deviation of the accumulated values
     */
    private void calculateSD() {
        long sd = 0;
        for (long v : values) {
            sd += Math.pow((v - mean),2)/values.size();
        }
        this.sd = (long) Math.pow(sd, .5);
    }

    public void addTime(long t) {
        if (values.size() == 0) { values.add(t); return; }

        int lowBound = 0; int highBound = values.size() - 1;
        int ind;
        while (lowBound < highBound) {
            ind = (highBound + lowBound) / 2;
            if (t < values.get(ind)) {
                highBound = ind;
            } else {
                lowBound = ind + 1;
            }
        }
        values.add(lowBound, t);
        statsUpToDate = false;
    }

    public void addTime(long t, boolean checkIfOutlier) {
        if (!checkIfOutlier || t > q1 - outlierBound * (q3 - q1) && t < q3 + outlierBound * (q3 - q1)) {
            addTime(t);
        }
    }

    public ArrayList<Long> getValues() {
        return values;
    }

    public long getMean() {return mean; }

    public int getSampleSize() {return values.size();}

    public long getMarginOfError() {
        return sd / (long) (Math.pow(values.size(), .5) * ZSCORE);
    }
}
