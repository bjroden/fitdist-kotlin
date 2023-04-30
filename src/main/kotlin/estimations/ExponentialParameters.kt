package estimations

import ksl.utilities.statistic.Statistic

public class ExponentialParameters : ParameterEstimatorIfc {
    /**
     * Estimate the parameters for an exponential distribution
     * Returns parameters in the form `[mean`].
     * @param [data] Input data.
     * @return Array containing `[mean`]
     */
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        if (data.any { it < 0 }) { return estimateFailure("Data must be positive") }
        val mean = Statistic(data).average
        return estimateSuccess(mean)
    }
}