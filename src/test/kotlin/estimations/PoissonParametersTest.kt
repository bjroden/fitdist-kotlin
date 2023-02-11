package estimations

import ksl.utilities.math.KSLMath
import kotlin.test.Test

class PoissonParametersTest {
    // Using digits = 10 in R
    val toxocaraLambda = 8.67924533

    @Test
    fun estimateGroundBeef() {
        val result = PoissonParameters().estimate(Data.groundBeef)
        assert(result.isFailure) { "Estimation should have failed due to non-integer data, was a success" }
    }

    @Test
    fun estimateToxocara() {
        val params = PoissonParameters().estimate(Data.toxocara).getOrThrow()
        assert(KSLMath.equal(params[0], toxocaraLambda)) { "Mean should be $toxocaraLambda, was ${params[0]}" }
    }
}