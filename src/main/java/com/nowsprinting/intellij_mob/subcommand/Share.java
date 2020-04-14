package com.nowsprinting.intellij_mob.subcommand;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.nowsprinting.intellij_mob.service.NotificationService;
import com.sun.istack.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Share {

    /**
     * Start screenshare with Zoom (macOS or Linux with xdotool, requires configuration in zoom to work)
     * It only works if you activate make the screenshare hotkey in zoom globally available, and keep the default shortcut at CMD+SHIFT+S (macOS)/ ALT+S (Linux).
     * And if run on macOS Catalina (or later?), Got to Security & Privacy > Privacy tab > Accessibility > Add `IntelliJ IDEA.app`
     */
    public static void startScreenShareWithZoom(@NotNull Project project) {
        int[] keys = keysByOS();
        if (keys.length == 0) {
            NotificationService service = NotificationService.getInstance(project);
            service.warning("Start screenshare with Zoom option is not supported your OS");
            return;
        }
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(200);
            for (int key : keys) {
                robot.keyPress(key);
            }
            for (int i = keys.length - 1; i >= 0; i--) {
                robot.keyRelease(keys[i]);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static int[] keysByOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("mac")) {
            return new int[]{
                    KeyEvent.VK_SHIFT,
                    KeyEvent.VK_META,   // command
                    KeyEvent.VK_S
            };
        } else if (os.startsWith("linux")) {    // TODO: not test yet on Linux
            return new int[]{
                    KeyEvent.VK_ALT,
                    KeyEvent.VK_S
            };
        }
        return new int[]{};
    }
}
