package estimations

import ksl.utilities.math.KSLMath
import ksl.utilities.random.rvariable.TriangularRV
import org.junit.jupiter.api.Test

class TriangularParametersTest {
    // The groundBeef and toxocara tests have been (temporarily?) removed due to the fact that input analyzer uses a
    // different method of estimation than this.

    @Test
    fun estimateRV(){
        val estimator = TriangularParameters()
        val max = 100.0
        val min = 0.0
        val sampleSize = 500000

        for (modeInt in 0..100 step 10) {
            val mode = modeInt.toDouble()
            val data = TriangularRV(min, mode, max, Data.rvTestStream).sample(sampleSize)
            val params = estimator.estimate(data).getOrThrow()
            assert(KSLMath.equal(params[0], min, precision = 0.05))
                { "Min should be $min, was ${params[0]}" }
            assert(KSLMath.equal(params[1], mode, precision = Data.defaultRVTestTolerance))
                { "Mode should be $mode, was ${params[1]}" }
            assert(KSLMath.equal(params[2], max, precision = 0.05))
                { "Max should be $max, was ${params[2]}" }
        }
    }
}