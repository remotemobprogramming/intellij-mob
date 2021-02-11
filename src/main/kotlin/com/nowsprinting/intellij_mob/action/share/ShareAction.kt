/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.action.share

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.nowsprinting.intellij_mob.MobBundle
import com.nowsprinting.intellij_mob.util.notifyError
import com.nowsprinting.intellij_mob.util.notifyWarning
import java.awt.AWTException
import java.awt.Robot
import java.awt.event.KeyEvent

/**
 * Start screenshare with Zoom (requires configuration in zoom to work)
 * It only works if you activate make the screenshare hotkey in zoom globally available, and keep the default shortcut at CMD+SHIFT+S (macOS)/ ALT+S (Windows, Linux).
 * And if run on macOS Catalina (or later?), Got to Security & Privacy > Privacy tab > Accessibility > Add `IntelliJ IDEA.app`
 */
class ShareAction : AnAction() {
    private val logger = Logger.getInstance(javaClass)

    override fun actionPerformed(e: AnActionEvent) {
        val keys = keysByOS()
        if (keys.isEmpty()) {
            val message = MobBundle.message("mob.screenshare.share_not_supported_os")
            logger.warn(message)
            notifyWarning(message)
            return
        }
        try {
            val robot = Robot()
            robot.autoDelay = 200
            for (key in keys) {
                robot.keyPress(key)
            }
            for (i in keys.indices.reversed()) {
                robot.keyRelease(keys[i])
            }
        } catch (e: AWTException) {
            val message = MobBundle.message("mob.screenshare.share_failure")
            logger.error(message, e)
            notifyError(message)
            return
        }
        val message = MobBundle.message("mob.screenshare.share_successful")
        logger.info(message)
    }

    private fun keysByOS(): IntArray {
        val os = System.getProperty("os.name").toLowerCase()
        if (os.startsWith("mac")) {
            return intArrayOf(
                KeyEvent.VK_SHIFT,
                KeyEvent.VK_META,  // command key
                KeyEvent.VK_S
            )
        } else if (os.startsWith("windows")) {
            return intArrayOf(
                KeyEvent.VK_ALT,
                KeyEvent.VK_S
            )
        } else if (os.startsWith("linux")) {    // TODO: not test yet on Linux
            return intArrayOf(
                KeyEvent.VK_ALT,
                KeyEvent.VK_S
            )
        }
        return intArrayOf()
    }
}