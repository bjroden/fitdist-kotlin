package estimations

import ksl.utilities.math.KSLMath
import ksl.utilities.random.rvariable.ExponentialRV
import kotlin.test.Test

class ExponentialParametersTest {
    // Using digits = 10 in R
    val groundBeefRate = 0.01357853096
    val groundBeefMean = 1 / groundBeefRate

    val toxocaraRate = 0.1152173925
    val toxocaraMean = 1 / toxocaraRate

    @Test
    fun estimateGroundBeef() {
        val params = ExponentialParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefMean)) { "Mean should be $groundBeefMean, was ${params[0]}" }
    }

    @Test
    fun estimateToxocara() {
        val params = ExponentialParameters().estimate(Data.toxocara).getOrThrow()
        assert(KSLMath.equal(params[0], toxocaraMean)) { "Mean should be $toxocaraMean, was ${params[0]}" }
    }

    @Test
    fun estimateKSL() {
        val kslPrecision = 0.01
        val estimator = ExponentialParameters()
        for (mInt in 1..5) {
            val m = mInt.toDouble()
            val data = ExponentialRV(m).sample(10000)
            val params = estimator.estimate(data).getOrThrow()
            assert(KSLMath.equal(params[0], m, precision = kslPrecision))
                { "Mean should be $m, was ${params[0]}" }
        }
    }
}