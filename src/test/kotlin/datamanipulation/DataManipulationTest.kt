package datamanipulation

import org.junit.jupiter.api.Test
import Data
import ksl.utilities.math.KSLMath

class DataManipulationTest {

    @Test
    fun shiftGroundBeef() {
        val groundBeefOffset = 10.0
        val shiftedData = DataManipulation.shiftData(Data.groundBeef)
        assert(KSLMath.equal(groundBeefOffset, shiftedData.offset))
            { "Offset should have been ${groundBeefOffset}, was ${shiftedData.offset}" }
        val unShiftedData = DataManipulation.shiftData(shiftedData.data, zeroPoint = shiftedData.offset)
        assert(KSLMath.equal(-groundBeefOffset, unShiftedData.offset))
            { "Offset should have been ${-groundBeefOffset}, was ${unShiftedData.offset}" }
    }
}