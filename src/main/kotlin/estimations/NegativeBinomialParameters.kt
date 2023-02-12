package estimations

import ksl.utilities.math.KSLMath
import ksl.utilities.statistic.Statistic
import ksl.utilities.toDoubles
import org.apache.commons.math3.analysis.UnivariateFunction
import org.apache.commons.math3.optim.MaxEval
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType
import org.apache.commons.math3.optim.univariate.BrentOptimizer
import org.apache.commons.math3.optim.univariate.SearchInterval
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction
import kotlin.math.floor
import kotlin.math.ln

public class NegativeBinomialParameters(public var maxEvaluations: Int = 1000) : ParameterEstimatorIfc {
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        if (data.any { it < 0 }) { return estimateFailure("Data must be non-negative") }
        if (data.any { floor(it) != it }) { return estimateFailure("Data must be integers") }

        val intData = data.map { it.toInt() }.toIntArray()
        val mean = Statistic(data).average
        val optimizer = BrentOptimizer(
            KSLMath.defaultNumericalPrecision,
            KSLMath.machinePrecision
        )
        val s = optimizer.optimize(
            GoalType.MAXIMIZE,
            UnivariateObjectiveFunction(FuncToOptimize(intData)),
            SearchInterval(0.0, Int.MAX_VALUE.toDouble()),
            MaxEval(maxEvaluations)
        ).point
        val p = s / (mean + s)
        return estimateSuccess(p, s)
    }

    private class FuncToOptimize(data: IntArray) : UnivariateFunction {
        val M = data.max()
        val n = data.size
        val mean = Statistic(data.toDoubles()).average
        val fkBuckets = getFkBuckets(data)

        override fun value(x: Double): Double {
            val s = x
            val p = s / (mean + s)
            val p1 = (1..M).sumOf { k -> fkBuckets[k - 1] * ln(s + k - 1) }
            val p2 = n * s * ln(p)
            val p3 = n * mean * ln(1 - p)
            return p1 + p2 + p3
        }

        private fun getFkBuckets(data: IntArray): IntArray {
            val buckets = IntArray(M + 1)
            for (i in data) { buckets[i] += 1 }
            for (i in 1..M) { buckets[i] = buckets[i] + buckets[i-1] }
            for (i in 0..M) { buckets[i] = n - buckets[i] }
            return buckets
        }
    }
}