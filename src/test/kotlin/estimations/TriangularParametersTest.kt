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
        
        var mode = 0.0
        var data = TriangularRV(min, mode, max, Data.rvTestStream).sample(sampleSize)
        var params = estimator.estimate(data).getOrThrow()
        assert(KSLMath.equal(params[1], mode, precision = Data.defaultRVTestTolerance))
            { "Mode should be $mode, was ${params[1]}" }

        mode = 10.0
        data = TriangularRV(min, mode, max, Data.rvTestStream).sample(sampleSize)
        params = estimator.estimate(data).getOrThrow()
        assert(KSLMath.equal(params[1], mode, precision = Data.defaultRVTestTolerance))
            { "Mode should be $mode, was ${params[1]}" }

        mode = 50.0
        data = TriangularRV(min, mode, max, Data.rvTestStream).sample(sampleSize)
        params = estimator.estimate(data).getOrThrow()
        assert(KSLMath.equal(params[1], mode, precision = Data.defaultRVTestTolerance))
            { "Mode should be $mode, was ${params[1]}" }

        mode = 80.0
        data = TriangularRV(min, mode, max, Data.rvTestStream).sample(sampleSize)
        params = estimator.estimate(data).getOrThrow()
        assert(KSLMath.equal(params[1], mode, precision = Data.defaultRVTestTolerance))
            { "Mode should be $mode, was ${params[1]}" }

        mode = 100.0
        data = TriangularRV(min, mode, max, Data.rvTestStream).sample(sampleSize)
        params = estimator.estimate(data).getOrThrow()
        assert(KSLMath.equal(params[1], mode, precision = Data.defaultRVTestTolerance))
            { "Mode should be $mode, was ${params[1]}" }
    }

}