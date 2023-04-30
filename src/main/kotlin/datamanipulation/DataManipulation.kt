package datamanipulation

import ksl.utilities.subtractConstant
import kotlin.math.floor

/**
 * Functions for manipulating input data
 */
public object DataManipulation {
    /**
     * Shift all data points so that the desired minimum is reached.
     * @param [data] Input data
     * @param [zeroPoint] The desired minimum value. Default is 0.0
     * @return The results of the operation
     */
    public fun shiftData(data: DoubleArray, zeroPoint: Double = 0.0) : ShiftDataResult {
        val dataCopy = data.copyOf()
        val offset = floor(data.min()) - floor(zeroPoint)
        val shiftedData = dataCopy.subtractConstant(offset)
        return ShiftDataResult(shiftedData, offset)
    }
}

/**
 * Results of a data shift operation.
 */
public data class ShiftDataResult(
    /**
     * The new data array
     */
    val data: DoubleArray,

    /**
     * The number that was subtracted from all values of the original data array
     */
    val offset: Double
)