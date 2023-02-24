package goodnessoffit


public sealed interface GofRequest <T: AbstractGofTest>
public sealed interface ContinuousRequest <T: AbstractGofTest> : GofRequest<T>
public sealed interface DiscreteRequest <T: AbstractGofTest> : GofRequest<T>

public class ChiSquareRequest(
    public val breakPoints: DoubleArray? = null
): ContinuousRequest<ChiSquareGofTest>, DiscreteRequest<ChiSquareGofTest>

public object KSRequest : ContinuousRequest<KolmogorovSmirnovGofTest>