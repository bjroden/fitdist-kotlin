package goodnessoffit

import ksl.utilities.distributions.CDFIfc
import ksl.utilities.distributions.ContinuousDistributionIfc
import ksl.utilities.distributions.DiscreteDistributionIfc
import ksl.utilities.statistic.Histogram

public class GofFactory {
    public fun <T: AbstractGofTest> continuousTest(
        request: ContinuousRequest<T>,
        data: DoubleArray,
        dist: ContinuousDistributionIfc
    ): T {
        @Suppress("UNCHECKED_CAST")
        return when (request) {
            is ChiSquareRequest -> makeChiSquare(data, dist, request)
            is KSRequest -> makeKs(data, dist)
        } as T
    }

    public fun <T: AbstractGofTest> discreteTest(
        request: DiscreteRequest<T>,
        data: DoubleArray,
        dist: DiscreteDistributionIfc
    ): T {
        @Suppress("UNCHECKED_CAST")
        return when (request) {
            is ChiSquareRequest -> makeChiSquare(data, dist, request)
        } as T
    }


    private companion object {
        private fun makeChiSquare(
            data: DoubleArray,
            dist: CDFIfc,
            request: ChiSquareRequest
        ): ChiSquareGofTest {
            val bins = request.breakPoints ?: automaticBins(data)
            val observedCounts = countObserved(data, bins)
            val expectedCounts = countExpected(data, dist, bins)
            return ChiSquareGofTest(expectedCounts, observedCounts)
        }

        private fun countObserved(data: DoubleArray, bins: DoubleArray): DoubleArray {
            // Use same method of histogram collection as R:
            // lower < value <= higher, with min value going to first bucket
            val counts = bins.toList().zipWithNext { lower, upper ->
                    data.count { lower < it && it <= upper  }.toDouble()
                }.toMutableList()
            counts[0] = counts[0] + data.count { it == bins.min() }
            return counts.toDoubleArray()
        }

        private fun countExpected(data: DoubleArray, dist: CDFIfc, bins: DoubleArray): DoubleArray {
            val n = data.size
            return bins.toList().zipWithNext { lower, upper ->
                n * ( dist.cdf(upper) - dist.cdf(lower) )
            }.toDoubleArray()
        }

        private fun automaticBins(data: DoubleArray) = Histogram.recommendBreakPoints(data)

        private fun makeKs(data: DoubleArray, dist: ContinuousDistributionIfc): KolmogorovSmirnovGofTest {
            val cdfs = data.sortedArray().map { dist.cdf(it) }.toDoubleArray()
            return KolmogorovSmirnovGofTest(cdfs)
        }
    }
}