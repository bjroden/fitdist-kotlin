package estimations

public class UniformParameters : ParameterEstimatorIfc {
    /**
     * Estimate the parameters for a uniform distribution.
     * Returns parameters in the form `[min, max`].
     * @param [data] Input data.
     * @return Array containing `[min, max`]
     */
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