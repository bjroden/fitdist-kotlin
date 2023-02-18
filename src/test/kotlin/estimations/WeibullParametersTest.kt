package estimations

import Data
import ksl.utilities.math.KSLMath
import ksl.utilities.random.rvariable.WeibullRV
import kotlin.test.Test

class WeibullParametersTest {
    // Using digits = 10 in R
    val groundBeefShape = 2.18588486
    val groundBeefScale = 83.34767905

    val tolerance = 0.001

    @Test
    fun estimateGroundBeef() {
        val params = WeibullParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefShape, precision = tolerance))
            { "Shape should be $groundBeefShape, was ${params[0]}" }
        assert(KSLMath.equal(params[1], groundBeefScale, precision = tolerance))
            { "Scale should be $groundBeefScale, was ${params[1]}" }
    }

    @Test
    fun estimateToxocara() {
        val result = WeibullParameters().estimate(Data.toxocara)
        assert(result.isFailure) { "Estimation should have failed due to non-positive data, was a success" }
    }

    @Test
    fun estimateRV() {
        val estimator = WeibullParameters()
        for (shapeInt in 1..5) {
            for (scaleInt in 1..5) {
                val shape = shapeInt.toDouble()
                val scale = scaleInt.toDouble()
                val data = WeibullRV(shape, scale).sample(Data.defaultRVSampleSize)
                val params = estimator.estimate(data).getOrThrow()
                assert(KSLMath.equal(params[0], shape, precision = Data.defaultRVTestTolerance))
                    { "Shape should be $shape, was ${params[0]}" }
                assert(KSLMath.equal(params[1], scale, precision = Data.defaultRVTestTolerance))
                    { "Scale should be $scale, was ${params[1]}" }
            }
        }
    }

}