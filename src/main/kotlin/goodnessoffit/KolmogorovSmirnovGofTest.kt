package goodnessoffit

import estimations.allNonnegative
import ksl.utilities.KSLArrays
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest
import kotlin.math.max


public class KolmogorovSmirnovGofTest(
    cdfs: DoubleArray,
) : AbstractGofTest(), PValueIfc {

    override val testScore: Double
    override val pValue: Double
    override val warnings: Set<WarningIfc>

    init {
        require(cdfs.size >= 2) { "Array length must be >= 2" }
        require(allNonnegative(cdfs)) { "Array data must be non-negative" }

        val nInt = cdfs.size
        val n = nInt.toDouble()
        val dPlus = cdfs.mapIndexed { i, value -> ((i + 1) / n) - value }.max()
        val dMinus = cdfs.mapIndexed { i, value -> value - (i / n) }.max()
        testScore = max(dPlus, dMinus)
        pValue = 1 - KolmogorovSmirnovTest().cdf(testScore, nInt)
        warnings = KSWarning.values().filter { warning ->
            when (warning) {
                KSWarning.TIED_VALUES -> !KSLArrays.isStrictlyIncreasing(cdfs)
            }
        }.toSet()
    }

    public enum class KSWarning: WarningIfc {
        TIED_VALUES {
            override val message: String
                get() = "Tied values should not be present in the Kolmogorov-Smirnov Test"
        }
    }
}