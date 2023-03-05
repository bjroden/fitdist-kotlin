package goodnessoffit

import ksl.utilities.distributions.*
import ksl.utilities.statistic.Histogram
import kotlin.math.floor
import kotlin.math.roundToInt

public class GofFactory {
    public fun <T, D> continuousTest(
        request: ContinuousRequest<T>,
        data: DoubleArray,
        dist: D
    ): T
    where T: AbstractGofTest,
          D: DistributionIfc<D>,
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
          D: DistributionIfc<D>,
          D: DiscreteDistributionIfc {
        @Suppress("UNCHECKED_CAST")
        return when (request) {
            is ChiSquareRequest -> makeChiSquareDiscrete(data, dist)
        } as T
    }


    private companion object {
        private fun <T> makeChiSquareContinuous(
            data: DoubleArray,
            dist: T,
            request: ChiSquareRequest
        ): ChiSquareGofTest
        where T: DistributionIfc<T>,
              T: ContinuousDistributionIfc {
            val bins = request.breakPoints ?: automaticBins(data)
            val observedCounts = countObserved(data, bins)
            val expectedCounts = countExpected(data.size, dist, bins)
            val positiveExpectedCounts = expectedCounts.indices.filter { expectedCounts[it] > 0 }
            val expectedPositive = expectedCounts.sliceArray(positiveExpectedCounts)
            val observedPositive = observedCounts.sliceArray(positiveExpectedCounts)
            return ChiSquareGofTest(expectedPositive, observedPositive, dist.parameters().size)
        }

        private fun <T> makeChiSquareDiscrete(
            data: DoubleArray,
            dist: T
        ): ChiSquareGofTest
        where T: DistributionIfc<T>,
              T: DiscreteDistributionIfc {
            require(data.all { floor(it) == it }) { "Data input cannot contain non-integer data" }
            val intData = data.map { it.roundToInt() }.toIntArray()
            val observedCounts = countObservedDiscrete(intData)
            val expectedCounts = countExpectedDiscrete(data.size, intData, dist)
            val positiveExpectedCounts = expectedCounts.indices.filter { expectedCounts[it] > 0 }
            val expectedPositive = expectedCounts.sliceArray(positiveExpectedCounts)
            val observedPositive = observedCounts.sliceArray(positiveExpectedCounts)
            return ChiSquareGofTest(expectedPositive, observedPositive, dist.parameters().size)
        }

        private fun countObservedDiscrete(data: IntArray): DoubleArray {
            val counts = IntArray(data.max() + 1)
            data.forEach { counts[it] += 1 }
            return counts.map { it.toDouble() }.toDoubleArray()
        }

        private fun countExpectedDiscrete(n: Int, data: IntArray, dist: PMFIfc): DoubleArray {
            return (0..data.max()).map { n * dist.pmf(it.toDouble()) }.toDoubleArray()
        }

        private fun countObserved(data: DoubleArray, bins: DoubleArray): DoubleArray {
            val histogram = Histogram(bins)
            histogram.collect(data)
            return doubleArrayOf(histogram.underFlowCount, *histogram.binCounts, histogram.overFlowCount)
        }

        private fun countExpected(n: Int, dist: CDFIfc, bins: DoubleArray): DoubleArray {
            val underflow = n * dist.cdf(bins.min())
            val overflow = n * (1 - dist.cdf(bins.max()))
            val middle = bins.toList().zipWithNext { lower, upper ->
                n * ( dist.cdf(upper) - dist.cdf(lower) )
            }.toDoubleArray()
            return doubleArrayOf(underflow, *middle, overflow)
        }

        private fun automaticBins(data: DoubleArray) = Histogram.recommendBreakPoints(data)

        private fun makeKs(data: DoubleArray, dist: ContinuousDistributionIfc): KolmogorovSmirnovGofTest {
            val cdfs = data.sortedArray().map { dist.cdf(it) }.toDoubleArray()
            return KolmogorovSmirnovGofTest(cdfs)
        }
    }
}