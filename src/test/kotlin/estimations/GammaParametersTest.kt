package estimations

import Data
import ksl.utilities.math.KSLMath
import kotlin.test.Test

class GammaParametersTest {
    // Using digits = 10 in R
    val groundBeefShape = 4.008252567
    val groundBeefRate = 0.054419110
    val groundBeefScale = 1 / groundBeefRate

    @Test
    fun estimate() {
        val params = GammaParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefShape)) { "Shape should be $groundBeefShape, was ${params[0]}" }
        assert(KSLMath.equal(params[1], groundBeefScale)) { "Scale should be $groundBeefScale, was ${params[1]}" }
    }
}