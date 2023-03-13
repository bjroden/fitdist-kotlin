package estimations

import ksl.utilities.distributions.*
import kotlin.reflect.KClass

public object EstimationFactory {
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

public enum class DistributionType(
    public val distType: KClass<out DistributionIfc<*>>,
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
