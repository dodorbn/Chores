package fr.eseo.ld.e5e.android.robinedo.chores.domain.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TimeLogTest {

    @Test
    @DisplayName("test duration calculation between start and end times")
    fun testDurationCalculation() {
        val startTime = LocalDateTime.of(2026,2,13,9,45,0)
        val endTime = LocalDateTime.of(2026,2,13,10,10,17)
        val timeLog = TimeLog(
            startTime = startTime,
            endTime = endTime
        )
        val expectedDuration = 1517
        val actualDuration = timeLog.duration
        Assertions.assertEquals(expectedDuration, actualDuration, "Duration should be $expectedDuration seconds")
    }

    @Test
    @DisplayName("test duration by default is zero")
    fun testDurationDefaultCalculation() {
        val timeLog = TimeLog( )
        val expectedDuration = 0
        val actualDuration = timeLog.duration
        Assertions.assertEquals(expectedDuration, actualDuration, "Duration should be $expectedDuration seconds")

    }

    @Test
    @DisplayName("test duration is zero when both start and end time are equal")
    fun testDurationZeroCalculation() {
        val nowTime = LocalDateTime.now()
        val timeLog = TimeLog(
            startTime = nowTime,
            endTime = nowTime
        )
        val expectedDuration = 0
        val actualDuration = timeLog.duration
        Assertions.assertEquals(expectedDuration, actualDuration, "Duration should be $expectedDuration seconds")
    }
}
