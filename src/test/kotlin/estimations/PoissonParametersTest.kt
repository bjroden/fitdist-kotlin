package estimations

import kotlin.test.Test

class PoissonParametersTest {

    @Test
    fun estimate() {
        val result = PoissonParameters().estimate(Data.groundBeef)
        assert(result.isFailure) { "Estimation should have failed due to non-integer data, was a success" }
    }
}