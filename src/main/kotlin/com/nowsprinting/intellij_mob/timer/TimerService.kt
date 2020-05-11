/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.timer

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.action.done.DoneNotificationAction
import com.nowsprinting.intellij_mob.action.next.NextNotificationAction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

enum class TimerState {
    NOT_RUNNING,
    REMAINING_TIME,
    OVER_TIME,
    ELAPSED_TIME
}

class TimerService {
    private val logger = Logger.getInstance(javaClass)
    private var startTime: LocalDateTime? = null
    private var expireTime: LocalDateTime? = null
    private var notified = false
    private var timerListeners = mutableSetOf<TimerListener>()

    init {
        timerCoroutine()
    }

    fun addListener(listener: TimerListener) {
        timerListeners.add(listener)
    }

    fun removeListener(listener: TimerListener) {
        timerListeners.remove(listener)
    }

    private fun notifyUpdate() {
        timerListeners.forEach {
            it.timerUpdate()
        }
    }

    fun start(minutes: Int = 0, now: LocalDateTime = LocalDateTime.now()) {
        startTime = now
        if (minutes > 0) {
            expireTime = startTime?.plusMinutes(minutes.toLong())
        }
        notifyUpdate()
        logger.info("mob timer started")
    }

    fun stop() {
        startTime = null
        expireTime = null
        notified = false
        notifyUpdate()
        logger.info("mob timer stopped")
    }

    fun isRunning(): Boolean {
        return startTime != null
    }

    fun getState(now: LocalDateTime = LocalDateTime.now()): TimerState {
        if (!isRunning()) {
            return TimerState.NOT_RUNNING
        }
        expireTime?.let {
            if (it.isAfter(now)) {
                return TimerState.REMAINING_TIME
            } else {
                return TimerState.OVER_TIME
            }
        }
        return TimerState.ELAPSED_TIME
    }

    fun getTime(now: LocalDateTime = LocalDateTime.now()): String {
        return when (getState(now)) {
            TimerState.NOT_RUNNING -> MobBundle.message("mob.timer.not_running_text")
            TimerState.REMAINING_TIME -> textFromSec(ChronoUnit.SECONDS.between(now, expireTime))
            TimerState.OVER_TIME -> textFromSec(ChronoUnit.SECONDS.between(expireTime, now))
            TimerState.ELAPSED_TIME -> textFromSec(ChronoUnit.SECONDS.between(startTime, now))
        }
    }

    private fun textFromSec(sec: Long): String {
        val m = sec / 60
        val s = sec - m * 60
        return String.format("%02d:%02d", m, s)
    }

    private fun isExpired(now: LocalDateTime = LocalDateTime.now()): Boolean {
        return getState(now).equals(TimerState.OVER_TIME)
    }

    private fun isNeedNotify(now: LocalDateTime = LocalDateTime.now()): Boolean {
        return !notified && isExpired(now)
    }

    private fun notifyExpire() {
        val stickyGroup = NotificationGroup("Mob Timer", NotificationDisplayType.STICKY_BALLOON, false)
        val notification = stickyGroup.createNotification(
            MobBundle.message("mob.timer.expired.title"),
            NotificationType.INFORMATION
        )
        notification.addAction(NextNotificationAction())
        notification.addAction(DoneNotificationAction())
        notification.notify(null)
    }

    private fun timerCoroutine() = GlobalScope.launch {
        while (true) {
            if (isRunning()) {
                notifyUpdate()
            }
            if (isNeedNotify(LocalDateTime.now())) {
                notifyExpire()
                notified = true
                logger.info("mob timer expired")
            }
            delay(1000L)
        }
    } // may it leak???

    companion object {
        fun getInstance(project: Project): TimerService? {
            return ServiceManager.getService(project, TimerService::class.java)
        }
    }
}