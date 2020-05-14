/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.timer.statusbar

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.timer.TimerService
import org.jetbrains.annotations.Nls

class TimerWidgetFactory : StatusBarWidgetFactory {
    private val logger = Logger.getInstance(javaClass)
    private lateinit var myProject: Project

    override fun getId(): String {
        return TimerWidget.ID
    }

    @Nls
    override fun getDisplayName(): String {
        return MobBundle.message("mob.timer_widget.name")
    }

    override fun isAvailable(project: Project): Boolean {
        return true
    }

    override fun createWidget(project: Project): TimerWidget {
        logger.info("create mob timer widget")
        myProject = project
        return TimerWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        logger.info("dispose mob timer widget")
        Disposer.dispose(widget)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        return true
    }
}