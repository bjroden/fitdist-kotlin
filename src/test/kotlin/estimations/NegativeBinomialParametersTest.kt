package estimations

import ksl.utilities.math.KSLMath
import kotlin.test.Test

class NegativeBinomialParametersTest {
    // Using digits = 10 in R
    // TODO: R seems to use a real number definition instead of an integer definition. See if these should be changed
    val toxocaraSize = 0.3971457488
    val toxocaraMu = 8.6802520252

    @Test
    fun estimateGroundBeef() {
        val result = NegativeBinomialParameters().estimate(Data.groundBeef)
        assert(result.isFailure) { "Estimation should have failed due to non-integer data, was a success" }
    }

    @Test
    fun estimateToxocara() {
        val params = NegativeBinomialParameters().estimate(Data.toxocara).getOrThrow()
        assert(KSLMath.equal(params[0], toxocaraSize)) { "Size should be $toxocaraSize, was ${params[0]}" }
        assert(KSLMath.equal(params[1], toxocaraMu)) { "Mu should be $toxocaraMu, was ${params[1]}" }
    }
}