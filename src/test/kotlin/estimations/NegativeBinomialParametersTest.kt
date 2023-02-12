package estimations

import ksl.utilities.math.KSLMath
import kotlin.test.Test

class NegativeBinomialParametersTest {
    // Using digits = 10 in R
    // R uses a mean-based parameter instead of probability of success, so our expected parameters differ
    val toxocaraSize = 0.3971457488
    val toxocaraProb = toxocaraSize / (Data.toxocara.average() + toxocaraSize)
    val toxocaraMu = 8.6802520252

    @Test
    fun estimateGroundBeef() {
        val result = NegativeBinomialParameters().estimate(Data.groundBeef)
        assert(result.isFailure) { "Estimation should have failed due to non-integer data, was a success" }
    }

    @Test
    fun estimateToxocara() {
        val estimator = NegativeBinomialParameters()
        val data = Data.toxocara
        val params = estimator.estimate(data).getOrThrow()
        assert(KSLMath.equal(params[0], toxocaraProb)) { "Probability should be $toxocaraProb, was ${params[0]}" }
        assert(KSLMath.equal(params[1], toxocaraSize)) { "Size should be $toxocaraSize, was ${params[1]}" }

        val estN = estimator.estimateNumSuccesses(data, params[0]).getOrThrow()
        val estP = estimator.estimateProbSuccess(data, params[1]).getOrThrow()
        assert(KSLMath.equal(estP, toxocaraProb)) { "Probability should be $toxocaraProb, was $estP"}
        assert(KSLMath.equal(estN, toxocaraSize)) { "Size should be $toxocaraSize, was $estN"}
    }
}