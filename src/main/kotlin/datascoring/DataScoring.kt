package datascoring

import goodnessoffit.AbstractGofTest

/**
 * Contains functions for scoring goodness of fit tests
 */
public object DataScoring {
    /**
     * Compute a weighted average on goodness of fit tests
     * @param [tests] Pairs in the form (test, weight). Weights should be in the interval `[0, 1`].
     * @return The weighted average based on the test's universal scores
     */
    public fun scoreTests(vararg tests: Pair<AbstractGofTest, Double>): Double {
        return tests.sumOf { (test, weight) -> test.universalScore * weight }
    }
}
