package estimations

import ksl.utilities.math.KSLMath
import kotlin.test.Test

class ExponentialParametersTest {
    // Using digits = 10 in R
    val groundBeefRate = 0.01357853096
    val groundBeefMean = 1 / groundBeefRate

    @Test
    fun estimate() {
        val params = ExponentialParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefMean)) { "Mean should be $groundBeefMean, was ${params[0]}" }
    }
}