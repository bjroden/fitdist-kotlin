package estimations

import Data
import ksl.utilities.math.KSLMath
import kotlin.test.Test
class UniformParametersTest {

    val groundBeefMin = 10.0
    val groundBeefMax = 200.0

    val toxocaraMin = 0.0
    val toxocaraMax = 75.0

    @Test
    fun estimateGroundBeef(){
        val params = UniformParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefMin)) { "Min should be $groundBeefMin, was ${params[0]}" }
        assert(KSLMath.equal(params[1], groundBeefMax)) { "Max should be $groundBeefMax, was ${params[1]}" }
    }

    @Test
    fun estimateToxocara(){
        val params = UniformParameters().estimate(Data.toxocara).getOrThrow()
        assert(KSLMath.equal(params[0], toxocaraMin)) { "Min should be $toxocaraMin, was ${params[0]}" }
        assert(KSLMath.equal(params[1], toxocaraMax)) { "Max should be $toxocaraMax, was ${params[1]}" }
    }
}