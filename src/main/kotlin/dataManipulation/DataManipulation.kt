package dataManipulation

import ksl.utilities.subtractConstant
import kotlin.math.floor

public class DataManipulation {
    public fun shiftData(data: DoubleArray, zeroPoint: Double = 0.0) : ShiftDataResult {
        val dataCopy = data.copyOf()
        val offset = (floor(data.min()) - floor(zeroPoint))
        val shiftedData = dataCopy.subtractConstant(offset)
        return ShiftDataResult(shiftedData, offset)
    }
}

public data class ShiftDataResult(val data: DoubleArray, val offset: Double)