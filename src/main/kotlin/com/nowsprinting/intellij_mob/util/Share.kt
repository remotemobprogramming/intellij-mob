package com.nowsprinting.intellij_mob.util

import com.intellij.openapi.diagnostic.Logger
import com.nowsprinting.intellij_mob.MobBundle
import java.awt.AWTException
import java.awt.Robot
import java.awt.event.KeyEvent

private val logger = Logger.getInstance("#com.nowsprinting.intellij_mob.util.ShareKt")

/**
 * Start screenshare with Zoom (macOS or Linux with xdotool, requires configuration in zoom to work)
 * It only works if you activate make the screenshare hotkey in zoom globally available, and keep the default shortcut at CMD+SHIFT+S (macOS)/ ALT+S (Linux).
 * And if run on macOS Catalina (or later?), Got to Security & Privacy > Privacy tab > Accessibility > Add `IntelliJ IDEA.app`
 *
 * @return message for notification content
 */
fun screenShareWithZoom(): Pair<Boolean, String> {
    val keys = keysByOS()
    if (keys.isEmpty()) {
        val message = MobBundle.message("mob.screenshare.share_not_supported_os")
        logger.warn(message)
        return Pair(false, message)
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
        e.printStackTrace()
        logger.error(e)
        return Pair(false, MobBundle.message("mob.screenshare.share_failure"))
    }
    return Pair(true, MobBundle.message("mob.screenshare.share_successful"))
}

private fun keysByOS(): IntArray {
    val os = System.getProperty("os.name").toLowerCase()
    if (os.startsWith("mac")) {
        return intArrayOf(
            KeyEvent.VK_SHIFT,
            KeyEvent.VK_META,  // command key
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