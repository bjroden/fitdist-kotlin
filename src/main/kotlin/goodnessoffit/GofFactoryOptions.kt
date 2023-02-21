package goodnessoffit


public sealed interface GofRequest <T: AbstractGofTest>
public sealed interface ContinuousRequest <T: AbstractGofTest> : GofRequest<T>
public sealed interface DiscreteRequest <T: AbstractGofTest> : GofRequest<T>

public data class ChiSquareRequest(
    // TODO: More granular interval definition
    public val intervals: Int? = null
): ContinuousRequest<ChiSquareGofTest>, DiscreteRequest<ChiSquareGofTest>

public object KSRequest : ContinuousRequest<KolmogorovSmirnovGofTest>