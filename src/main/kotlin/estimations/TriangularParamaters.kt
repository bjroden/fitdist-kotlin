package estimations

import org.apache.commons.math3.stat.StatUtils
import ksl.utilities.statistic.Statistic

class TriangularParamaters : ParameterEstimatorIfc {
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        //Error Check
        if(data.isEmpty()){
            return estimateFailure("Provided data array is empty")
        }

        //Calculate values
        val min = data.min()
        val modes = StatUtils.mode(data)
        val mode = Statistic(modes).average
        val max = data.max()

        //Return as expected
        return estimateSuccess(min, mode, max)

    }
}