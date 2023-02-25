package goodnessoffit

import Data
import estimations.ExponentialParameters
import ksl.utilities.distributions.Exponential
import ksl.utilities.math.KSLMath
import ksl.utilities.random.rvariable.ExponentialRV
import kotlin.test.Test
import kotlin.test.fail

class GofFactoryTest {
    @Test
    fun manualChiSquareIntervals() {
        val data = ExponentialRV(4.0, stream = Data.rvTestStream).sample(100)
        // Breaks used in R histogram
        val breaks = (0..16 step 2).map { it.toDouble() }.toDoubleArray()
        val dist = Exponential(ExponentialParameters().estimate(data).getOrThrow())
        val test = GofFactory().continuousTest(ChiSquareRequest(breaks), data, dist)
        // TODO: fill in assert values when we confirm where the "ends" of the empirical
        //  should be when making chi square intervals
        fail("TODO: Need chi square interval comparison to R data")
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