package goodnessoffit

import ksl.utilities.statistic.Histogram


public sealed interface GofRequest <T: AbstractGofTest>
public sealed interface ContinuousRequest <T: AbstractGofTest> : GofRequest<T>
public sealed interface DiscreteRequest <T: AbstractGofTest> : GofRequest<T>

public class ChiSquareRequest(
    public val breakPoints: DoubleArray
): ContinuousRequest<ChiSquareGofTest>, DiscreteRequest<ChiSquareGofTest> {
    public companion object {
        public fun automaticBinsContinuous(data: DoubleArray): DoubleArray =
            Histogram.recommendBreakPoints(data)

        public fun automaticBinsDiscrete(data: DoubleArray): DoubleArray =
            (0..data.max().toInt())
                .map { it.toDouble() }
                .toDoubleArray()
    }
}


public object KSRequest : ContinuousRequest<KolmogorovSmirnovGofTest>