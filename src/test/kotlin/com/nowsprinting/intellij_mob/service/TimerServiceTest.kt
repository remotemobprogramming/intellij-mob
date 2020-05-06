/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.Month

internal class TimerServiceTest {
    private val startTime = LocalDateTime.of(2020, Month.DECEMBER, 31, 23, 55, 30)
    private val verifyTime = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 1, 10)

    @Test
    fun getTime_notRunning_fixedText() {
        val sut = TimerService()
        assertFalse(sut.isRunning(), "not running")
        assertEquals("Mob", sut.getTime())
    }

    @Test
    fun getTime_stopped_fixedText() {
        val sut = TimerService()
        sut.start()
        sut.stop()
        assertFalse(sut.isRunning(), "not running")
        assertEquals("Mob", sut.getTime())
    }

    @Test
    fun getTime_remainingTime_timeText() {
        val sut = TimerService()
        sut.start(10, startTime)
        assertEquals(TimerState.REMAINING_TIME, sut.getState(verifyTime))
        assertEquals("04:20", sut.getTime(verifyTime))
    }

    @Test
    fun getTime_overTime_timeText() {
        val sut = TimerService()
        sut.start(5, startTime)
        assertEquals(TimerState.OVER_TIME, sut.getState(verifyTime))
        assertEquals("00:40", sut.getTime(verifyTime))
    }

    @Test
    fun getTime_elapsedTime_timeText() {
        val sut = TimerService()
        sut.start(0, startTime)
        assertEquals(TimerState.ELAPSED_TIME, sut.getState(verifyTime))
        assertEquals("05:40", sut.getTime(verifyTime))
    }

    @Test
    fun getTime_sameTime_timeTextZero() {
        val sameTime = startTime
        val sut = TimerService()
        sut.start(0, sameTime)
        assertEquals("00:00", sut.getTime(sameTime))
    }

    @Test
    fun getTime_longerTime_timeText() {
        val longerTime = verifyTime.plusHours(2)
        val sut = TimerService()
        sut.start(0, startTime)
        assertEquals("125:40", sut.getTime(longerTime))
    }
    // other idea, e.g. "77 minutes passed", "8 hours passed", "500 days passed"
}