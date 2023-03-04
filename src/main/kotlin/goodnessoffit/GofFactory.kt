package goodnessoffit

import ksl.utilities.distributions.CDFIfc
import ksl.utilities.distributions.ContinuousDistributionIfc
import ksl.utilities.distributions.DiscreteDistributionIfc
import ksl.utilities.distributions.DistributionIfc
import ksl.utilities.statistic.Histogram

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
            is ChiSquareRequest -> makeChiSquare(data, dist, request)
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
            is ChiSquareRequest -> makeChiSquare(data, dist, request)
        } as T
    }


    private companion object {
        private fun <T> makeChiSquare(
            data: DoubleArray,
            dist: DistributionIfc<T>,
            request: ChiSquareRequest
        ): ChiSquareGofTest {
            val bins = request.breakPoints ?: automaticBins(data)
            val observedCounts = countObserved(data, bins)
            val expectedCounts = countExpected(data.size, dist, bins)
            val positiveExpectedCounts = expectedCounts.indices.filter { expectedCounts[it] > 0 }
            val expectedPositive = expectedCounts.sliceArray(positiveExpectedCounts)
            val observedPositive = observedCounts.sliceArray(positiveExpectedCounts)
            return ChiSquareGofTest(expectedPositive, observedPositive, dist.parameters().size)
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