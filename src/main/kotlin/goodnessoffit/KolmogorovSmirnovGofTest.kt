package goodnessoffit

/**
 * Stores results of a Kolmogorov-Smirnov test.
 */
public data class KolmogorovSmirnovGofTest(
    override val testScore: Double,
    override val pValue: Double,
    override val universalScore: Double,
    override val warnings: Set<KSWarning>
) : AbstractGofTest(), PValueIfc

/**
 * Warnings specific to the Kolmogorov-Smirnov test
 */
public enum class KSWarning: WarningIfc {
    /**
     * Warning for tied values being present in the input data
     */
    TIED_VALUES {
        override val message: String
            get() = "Tied values should not be present in the Kolmogorov-Smirnov Test"
    }
}