package com.nowsprinting.intellij_mob.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.NonDefaultProjectConfigurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
//import com.intellij.plugins.haxe.util.HaxeUtil;
import com.nowsprinting.intellij_mob.MobBundle;
import com.nowsprinting.intellij_mob.config.ui.MobSettingsForm;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MobSettingsConfigurable implements SearchableConfigurable, NonDefaultProjectConfigurable {
    private MobSettingsForm mySettingsPane;
    private final Project myProject;

    public MobSettingsConfigurable(Project project) {
        myProject = project;
    }

    public String getDisplayName() {
        return MobBundle.message("mob.settings.name");
    }

    @NotNull
    public String getId() {
        return "mob.settings";
    }

    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        if (mySettingsPane == null) {
            mySettingsPane = new MobSettingsForm();
        }
        reset();
        return mySettingsPane.getPanel();
    }

    public boolean isModified() {
        return mySettingsPane != null && mySettingsPane.isModified(getSettings());
    }

    public void apply() throws ConfigurationException {
        if (mySettingsPane != null) {
            final boolean modified = isModified();
            mySettingsPane.applyEditorTo(getSettings());
            if (modified) {
//                HaxeUtil.reparseProjectFiles(myProject);
            }
        }
    }

    public void reset() {
        if (mySettingsPane != null) {
            mySettingsPane.resetEditorFrom(getSettings());
        }
    }

    private MobProjectSettings getSettings() {
        return MobProjectSettings.getInstance(myProject);
    }

    public void disposeUIResources() {
        mySettingsPane = null;
    }

    public Runnable enableSearch(String option) {
        return null;
    }
}