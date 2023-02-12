package estimations

public interface ParameterEstimatorIfc {
    public fun estimate(data: DoubleArray): Result<DoubleArray>
}