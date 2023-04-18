package goodnessoffit

public data class ChiSquareGofTest(
    override val testScore: Double,
    override val pValue: Double,
    override val universalScore: Double,
    override val warnings: Set<ChiSquareWarning>,
    public val degreesOfFreedom: Int,
    public val intervals: Int
) : AbstractGofTest(), PValueIfc

public enum class ChiSquareWarning : WarningIfc {
    SMALL_EXPECTED_COUNTS {
        override val message: String
            get() = ">20% of intervals had less than 5 expected values"
    }
}