package goodnessoffit

import ksl.utilities.statistic.Histogram


/**
 * Marker interface used by GofFactory to tell what type of goodness of fit test is requested
 */
public sealed interface GofRequest <T: AbstractGofTest>

/**
 * Marker interface to request a continuous goodness of fit test from GofFactory
 */
public sealed interface ContinuousRequest <T: AbstractGofTest> : GofRequest<T>

/**
 * Marker interface to request a discrete goodness of fit test from GofFactory
 */
public sealed interface DiscreteRequest <T: AbstractGofTest> : GofRequest<T>

/**
 * Request a chi-square goodness of fit test
 */
public class ChiSquareRequest(
    // TODO: documentation for this, with differences between continuous and discrete tests
    public val breakPoints: DoubleArray
): ContinuousRequest<ChiSquareGofTest>, DiscreteRequest<ChiSquareGofTest> {
    public companion object {
        public fun automaticBinsContinuous(data: DoubleArray): DoubleArray =
            Histogram.recommendBreakPoints(data)

        public fun automaticBinsDiscrete(data: DoubleArray): DoubleArray =
            (0..data.max().toInt())
                .map { it.toDouble() }
                .toDoubleArray()
    }
}


/**
 * Request a Kolmogorov-Smirnov goodness of fit test
 */
public object KSRequest : ContinuousRequest<KolmogorovSmirnovGofTest>