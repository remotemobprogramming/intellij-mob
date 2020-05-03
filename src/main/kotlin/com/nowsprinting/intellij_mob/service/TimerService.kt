package com.nowsprinting.intellij_mob.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import java.time.LocalTime

class TimerService {
    private var startTime: LocalTime? = null
    private var expireTime: LocalTime? = null

    fun isRunning(): Boolean {
        return startTime != null
    }

    fun start(minutes: Int = 0) {
//        startTime = LocalTime.now()   // TODO: enable later
        if (minutes > 0) {
            expireTime = startTime?.plusMinutes(minutes.toLong())
        }
    }

    fun stop() {
        startTime = null
        expireTime = null
    }

    fun time(): String {
        return "" // TODO:
        // expire time was not set: elapsed time
        // not expired: remaining time
        // expired: over time (red color)
    }

    companion object {
        fun getInstance(project: Project): TimerService? {
            return ServiceManager.getService(project, TimerService::class.java)
        }
    }
}