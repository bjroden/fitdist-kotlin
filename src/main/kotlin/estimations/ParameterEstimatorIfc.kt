package estimations

/**
 * Interface to estimate parameters for a distribution
 */
public interface ParameterEstimatorIfc {
    /**
     * Computes the estimated parameters from input data.
     * Unless otherwise specified, the values will be in the same order that
     * is used to instantiate the corresponding KSL class.
     * @param [data] Input data.
     * @return The estimated parameters in the form of an array of doubles.
     */
    public fun estimate(data: DoubleArray): Result<DoubleArray>
}