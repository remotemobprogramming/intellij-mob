/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.nowsprinting.intellij_mob.config.ui;

import com.nowsprinting.intellij_mob.config.MobProjectSettings;

import javax.swing.*;

public class MobSettingsForm {
    JPanel panel1;
    JTextField wipBranch;
    JTextField baseBranch;
    JTextField remoteName;
    JTextField timerMinutes;
    JCheckBox timerSound;
    JCheckBox startWithShare;
    JTextField wipCommitMessage;
    JCheckBox nextStay;

    public JComponent getPanel() {
        return panel1;
    }

    public boolean isModified(MobProjectSettings settings) {
        if (!wipBranch.getText().equals(settings.wipBranch)) return true;
        if (!baseBranch.getText().equals(settings.baseBranch)) return true;
        if (!remoteName.getText().equals(settings.remoteName)) return true;
        if (!timerMinutes.getText().equals(timerMinutesIfZeroReturnEmpty(settings))) return true;
        if (!timerSound.isSelected() == settings.timerSound) return true;
        if (!startWithShare.isSelected() == settings.startWithShare) return true;
        if (!wipCommitMessage.getText().equals(settings.wipCommitMessage)) return true;
        if (!nextStay.isSelected() == settings.nextStay) return true;
        return false;
    }

    public void applyEditorTo(MobProjectSettings settings) {
        settings.wipBranch = wipBranch.getText();
        settings.baseBranch = baseBranch.getText();
        settings.remoteName = remoteName.getText();
        try {
            settings.timerMinutes = Integer.parseInt(timerMinutes.getText());
        } catch (NumberFormatException e) {
            settings.timerMinutes = 0;
        }
        settings.timerSound = timerSound.isSelected();
        settings.startWithShare = startWithShare.isSelected();
        settings.wipCommitMessage = wipCommitMessage.getText();
        settings.nextStay = nextStay.isSelected();
    }

    public void resetEditorFrom(MobProjectSettings settings) {
        wipBranch.setText(settings.wipBranch);
        baseBranch.setText(settings.baseBranch);
        remoteName.setText(settings.remoteName);
        timerMinutes.setText(timerMinutesIfZeroReturnEmpty(settings));
        timerSound.setSelected(settings.timerSound);
        startWithShare.setSelected(settings.startWithShare);
        wipCommitMessage.setText(settings.wipCommitMessage);
        nextStay.setSelected(settings.nextStay);
    }

    private String timerMinutesIfZeroReturnEmpty(MobProjectSettings settings) {
        if (settings.timerMinutes > 0) {
            return Integer.toString(settings.timerMinutes);
        } else {
            return "";
        }
    }
}