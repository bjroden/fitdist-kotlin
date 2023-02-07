package estimations

import ksl.utilities.math.KSLMath
import org.junit.jupiter.api.Test


class TriangularParametersTest {

    val groundBeefMin = 10.0
    val groundBeefMode = 80.0
    val groundBeefMax = 200.0


    @Test
    fun estimate(){
        val params = TriangularParamaters().estimate(Data.groundBeef).getOrThrow()

        assert(KSLMath.equal(params[0], groundBeefMin)) {"Min should be $groundBeefMin, was ${params[0]}"}
        assert(KSLMath.equal(params[1], groundBeefMode)) {"Mode should be $groundBeefMode, was ${params[1]}"}
        assert(KSLMath.equal(params[2], groundBeefMax)) {"Max should be $groundBeefMax, was ${params[2]}"}
    }

}