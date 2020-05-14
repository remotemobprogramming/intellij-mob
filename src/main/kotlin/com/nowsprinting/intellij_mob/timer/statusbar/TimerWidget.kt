/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.timer.statusbar

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget.MultipleTextValuesPresentation
import com.intellij.openapi.wm.StatusBarWidget.WidgetPresentation
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import com.intellij.util.Consumer
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.timer.TimerListener
import com.nowsprinting.intellij_mob.timer.TimerService
import com.nowsprinting.intellij_mob.timer.TimerState
import org.jetbrains.annotations.CalledInAwt
import java.awt.event.MouseEvent
import javax.swing.Icon

class TimerWidget(project: Project) : EditorBasedWidget(project), MultipleTextValuesPresentation, TimerListener {
    private val logger = Logger.getInstance(javaClass)
    private lateinit var timer: TimerService

    init {
        TimerService.getInstance(project)?.let {
            timer = it
            timer.addListener(this)
            logger.debug("got a timer service")
        }
        logger.debug("init mob timer widget completed")
    }

    override fun ID(): String {
        return ID
    }

    override fun install(statusBar: StatusBar) {
        super.install(statusBar)
        update()
        logger.debug("install mob timer widget completed")
    }

    override fun dispose() {
        super.dispose()
        timer.removeListener(this)
        logger.debug("dispose mob timer widget completed")
    }

    override fun getPresentation(): WidgetPresentation? {
        return this
    }

    override fun getSelectedValue(): String? {
        return timer.getTime()
    }

    override fun getTooltipText(): String? {
        val state = when (timer.getState()) {
            TimerState.NOT_RUNNING -> MobBundle.message("mob.timer_widget.state.not_running")
            TimerState.REMAINING_TIME -> MobBundle.message("mob.timer_widget.state.remaining_time")
            TimerState.OVER_TIME -> MobBundle.message("mob.timer_widget.state.over_time")
            TimerState.ELAPSED_TIME -> MobBundle.message("mob.timer_widget.state.elapsed_time")
        }
        return "${MobBundle.message("mob.timer_widget.name")}: $state"
    }

    override fun getIcon(): Icon? {
        return null
    }

    override fun getPopupStep(): ListPopup? {
        if (project.isDisposed) return null

        // TODO: implements later: timer restart, suspend, resume actions. maybe
        return null
    }

    override fun getClickConsumer(): Consumer<MouseEvent>? {
        // has no effect since the click opens a list popup, and the consumer is not called for the MultipleTextValuesPresentation
        return null
    }

    override fun notifyUpdate() {
        update()
    }

    @CalledInAwt
    private fun update() {
        if (project.isDisposed) return
        myStatusBar.updateWidget(ID())
    }

    companion object {
        internal const val ID = "com.nowsprinting.intellij_mob.TimerWidget"
    }
}