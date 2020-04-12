package com.nowsprinting.intellij_mob.action.start;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nowsprinting.intellij_mob.MobBundle;
import com.nowsprinting.intellij_mob.MobTimer;
import com.nowsprinting.intellij_mob.action.start.ui.StartDialog;
import com.nowsprinting.intellij_mob.config.MobProjectSettings;
import org.jetbrains.annotations.NotNull;

public class StartAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        MobTimer timer = MobTimer.getInstance(e.getProject());
        e.getPresentation().setEnabled(!timer.isRunning());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        MobProjectSettings settings = MobProjectSettings.getInstance(e.getProject());
        StartDialog dialog = new StartDialog();
        dialog.setTimer(settings.timer);
        dialog.setStartWithShare(settings.startWithShare);
        dialog.setNextAtExpire(settings.nextAtExpire);
        dialog.setTitle(MobBundle.message("mob.start.dialog.title"));
        dialog.pack();
        dialog.setLocationRelativeTo(null); // screen center
        dialog.setVisible(true);
    }
}