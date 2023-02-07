package estimations

import Data
import ksl.utilities.math.KSLMath
import kotlin.test.Test
class UniformParametersTest {

    val groundBeefMin = 10.0
    val groundBeefMax = 200.0

    @Test
    fun estimate(){
        val params = UniformParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefMin)) {"Min should be $groundBeefMin, was ${params[0]}"}
        assert(KSLMath.equal(params[1], groundBeefMax)) {"Max should be $groundBeefMax, was ${params[1]}"}
    }


}