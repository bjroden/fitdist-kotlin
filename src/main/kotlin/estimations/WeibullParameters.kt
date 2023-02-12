package estimations

import kotlin.math.ln
import kotlin.math.pow

public class WeibullParameters : ParameterEstimatorIfc {
    //TODO: replace sumOf with more computationally stable method (and maybe pow if needed)
    override fun estimate(data: DoubleArray): Result<DoubleArray> {
        if (data.any { it <= 0 }) { return estimateFailure("Data must be positive") }
        var alpha = getAlpha0(data)
        for (i in 1..4) {
            alpha = getAlphaK(data, alpha)
        }
        val beta = (data.sumOf { it.pow(alpha) } / data.size ).pow(1.0 / alpha)

        return estimateSuccess(alpha, beta)
    }

    private fun getAlpha0(data: DoubleArray): Double {
        val n = data.size.toDouble()
        val coefficient = 6.0 / (Math.PI.pow(2))
        val sums = data.sumOf { ln(it).pow(2) } - (data.sumOf { ln(it) }.pow(2) / n)
        return ((coefficient * sums) / (n-1)).pow(-0.5)
    }

    private fun getAlphaK(data: DoubleArray, previousAlpha: Double): Double {
        val A = data.sumOf { ln(it) } / data.size
        val B = data.sumOf { it.pow(previousAlpha) }
        val C = data.sumOf { it.pow(previousAlpha) * ln(it) }
        val H = data.sumOf { it.pow(previousAlpha) * ln(it).pow(2) }
        val numerator = A + (1/previousAlpha) - (C/B)
        val denominator = (1/(previousAlpha.pow(2))) + ( ((B*H) - C.pow(2)) / B.pow(2) )
        return previousAlpha + (numerator / denominator)
    }

}