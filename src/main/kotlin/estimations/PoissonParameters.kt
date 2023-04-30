package estimations

import ksl.utilities.statistic.Statistic
import kotlin.math.floor

public class PoissonParameters : ParameterEstimatorIfc {
    /**
     * Estimate the parameters for a poisson distribution.
     * Data must be non-negative integers.
     * Returns parameters in the form `[mean`].
     * @param [data] Input data. Must be non-negative integers.
     * @return Array containing `[mean`]
     */
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        if (data.any { floor(it) != it }) { return estimateFailure("Data must only contain integers") }
        if (data.any { it < 0 }) { return estimateFailure("Data cannot be negative") }
        val mean = Statistic(data).average
        return estimateSuccess(mean)
    }
}