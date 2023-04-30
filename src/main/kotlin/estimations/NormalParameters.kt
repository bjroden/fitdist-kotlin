package estimations

import ksl.utilities.statistic.Statistic
import org.apache.commons.math3.stat.descriptive.moment.Variance

public class NormalParameters : ParameterEstimatorIfc {
    /**
     * Estimate the parameters for a normal distribution.
     * Returns parameters in the form `[mean, variance`].
     * @param [data] Input data.
     * @return Array containing `[mean, variance`]
     */
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        val mean = Statistic(data).average
        val variance = Variance(false).evaluate(data)
        return estimateSuccess(mean, variance)
    }
}