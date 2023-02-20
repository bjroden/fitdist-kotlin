package estimations

import ksl.utilities.statistic.Statistic

public class TriangularParameters : ParameterEstimatorIfc {
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        if (data.isEmpty()) {
            return estimateFailure("Provided data array is empty")
        }

        val min = data.min()
        val mean = Statistic(data).average
        val max = data.max()
        val mode = (3 * mean) - min - max

        return estimateSuccess(min, mode, max)

    }
}