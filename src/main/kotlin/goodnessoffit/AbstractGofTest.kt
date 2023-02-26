package goodnessoffit

public abstract class AbstractGofTest {
    public abstract val testScore: Double
    public abstract val warnings: Collection<WarningIfc>
}