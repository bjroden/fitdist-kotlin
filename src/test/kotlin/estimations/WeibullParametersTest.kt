package estimations

import Data
import ksl.utilities.math.KSLMath
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
}