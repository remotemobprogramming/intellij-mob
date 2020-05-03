package com.nowsprinting.intellij_mob.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.nowsprinting.intellij_mob.MobBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "MobProjectSettings",
        storages = {
                @Storage("mob.xml")
        }
)
public class MobProjectSettings implements PersistentStateComponent<MobProjectSettings> {
    public String wipBranch;
    public String baseBranch;
    public String remoteName;
    public int timerMinutes;
    public boolean startWithShare;
    public String wipCommitMessage;
    public boolean nextStay;

    public static MobProjectSettings getInstance(Project project) {
        return ServiceManager.getService(project, MobProjectSettings.class);
    }

    @Override
    public void loadState(@NotNull MobProjectSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Nullable
    @Override
    public MobProjectSettings getState() {
        return this;
    }

    public void noStateLoaded() {
        wipBranch = readStringDefaultFromResourceBundle("mob.settings.default.wip_branch");
        baseBranch = readStringDefaultFromResourceBundle("mob.settings.default.base_branch");
        remoteName = readStringDefaultFromResourceBundle("mob.settings.default.remote_name");
        timerMinutes = readIntDefaultFromResourceBundle("mob.settings.default.timer_minutes", 0);
        startWithShare = readBooleanDefaultFromResourceBundle("mob.settings.default.start_with_share");
        wipCommitMessage = readStringDefaultFromResourceBundle("mob.settings.default.wip_commit_message");
        nextStay = readBooleanDefaultFromResourceBundle("mob.settings.default.next_stay");
    }

    private String readStringDefaultFromResourceBundle(String key) {
        return MobBundle.message(key);
    }

    private int readIntDefaultFromResourceBundle(String key, int defaultValue) {
        try {
            return Integer.parseInt(readStringDefaultFromResourceBundle(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean readBooleanDefaultFromResourceBundle(String key) {
        return readStringDefaultFromResourceBundle(key).toLowerCase().equals("true");
    }
}