package goodnessoffit

import ksl.utilities.distributions.CDFIfc
import ksl.utilities.distributions.ContinuousDistributionIfc
import ksl.utilities.distributions.DiscreteDistributionIfc

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
            TODO()
        }

        private fun makeKs(data: DoubleArray, dist: ContinuousDistributionIfc): KolmogorovSmirnovGofTest {
            val expected = (1..data.size).map { it.toDouble() / data.size }.toDoubleArray()
            val observed = data.sortedArray().map { dist.cdf(it) }.toDoubleArray()
            return KolmogorovSmirnovGofTest(expected, observed)
        }
    }
}