package goodnessoffit

public data class KolmogorovSmirnovGofTest(
    override val testScore: Double,
    override val pValue: Double,
    override val universalScore: Double,
    override val warnings: Set<KSWarning>
) : AbstractGofTest(), PValueIfc

public enum class KSWarning: WarningIfc {
    TIED_VALUES {
        override val message: String
            get() = "Tied values should not be present in the Kolmogorov-Smirnov Test"
    }
}