package goodnessoffit

import Data
import ksl.utilities.distributions.Exponential
import ksl.utilities.distributions.Poisson
import ksl.utilities.math.KSLMath
import ksl.utilities.random.rvariable.ExponentialRV
import ksl.utilities.random.rvariable.PoissonRV
import kotlin.test.Test

class GofFactoryTest {
    @Test
    fun chiSquareContinuous() {
        val testMean = 4.0
        val estimatedMeanR = 1 / 0.2507453184
        val sampleSize = 100
        val chiSquareValueR = 4.49955476
        val pValueR = 0.7207708875

        val data = ExponentialRV(testMean, stream = Data.rvTestStream).sample(sampleSize)
        // Breaks used in R histogram
        val breaks = (0..16 step 2).map { it.toDouble() }.toDoubleArray()
        val dist = Exponential(estimatedMeanR)
        val test = GofFactory().continuousTest(ChiSquareRequest(breaks), data, dist)
        assert(KSLMath.equal(test.testScore, chiSquareValueR))
            { "Chi square value should be $chiSquareValueR, was ${test.testScore}" }
        assert(KSLMath.equal(test.testScore, chiSquareValueR))
            { "Chi square p-value should be $pValueR, was ${test.pValue}" }
    }

    @Test
    fun chiSquareDiscrete() {
        val testMean = 4.0
        val estimatedMeanR = 4.04
        val sampleSize = 100
        val chiSquareValueR = 3.16790051
        val pValueR = 0.8690477356
        // Apache commons' Chi Square test resizes arrays if expected and observed counts are different,
        //  which is the case here, so results will differ slightly
        val precision = 0.05

        val data = PoissonRV(testMean, stream = Data.rvTestStream).sample(sampleSize)
        val dist = Poisson(estimatedMeanR)
        val test = GofFactory().discreteTest(ChiSquareRequest(), data, dist)
        assert(KSLMath.equal(test.testScore, chiSquareValueR, precision = precision))
            { "Chi square value should be $chiSquareValueR, was ${test.testScore}" }
        assert(KSLMath.equal(test.testScore, chiSquareValueR, precision = precision))
            { "Chi square p-value should be $pValueR, was ${test.pValue}" }
    }

    @Test
    fun kolmogorovSmirnovTest() {
        val ksValueR = 0.074913363
        val estimatedMeanR = 1 / 0.2507453184
        val pValueR = 0.6286257

        val data = ExponentialRV(4.0, stream = Data.rvTestStream).sample(100)
        val dist = Exponential(estimatedMeanR)
        val test = GofFactory().continuousTest(KSRequest, data, dist)
        assert(KSLMath.equal(test.testScore, ksValueR, precision = 0.000001))
            { "KS score should be $ksValueR, was ${test.testScore}" }
        // TODO: Currently fails, even when the cdf method used in KS class is replaced with the RealDistribution
        //  method with hardcoded values. See if this is an acceptable deviation from R.
        assert(KSLMath.equal(test.pValue, pValueR, precision = 0.000001))
            { "KS score should be $pValueR, was ${test.pValue}" }
    }
}