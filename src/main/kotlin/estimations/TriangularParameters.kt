package estimations

import org.apache.commons.math3.stat.StatUtils
import ksl.utilities.statistic.Statistic

class TriangularParameters : ParameterEstimatorIfc {
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        if (data.isEmpty()) {
            return estimateFailure("Provided data array is empty")
        }

        val min = data.min()
        val modes = StatUtils.mode(data)
        val mode = Statistic(modes).average
        val max = data.max()

        return estimateSuccess(min, mode, max)

    }
}