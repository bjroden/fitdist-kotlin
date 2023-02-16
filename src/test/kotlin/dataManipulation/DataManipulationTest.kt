package dataManipulation

import org.junit.jupiter.api.Test
import Data

class DataManipulationTest {

    @Test
    fun shiftGroundBeef() {
        val groundBeefOffset = 10.0
        val shiftedData = DataManipulation().shiftData(Data.groundBeef)
        assert(shiftedData.offset == groundBeefOffset) {"offset should have been ${groundBeefOffset}, was ${shiftedData.offset}"}
    }
}