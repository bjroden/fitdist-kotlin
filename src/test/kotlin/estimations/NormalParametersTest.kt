package estimations

import Data
import ksl.utilities.math.KSLMath
import kotlin.math.pow
import kotlin.test.Test

class NormalParametersTest {
    // Using digits = 10 in R
    val groundBeefMean = 73.64566929
    val groundBeefStdDev = 35.81415889
    val groundBeefVariance = groundBeefStdDev.pow(2)

    @Test
    fun estimate() {
        val params = NormalParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefMean)) { "Mean should be $groundBeefMean, was ${params[0]}" }
        assert(KSLMath.equal(params[1], groundBeefVariance)) { "Variance should be $groundBeefVariance, was ${params[1]}" }
    }
}