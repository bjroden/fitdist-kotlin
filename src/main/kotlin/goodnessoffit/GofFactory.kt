package goodnessoffit

import ksl.utilities.distributions.*
import ksl.utilities.statistic.Histogram
import kotlin.math.floor

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
            return ChiSquareGofTest(expectedPositive, observedPositive, dist.parameters().size)
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
            return ChiSquareGofTest(expectedPositive, observedPositive, dist.parameters().size)
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
            return KolmogorovSmirnovGofTest(cdfs)
        }
    }
}