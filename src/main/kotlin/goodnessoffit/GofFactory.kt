package goodnessoffit

import estimations.allNonnegative
import ksl.utilities.KSLArrays
import ksl.utilities.distributions.*
import ksl.utilities.statistic.Histogram
import org.apache.commons.math3.distribution.ChiSquaredDistribution
import org.apache.commons.math3.stat.inference.ChiSquareTest
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest
import kotlin.math.floor
import kotlin.math.max

public class GofFactory {
    public fun <T, D> continuousTest(
        request: ContinuousRequest<T>,
        data: DoubleArray,
        dist: D
    ): T
    where T: AbstractGofTest,
          D: DistributionIfc<*>,
          D: ContinuousDistributionIfc {
        @Suppress("UNCHECKED_CAST")
        return when (request) {
            is ChiSquareRequest -> makeChiSquareContinuous(data, dist, request)
            is KSRequest -> makeKs(data, dist)
        } as T
    }

    public fun <T, D> discreteTest(
        request: DiscreteRequest<T>,
        data: DoubleArray,
        dist: D
    ): T
    where T: AbstractGofTest,
          D: DistributionIfc<*>,
          D: DiscreteDistributionIfc {
        @Suppress("UNCHECKED_CAST")
        return when (request) {
            is ChiSquareRequest -> makeChiSquareDiscrete(data, dist, request)
        } as T
    }

    private companion object {
        private fun makeChiSquareFromArrays(
            expected: DoubleArray,
            observed: DoubleArray,
            parameterCount: Int
        ): ChiSquareGofTest {
            require(expected.size == observed.size) { "Expected and observed arrays must be same size" }
            require(expected.size >= 2) { "Expected and observed array length must be >= 2" }
            require(allNonnegative(expected) && allNonnegative(observed))
                { "Expected and observed arrays must be non-negative" }

            val intervals = expected.size
            val degreesOfFreedom = intervals - 1 - parameterCount
            require(degreesOfFreedom > 0) { "Degrees of freedom must be > 0" }
            val longObserved = observed.map { it.toLong() }.toLongArray()
            val testScore = ChiSquareTest().chiSquare(expected, longObserved)
            val pValue = 1 - ChiSquaredDistribution(degreesOfFreedom.toDouble()).cumulativeProbability(testScore)
            val universalScore = pValue
            val warnings = ChiSquareWarning.values().filter { warning ->
                when (warning) {
                    ChiSquareWarning.SMALL_EXPECTED_COUNTS -> expected.count { it <= 5 } >= 0.2 * expected.size
                }
            }.toSet()
            return ChiSquareGofTest(
                testScore, pValue, universalScore, warnings, degreesOfFreedom, intervals
            )
        }

        private fun makeKSTestFromCdfs(cdfs: DoubleArray): KolmogorovSmirnovGofTest {
            require(cdfs.size >= 2) { "Array length must be >= 2" }
            require(cdfs.all { it in 0.0..1.0 }) { "K-S cdf values must be in [0,1]" }

            val nInt = cdfs.size
            val n = nInt.toDouble()
            val dPlus = cdfs.mapIndexed { i, value -> ((i + 1) / n) - value }.max()
            val dMinus = cdfs.mapIndexed { i, value -> value - (i / n) }.max()
            val testScore = max(dPlus, dMinus)
            val pValue = 1 - KolmogorovSmirnovTest().cdf(testScore, nInt)
            val universalScore = pValue
            val warnings = KSWarning.values().filter { warning ->
                when (warning) {
                    KSWarning.TIED_VALUES -> !KSLArrays.isAllDifferent(cdfs)
                }
            }.toSet()
            return KolmogorovSmirnovGofTest(testScore, pValue, universalScore, warnings)
        }

        private fun <T> makeChiSquareContinuous(
            data: DoubleArray,
            dist: T,
            request: ChiSquareRequest
        ): ChiSquareGofTest
        where T: DistributionIfc<*>,
              T: ContinuousDistributionIfc {
            val observedCounts = countObservedContinuous(data, request.breakPoints)
            val expectedCounts = countExpectedContinuous(data.size, dist, request.breakPoints)
            val positiveExpectedCounts = expectedCounts.indices.filter { expectedCounts[it] > 0 }
            val expectedPositive = expectedCounts.sliceArray(positiveExpectedCounts)
            val observedPositive = observedCounts.sliceArray(positiveExpectedCounts)
            return makeChiSquareFromArrays(expectedPositive, observedPositive, dist.parameters().size)
        }

        private fun <T> makeChiSquareDiscrete(
            data: DoubleArray,
            dist: T,
            request: ChiSquareRequest
        ): ChiSquareGofTest
        where T: DistributionIfc<*>,
              T: DiscreteDistributionIfc {
            require(data.all { floor(it) == it }) { "Data input cannot contain non-integer data" }
            val observedCounts = countObservedDiscrete(data, request.breakPoints)
            val expectedCounts = countExpectedDiscrete(data.size, dist, request.breakPoints)
            val positiveExpectedCounts = expectedCounts.indices.filter { expectedCounts[it] > 0 }
            val expectedPositive = expectedCounts.sliceArray(positiveExpectedCounts)
            val observedPositive = observedCounts.sliceArray(positiveExpectedCounts)
            return makeChiSquareFromArrays(expectedPositive, observedPositive, dist.parameters().size)
        }

        private fun countObservedDiscrete(data: DoubleArray, bins: DoubleArray): DoubleArray {
            val first = data.count { it <= bins[0] }.toDouble()
            val rest = bins.toList().zipWithNext { lower, higher ->
                data.count { lower < it && it <= higher }.toDouble()
            }.toDoubleArray()
            return doubleArrayOf(first, *rest)
        }

        private fun countExpectedDiscrete(n: Int, dist: DiscreteDistributionIfc, bins: DoubleArray): DoubleArray {
            val first = n * dist.cdf(bins.min())
            val rest = bins.toList().zipWithNext { lower, higher ->
                n * ( dist.cdf(higher) - dist.cdf(lower) )
            }.toDoubleArray()
            return doubleArrayOf(first, *rest)
        }

        private fun countObservedContinuous(data: DoubleArray, bins: DoubleArray): DoubleArray {
            val histogram = Histogram(bins).apply { collect(data) }
            return doubleArrayOf(histogram.underFlowCount, *histogram.binCounts, histogram.overFlowCount)
        }

        private fun countExpectedContinuous(n: Int, dist: CDFIfc, bins: DoubleArray): DoubleArray {
            val underflow = n * dist.cdf(bins.min())
            val overflow = n * (1 - dist.cdf(bins.max()))
            val middle = bins.toList().zipWithNext { lower, upper ->
                n * ( dist.cdf(upper) - dist.cdf(lower) )
            }.toDoubleArray()
            return doubleArrayOf(underflow, *middle, overflow)
        }

        private fun makeKs(data: DoubleArray, dist: ContinuousDistributionIfc): KolmogorovSmirnovGofTest {
            val cdfs = data.sortedArray().map { dist.cdf(it) }.toDoubleArray()
            return makeKSTestFromCdfs(cdfs)
        }
    }
}