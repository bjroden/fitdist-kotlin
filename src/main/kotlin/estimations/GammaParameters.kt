package estimations

import ksl.utilities.Interval
import org.apache.commons.math3.stat.StatUtils.sumLog
import ksl.utilities.math.FunctionIfc
import ksl.utilities.math.KSLMath
import ksl.utilities.rootfinding.BisectionRootFinder
import ksl.utilities.statistic.Statistic
import org.apache.commons.numbers.gamma.Digamma
import kotlin.math.ln

class GammaParameters : ParameterEstimatorIfc {
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        if (data.any { it < 0 }) { return estimateFailure("Data cannot be negative") }
        val solver = BisectionRootFinder(FuncToSolve(data), Interval(0.0000001, 100000.0), desiredPrec = KSLMath.machinePrecision)
        solver.evaluate()
        val alpha = solver.result
        val beta = Statistic(data).average / alpha
        return estimateSuccess(alpha, beta)
    }
}

private class FuncToSolve(data: DoubleArray) : FunctionIfc {
    val rhs = sumLog(data) / data.size
    val mean = Statistic(data).average

    override fun f(x: Double): Double = ln(mean / x) + Digamma.value(x) - rhs
}