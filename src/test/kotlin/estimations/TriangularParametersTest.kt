package estimations

import ksl.utilities.math.KSLMath
import org.junit.jupiter.api.Test

//TODO: Fix the tests to align with the new implementation
class TriangularParametersTest {

    val groundBeefMin = 10.0
    val groundBeefMode = 80.0
    val groundBeefMax = 200.0

    val toxocaraMin = 0.0
    val toxocaraMode = 0.0
    val toxocaraMax = 75.0

    @Test
    fun estimateGroundBeef(){
        val params = TriangularParameters().estimate(Data.groundBeef).getOrThrow()
        assert(KSLMath.equal(params[0], groundBeefMin)) { "Min should be $groundBeefMin, was ${params[0]}" }
        assert(KSLMath.equal(params[1], groundBeefMode)) { "Mode should be $groundBeefMode, was ${params[1]}" }
        assert(KSLMath.equal(params[2], groundBeefMax)) { "Max should be $groundBeefMax, was ${params[2]}" }
    }

    @Test
    fun estimateToxocara(){
        val params = TriangularParameters().estimate(Data.toxocara).getOrThrow()
        assert(KSLMath.equal(params[0], toxocaraMin)) { "Min should be $toxocaraMin, was ${params[0]}" }
        assert(KSLMath.equal(params[1], toxocaraMode)) { "Mode should be $toxocaraMode, was ${params[1]}" }
        assert(KSLMath.equal(params[2], toxocaraMax)) { "Max should be $toxocaraMax, was ${params[2]}" }
    }
}