package estimations

import Data
import ksl.utilities.math.KSLMath
import kotlin.test.Test

class WeibullParametersTest {
    // Using digits = 10 in R
    val groundBeefShape = 2.18588486
    val groundBeefScale = 83.34767905

    @Test
    fun estimate() {
        val params = WeibullParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefShape)) { "Shape should be $groundBeefShape, was ${params[0]}" }
        assert(KSLMath.equal(params[1], groundBeefScale)) { "Scale should be $groundBeefScale, was ${params[1]}" }
    }
}