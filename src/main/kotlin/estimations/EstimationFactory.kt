package estimations

import ksl.utilities.distributions.*
import kotlin.reflect.KClass

/**
 * Provides estimated parameters for distributions
 */
public object EstimationFactory {
    /**
     * Get the estimation class for the requested distribution type.
     * @param [dist] The type of distribution to estimate
     * @return The appropriate estimator class
     */
    public fun getEstimator(dist: DistributionType): ParameterEstimatorIfc = when (dist) {
        DistributionType.ExponentialType -> ExponentialParameters()
        DistributionType.GammaType -> GammaParameters()
        DistributionType.NegativeBinomialType -> NegativeBinomialParameters()
        DistributionType.NormalType -> NormalParameters()
        DistributionType.PoissonType -> PoissonParameters()
        DistributionType.TriangularType -> TriangularParameters()
        DistributionType.UniformType -> UniformParameters()
        DistributionType.WeibullType -> WeibullParameters()
    }

    /**
     * Generate an estimated KSL distribution class based on the input data
     * @param [dist] The type of distribution to estimate
     * @param [data] The input data used for estimations
     * @return The corresponding KSL distribution class with estimated parameters
     */
    public fun getDistribution(dist: DistributionType, data: DoubleArray): Result<DistributionIfc<*>> {
        val params = getEstimator(dist).estimate(data)
        return params.map {
            when (dist) {
                DistributionType.ExponentialType -> Exponential(it)
                DistributionType.GammaType -> Gamma(it)
                DistributionType.NegativeBinomialType -> NegativeBinomial(it)
                DistributionType.NormalType -> Normal(it)
                DistributionType.PoissonType -> Poisson(it)
                DistributionType.TriangularType -> Triangular(it)
                DistributionType.UniformType -> Uniform(it)
                DistributionType.WeibullType -> Weibull(it)
            }
        }
    }
}

/**
 * Enum class which contains all the distributions that can have their parameters estimated.
 * Intended for use with the EstimationFactory.
 */
public enum class DistributionType(
    /**
     * The corresponding KSL distribution
     */
    public val distType: KClass<out DistributionIfc<*>>,

    /**
     * Name of the distribution
     */
    public val distName: String
) {
    ExponentialType(Exponential::class, "Exponential"),
    GammaType(Gamma::class, "Gamma"),
    NegativeBinomialType(NegativeBinomial::class, "Negative Binomial"),
    NormalType(Normal::class, "Normal"),
    PoissonType(Poisson::class, "Poisson"),
    TriangularType(Triangular::class, "Triangular"),
    UniformType(Uniform::class, "Uniform"),
    WeibullType(Weibull::class, "Weibull")
}
