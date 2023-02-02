interface ParameterEstimatorIfc {
    fun estimate(data: DoubleArray): Result<DoubleArray>
}