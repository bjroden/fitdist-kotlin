package estimations

import Data
import ksl.utilities.math.KSLMath
import ksl.utilities.random.rvariable.NormalRV
import kotlin.math.pow
import kotlin.test.Test

class NormalParametersTest {
    // Using digits = 10 in R
    val groundBeefMean = 73.64566929
    val groundBeefStdDev = 35.81415889
    val groundBeefVariance = groundBeefStdDev.pow(2)

    val toxocaraMean = 8.679245283
    val toxocaraStdDev = 14.157834800
    val toxocaraVariance = toxocaraStdDev.pow(2)

    @Test
    fun estimateGroundBeef() {
        val params = NormalParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefMean)) { "Mean should be $groundBeefMean, was ${params[0]}" }
        assert(KSLMath.equal(params[1], groundBeefVariance)) { "Variance should be $groundBeefVariance, was ${params[1]}" }
    }

    @Test
    fun estimateToxocara() {
        val params = NormalParameters().estimate(Data.toxocara).getOrThrow()
        assert(KSLMath.equal(params[0], toxocaraMean)) { "Mean should be $toxocaraMean, was ${params[0]}" }
        assert(KSLMath.equal(params[1], toxocaraVariance)) { "Variance should be $toxocaraVariance, was ${params[1]}" }
    }

    @Test
    fun estimateRV() {
        val estimator = NormalParameters()
        for (mInt in 1..5) {
            for (vInt in 1..5) {
                val m = mInt.toDouble()
                val v = vInt.toDouble()
                val data = NormalRV(m, v).sample(Data.defaultRVSampleSize)
                val params = estimator.estimate(data).getOrThrow()
                assert(KSLMath.equal(params[0], m, precision = Data.defaultRVTestTolerance))
                    { "Mean should be $m, was ${params[0]}" }
                assert(KSLMath.equal(params[1], v, precision = Data.defaultRVTestTolerance))
                    { "Variance should be $v, was ${params[1]}" }
            }
        }
    }
}