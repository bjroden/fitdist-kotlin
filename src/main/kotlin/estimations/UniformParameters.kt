package estimations

public class UniformParameters : ParameterEstimatorIfc {
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        val alpha = data.minOrNull()
        val beta = data.maxOrNull()

        return if (alpha == null || beta == null) {
            estimateFailure("Provided data array is empty")
        } else {
            estimateSuccess(alpha, beta)
        }
    }
}