package estimations

import Data
import ksl.utilities.math.KSLMath
import ksl.utilities.random.rvariable.GammaRV
import kotlin.test.Test

class GammaParametersTest {
    // Using digits = 10 in R
    val groundBeefShape = 4.008252567
    val groundBeefRate = 0.054419110
    val groundBeefScale = 1 / groundBeefRate

    val tolerance = 0.001

    @Test
    fun estimateGroundBeef() {
        val params = GammaParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefShape, precision = tolerance))
            { "Shape should be $groundBeefShape, was ${params[0]}" }
        assert(KSLMath.equal(params[1], groundBeefScale, precision = tolerance))
            { "Scale should be $groundBeefScale, was ${params[1]}" }
    }

    @Test
    fun estimateToxocara() {
        val result = GammaParameters().estimate(Data.toxocara)
        assert(result.isFailure) { "Estimation should have failed due to non-positive data, was a success" }
    }

    @Test
    fun estimateKSL() {
        val kslPrecision = 0.04
        val estimator = GammaParameters()
        for (shapeInt in 1..5) {
            for (scaleInt in 1..5) {
                val shape = shapeInt.toDouble()
                val scale = scaleInt.toDouble()
                val data = GammaRV(shape, scale).sample(10000)
                val params = estimator.estimate(data).getOrThrow()
                assert(KSLMath.equal(params[0], shape, precision = kslPrecision))
                    { "Shape should be $shape, was ${params[0]}" }
                assert(KSLMath.equal(params[1], scale, precision = kslPrecision))
                    { "Scale should be $scale, was ${params[0]}" }
            }
        }
    }
}