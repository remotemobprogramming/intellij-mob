package com.nowsprinting.intellij_mob.subcommand

import com.intellij.openapi.project.Project
import com.nowsprinting.intellij_mob.service.NotificationService
import java.awt.AWTException
import java.awt.Robot
import java.awt.event.KeyEvent

/**
 * Start screenshare with Zoom (macOS or Linux with xdotool, requires configuration in zoom to work)
 * It only works if you activate make the screenshare hotkey in zoom globally available, and keep the default shortcut at CMD+SHIFT+S (macOS)/ ALT+S (Linux).
 * And if run on macOS Catalina (or later?), Got to Security & Privacy > Privacy tab > Accessibility > Add `IntelliJ IDEA.app`
 */
fun startScreenShareWithZoom(project: Project) {
    val keys = keysByOS()
    if (keys.isEmpty()) {
        val service = NotificationService.getInstance(project)
        service?.warning("Start screenshare with Zoom option is not supported your OS")
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
        e.printStackTrace()
    }
}

private fun keysByOS(): IntArray {
    val os = System.getProperty("os.name").toLowerCase()
    if (os.startsWith("mac")) {
        return intArrayOf(
            KeyEvent.VK_SHIFT,
            KeyEvent.VK_META,  // command
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