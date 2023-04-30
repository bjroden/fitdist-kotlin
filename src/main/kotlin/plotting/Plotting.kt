package plotting

import ksl.utilities.distributions.CDFIfc
import ksl.utilities.distributions.InverseCDFIfc

/**
 * Contains functions useful for plotting graphs for distribution fitting,
 * e.g. P-P plots, Q-Q plots, etc.
 */
public object Plotting {
    /**
     * Calculate the probabilities associated with an input array based on the provided CDF function.
     * @param [data] The observed data
     * @param [dist] Class with a CDF function, which is used to generate the probabilities.
     * @return A sorted array of the distribution probabilities
     */
    public fun observedDataToProbabilities(data: DoubleArray, dist: CDFIfc): DoubleArray {
        return data.sortedArray().map { dist.cdf(it) }.toDoubleArray()
    }

    /**
     * Calculate the expected probabilities.
     * For each value i in the range 1:n, p = (1 - 0.5) / n.
     * @param [n] The number of data points to generate
     * @return An array of probabilities with size n
     */
    public fun generateExpectedProbabilities(n: Int): DoubleArray {
        return (1..n).map { (it - 0.5) / n }.toDoubleArray()
    }

    /**
     * Calculate the expected data points.
     * Probabilities are calculated using the formula: p = (i - 0.5) / n.
     * Then the inverse CDF function is applied to these probabilities.
     * @param [n] The number of data points to generate
     * @param [dist] Class with an inverse CDF function
     * @return A sorted array of probabilities with size n
     */
    public fun generateExpectedData(n: Int, dist: InverseCDFIfc): DoubleArray {
        return (1..n).map { dist.invCDF((it - 0.5) / n) }.toDoubleArray()
    }
}