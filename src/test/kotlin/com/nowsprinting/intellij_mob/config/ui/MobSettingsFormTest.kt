package com.nowsprinting.intellij_mob.config.ui

import com.nowsprinting.intellij_mob.config.MobProjectSettings
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MobSettingsFormTest {

    @Test
    fun resetEditorFrom_set_wipBranch() {
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session-2"
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertEquals(sut.wipBranch.text, "mob-session-2")
    }

    @Test
    fun resetEditorFrom_set_baseBranch() {
        val settings = MobProjectSettings()
        settings.baseBranch = "develop"
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertEquals(sut.baseBranch.text, "develop")
    }

    @Test
    fun resetEditorFrom_set_remoteName() {
        val settings = MobProjectSettings()
        settings.remoteName = "upstream"
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertEquals(sut.remoteName.text, "upstream")
    }

    @Test
    fun resetEditorFrom_set_debug() {
        val settings = MobProjectSettings()
        settings.debug = true
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertTrue(sut.debug.isSelected)
    }

    @Test
    fun resetEditorFrom_set_timer() {
        val settings = MobProjectSettings()
        settings.timer = 100
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertEquals(sut.timer.text, "100")
    }

    @Test
    fun resetEditorFrom_set_startWithShare() {
        val settings = MobProjectSettings()
        settings.startWithShare = true
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertTrue(sut.startWithShare.isSelected)
    }

    @Test
    fun resetEditorFrom_set_nextAtExpire() {
        val settings = MobProjectSettings()
        settings.nextAtExpire = true
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertTrue(sut.nextAtExpire.isSelected)
    }

    @Test
    fun resetEditorFrom_set_wipCommitMessage() {
        val settings = MobProjectSettings()
        settings.wipCommitMessage = "fix"
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertEquals(sut.wipCommitMessage.text, "fix")
    }

    @Test
    fun resetEditorFrom_set_nextStay() {
        val settings = MobProjectSettings()
        settings.nextStay = true
        val sut = MobSettingsForm()
        sut.resetEditorFrom(settings)
        Assertions.assertTrue(sut.nextStay.isSelected)
    }

    private fun createDefaultSettings(): MobProjectSettings {
        val settings = MobProjectSettings()
        settings.wipBranch = "mob-session"
        settings.baseBranch = "master"
        settings.remoteName = "origin"
        settings.debug = false
        settings.timer = 10
        settings.startWithShare = false
        settings.nextAtExpire = false
        settings.wipCommitMessage = "mob next [ci-skip]"
        settings.nextStay = false
        return settings
    }

    private fun createDefaultForm(): MobSettingsForm {
        val form = MobSettingsForm()
        form.wipBranch.text = "mob-session"
        form.baseBranch.text = "master"
        form.remoteName.text = "origin"
        form.debug.isSelected = false
        form.timer.text = "10"
        form.startWithShare.isSelected = false
        form.nextAtExpire.isSelected = false
        form.wipCommitMessage.text = "mob next [ci-skip]"
        form.nextStay.isSelected = false
        return form
    }

    @Test
    fun isModified_notModified_false() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        val actual = sut.isModified(settings)
        Assertions.assertFalse(actual)
    }

    @Test
    fun isModified_modified_wipBranch() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.wipBranch.text = "mob-session-2"
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun isModified_modified_baseBranch() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.baseBranch.text = "develop"
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun isModified_modified_remoteName() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.remoteName.text = "upstream"
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun isModified_modified_debug() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.debug.isSelected = true
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun isModified_modified_timer() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.timer.text = "100"
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun isModified_modified_startWithShare() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.startWithShare.isSelected = true
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun isModified_modified_nextAtExpire() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.nextAtExpire.isSelected = true
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun isModified_modified_wipCommitMessage() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.wipCommitMessage.text = "fix"
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun isModified_modified_nextStay() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.nextStay.isSelected = true
        val actual = sut.isModified(settings)
        Assertions.assertTrue(actual)
    }

    @Test
    fun applyEditorTo_modified_wipBranch() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.wipBranch.text = "mob-session-2"
        sut.applyEditorTo(settings)
        Assertions.assertEquals("mob-session-2", settings.wipBranch)
    }

    @Test
    fun applyEditorTo_modified_baseBranch() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.baseBranch.text = "develop"
        sut.applyEditorTo(settings)
        Assertions.assertEquals("develop", settings.baseBranch)
    }

    @Test
    fun applyEditorTo_modified_remoteName() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.remoteName.text = "upstream"
        sut.applyEditorTo(settings)
        Assertions.assertEquals("upstream", settings.remoteName)
    }

    @Test
    fun applyEditorTo_modified_debug() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.debug.isSelected = true
        sut.applyEditorTo(settings)
        Assertions.assertTrue(settings.debug)
    }

    @Test
    fun applyEditorTo_modified_timer() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.timer.text = "100"
        sut.applyEditorTo(settings)
        Assertions.assertEquals(100, settings.timer)
    }

    @Test
    fun applyEditorTo_modified_startWithShare() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.startWithShare.isSelected = true
        sut.applyEditorTo(settings)
        Assertions.assertTrue(settings.startWithShare)
    }

    @Test
    fun applyEditorTo_modified_nextAtExpire() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.nextAtExpire.isSelected = true
        sut.applyEditorTo(settings)
        Assertions.assertTrue(settings.nextAtExpire)
    }

    @Test
    fun applyEditorTo_modified_wipCommitMessage() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.wipCommitMessage.text = "fix"
        sut.applyEditorTo(settings)
        Assertions.assertEquals("fix", settings.wipCommitMessage)
    }

    @Test
    fun applyEditorTo_modified_nextStay() {
        val settings = createDefaultSettings()
        val sut = createDefaultForm()
        sut.nextStay.isSelected = true
        sut.applyEditorTo(settings)
        Assertions.assertTrue(settings.nextStay)
    }
}