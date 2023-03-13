package plotting

import ksl.utilities.distributions.CDFIfc
import ksl.utilities.distributions.InverseCDFIfc

public object Plotting {
    public fun observedDataToProbabilities(data: DoubleArray, dist: CDFIfc): DoubleArray {
        return data.sortedArray().map { dist.cdf(it) }.toDoubleArray()
    }

    public fun generateExpectedProbabilities(n: Int): DoubleArray {
        return (1..n).map { (it - 0.5) / n }.toDoubleArray()
    }

    public fun generateExpectedData(n: Int, dist: InverseCDFIfc): DoubleArray {
        return (1..n).map { dist.invCDF((it - 0.5) / n) }.toDoubleArray()
    }
}