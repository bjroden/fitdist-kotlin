package goodnessoffit

import estimations.allNonnegative
import org.apache.commons.math3.distribution.ChiSquaredDistribution
import org.apache.commons.math3.stat.inference.ChiSquareTest

// TODO: Double check correctness of all parts used
public class ChiSquareGofTest(
    expected: DoubleArray,
    observed: DoubleArray,
    parameterCount: Int
) : AbstractGofTest(), PValueIfc {

    override val testScore: Double
    override val pValue: Double
    override val warnings: Set<ChiSquareWarning>
    public val degreesOfFreedom: Int
    public val intervals: Int

    init {
        require(expected.size == observed.size) { "Expected and observed arrays must be same size" }
        require(expected.size >= 2) { "Expected and observed array length must be >= 2" }
        require(allNonnegative(expected) && allNonnegative(observed)) { "Expected and observed array length must be >= 2" }

        intervals = expected.size
        degreesOfFreedom = intervals - 1 - parameterCount
        val longObserved = observed.map { it.toLong() }.toLongArray()
        testScore = ChiSquareTest().chiSquare(expected, longObserved)
        pValue = 1 - ChiSquaredDistribution(degreesOfFreedom.toDouble()).cumulativeProbability(testScore)
        warnings = ChiSquareWarning.values().filter { warning ->
            when (warning) {
                ChiSquareWarning.SMALL_EXPECTED_COUNTS -> expected.count { it <= 5 } >= 0.2 * expected.size
            }
        }.toSet()
    }

    public enum class ChiSquareWarning : WarningIfc {
        SMALL_EXPECTED_COUNTS {
            override val message: String
                get() = ">20% of intervals had less than 5 expected values"
        }
    }
}