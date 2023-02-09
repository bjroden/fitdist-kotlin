package estimations

import ksl.utilities.statistic.Statistic

class ExponentialParameters : ParameterEstimatorIfc {
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        if (data.any { it < 0 }) { return estimateFailure("Data must be positive") }
        val mean = Statistic(data).average
        return estimateSuccess(mean)
    }
}