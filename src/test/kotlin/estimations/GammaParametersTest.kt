package estimations

import Data
import ksl.utilities.math.KSLMath
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
}