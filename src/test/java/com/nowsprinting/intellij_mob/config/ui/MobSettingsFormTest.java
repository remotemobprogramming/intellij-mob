package com.nowsprinting.intellij_mob.config.ui;

import com.nowsprinting.intellij_mob.config.MobProjectSettings;
import org.junit.Test;

import static org.junit.Assert.*;

public class MobSettingsFormTest {

    @Test
    public void resetEditorFrom_set_wipBranch() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.wipBranch = "mob-session-2";

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertEquals(sut.wipBranch.getText(), "mob-session-2");
    }

    @Test
    public void resetEditorFrom_set_baseBranch() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.baseBranch = "develop";

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertEquals(sut.baseBranch.getText(), "develop");
    }

    @Test
    public void resetEditorFrom_set_remoteName() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.remoteName = "upstream";

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertEquals(sut.remoteName.getText(), "upstream");
    }

    @Test
    public void resetEditorFrom_set_debug() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.debug = true;

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertTrue(sut.debug.isSelected());
    }

    @Test
    public void resetEditorFrom_set_timer() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.timer = 100;

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertEquals(sut.timer.getText(), "100");
    }

    @Test
    public void resetEditorFrom_set_startWithShare() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.startWithShare = true;

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertTrue(sut.startWithShare.isSelected());
    }

    @Test
    public void resetEditorFrom_set_nextAtExpire() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.nextAtExpire = true;

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertTrue(sut.nextAtExpire.isSelected());
    }

    @Test
    public void resetEditorFrom_set_wipCommitMessage() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.wipCommitMessage = "fix";

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertEquals(sut.wipCommitMessage.getText(), "fix");
    }

    @Test
    public void resetEditorFrom_set_nextStay() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.nextStay = true;

        MobSettingsForm sut = new MobSettingsForm();
        sut.resetEditorFrom(settings);
        assertTrue(sut.nextStay.isSelected());
    }

    private MobProjectSettings createDefaultSettings() {
        MobProjectSettings settings = new MobProjectSettings();
        settings.wipBranch = "mob-session";
        settings.baseBranch = "master";
        settings.remoteName = "origin";
        settings.debug = false;
        settings.timer = 10;
        settings.startWithShare = false;
        settings.nextAtExpire = false;
        settings.wipCommitMessage = "mob next [ci-skip]";
        settings.nextStay = false;
        return settings;
    }

    private MobSettingsForm createDefaultForm() {
        MobSettingsForm form = new MobSettingsForm();
        form.wipBranch.setText("mob-session");
        form.baseBranch.setText("master");
        form.remoteName.setText("origin");
        form.debug.setSelected(false);
        form.timer.setText("10");
        form.startWithShare.setSelected(false);
        form.nextAtExpire.setSelected(false);
        form.wipCommitMessage.setText("mob next [ci-skip]");
        form.nextStay.setSelected(false);
        return form;
    }

    @Test
    public void isModified_notModified_false() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        boolean actual = sut.isModified(settings);
        assertFalse(actual);
    }

    @Test
    public void isModified_modified_wipBranch() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.wipBranch.setText("mob-session-2");
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void isModified_modified_baseBranch() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.baseBranch.setText("develop");
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void isModified_modified_remoteName() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.remoteName.setText("upstream");
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void isModified_modified_debug() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.debug.setSelected(true);
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void isModified_modified_timer() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.timer.setText("100");
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void isModified_modified_startWithShare() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.startWithShare.setSelected(true);
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void isModified_modified_nextAtExpire() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.nextAtExpire.setSelected(true);
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void isModified_modified_wipCommitMessage() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.wipCommitMessage.setText("fix");
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void isModified_modified_nextStay() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.nextStay.setSelected(true);
        boolean actual = sut.isModified(settings);
        assertTrue(actual);
    }

    @Test
    public void applyEditorTo_modified_wipBranch() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.wipBranch.setText("mob-session-2");
        sut.applyEditorTo(settings);
        assertEquals("mob-session-2", settings.wipBranch);
    }

    @Test
    public void applyEditorTo_modified_baseBranch() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.baseBranch.setText("develop");
        sut.applyEditorTo(settings);
        assertEquals("develop", settings.baseBranch);
    }

    @Test
    public void applyEditorTo_modified_remoteName() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.remoteName.setText("upstream");
        sut.applyEditorTo(settings);
        assertEquals("upstream", settings.remoteName);
    }

    @Test
    public void applyEditorTo_modified_debug() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.debug.setSelected(true);
        sut.applyEditorTo(settings);
        assertTrue(settings.debug);
    }

    @Test
    public void applyEditorTo_modified_timer() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.timer.setText("100");
        sut.applyEditorTo(settings);
        assertEquals(100, settings.timer);
    }

    @Test
    public void applyEditorTo_modified_startWithShare() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.startWithShare.setSelected(true);
        sut.applyEditorTo(settings);
        assertTrue(settings.startWithShare);
    }

    @Test
    public void applyEditorTo_modified_nextAtExpire() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.nextAtExpire.setSelected(true);
        sut.applyEditorTo(settings);
        assertTrue(settings.nextAtExpire);
    }

    @Test
    public void applyEditorTo_modified_wipCommitMessage() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.wipCommitMessage.setText("fix");
        sut.applyEditorTo(settings);
        assertEquals("fix", settings.wipCommitMessage);
    }

    @Test
    public void applyEditorTo_modified_nextStay() {
        MobProjectSettings settings = createDefaultSettings();
        MobSettingsForm sut = createDefaultForm();
        sut.nextStay.setSelected(true);
        sut.applyEditorTo(settings);
        assertTrue(settings.nextStay);
    }
}
