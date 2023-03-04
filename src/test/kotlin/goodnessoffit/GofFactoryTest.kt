package goodnessoffit

import Data
import ksl.utilities.distributions.Exponential
import ksl.utilities.math.KSLMath
import ksl.utilities.random.rvariable.ExponentialRV
import kotlin.test.Test

class GofFactoryTest {
    @Test
    fun manualChiSquareIntervals() {
        val chiSquareValueR = 4.49955476
        val estimatedMeanR = 1 / 0.2507453184
        val pValueR = 0.7207708875

        val data = ExponentialRV(4.0, stream = Data.rvTestStream).sample(100)
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