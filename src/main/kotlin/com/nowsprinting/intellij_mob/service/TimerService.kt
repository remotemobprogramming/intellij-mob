package com.nowsprinting.intellij_mob.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import java.util.*

class TimerService {
    private val startTime: Date? = null
    private val expireTime: Date? = null

    fun isRunning(): Boolean {
        return false // TODO:
    }

    companion object {
        fun getInstance(project: Project): TimerService? {
            return ServiceManager.getService(
                project,
                TimerService::class.java
            )
        }
    }
}