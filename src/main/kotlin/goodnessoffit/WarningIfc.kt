package goodnessoffit

/**
 * Warning that indicates a goodness of fit test was successful,
 * but an assumption of the test may have been violated.
 * Inheritors can use an enum class to define all possible warnings for a test,
 * so that a particular warning's presence can be programmatically checked for.
 */
public interface WarningIfc {
    /**
     * Message associated with this warning.
     */
    public val message: String
}