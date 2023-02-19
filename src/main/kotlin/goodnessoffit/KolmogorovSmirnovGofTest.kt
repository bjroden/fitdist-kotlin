package goodnessoffit

import estimations.allNonnegative
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest


// TODO: From what I can tell, Apache commons math expects you to either use their RealDistribution class or
//  use a 2-sample method of calculating the test statistics. Neither are really applicable here.
//  Either find a better way of using the library / restructuring parameter arguments, or calculate
//  test information by hand
public class KolmogorovSmirnovGofTest(
    expected: DoubleArray,
    observed: DoubleArray
) : AbstractGofTest(), PValueIfc {

    override val testScore: Double
    override val pValue: Double

    init {
        require(expected.size == observed.size) { "Expected and observed arrays must be same size" }
        require(expected.size >= 2) { "Expected and observed array length must be >= 2" }
        require(allNonnegative(expected) && allNonnegative(observed)) { "Expected and observed array length must be >= 2" }

        // TODO: These need to be adjusted
        val tester = KolmogorovSmirnovTest()
        testScore = tester.kolmogorovSmirnovStatistic(expected, observed)
        pValue = tester.kolmogorovSmirnovTest(expected, observed)
    }
}