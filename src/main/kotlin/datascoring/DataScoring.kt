package datascoring

import goodnessoffit.AbstractGofTest

public object DataScoring {
    public fun scoreTests(vararg tests: Pair<AbstractGofTest, Double>): Double {
        return tests.sumOf { (test, weight) -> test.universalScore * weight }
    }
}
