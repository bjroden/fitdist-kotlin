package estimations

import kotlin.test.Test

class NegativeBinomialParametersTest {

    @Test
    fun estimate() {
        val result = NegativeBinomialParameters().estimate(Data.groundBeef)
        assert(result.isFailure) { "Estimation should have failed due to non-integer data, was a success" }
    }
}