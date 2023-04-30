package goodnessoffit

/**
 * Class containing results for a goodness of fit test.
 */
public abstract class AbstractGofTest {
    /**
     * The test statistic.
     */
    public abstract val testScore: Double

    /**
     * Universal score used for ranking different types of tests on a common scale.
     * Scale is on the interval `[0, 1`], with 1 being the highest score and 0 being the lowest.
     */
    public abstract val universalScore: Double

    /**
     * Collection of warnings associated with the test.
     */
    public abstract val warnings: Collection<WarningIfc>
}