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
    /**
     * Breakpoints used for chi-square intervals. Must be strictly increasing.
     *
     * If used for a continuous test, intervals will be created from the breakpoints,
     * and an extra interval will be created from negative infinity to the lowest breakpoint value,
     * as well as an extra interval from the highest breakpoint value to positive infinity.
     * For example, if breakPoints = `[0, 2, 4`], the intervals will be
     * `[-∞..0, 0..2, 2..4, 4..∞`].
     *
     * If used for a discrete test, intervals will be based on the frequency counts for each value
     * in the breakpoints array. If there is a gap greater than 1 between two breakPoint values, these
     * will be combined into one interval. For example, the breakPoints `[0, 2, 3`] will give intervals
     * `[0, 1..2, 3`]. If the lowest value is not zero, the first interval will be from 0 to that value.
     */
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