package com.nowsprinting.intellij_mob.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.nowsprinting.intellij_mob.MobBundle;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "MobProjectSettings",
        storages = {
                @Storage("/mob.xml")
        }
)
public class MobProjectSettings implements PersistentStateComponent<Element> {
    public static final String MOB_SETTINGS = "MobProjectSettings";
    public static final String KEY_WIP_BRANCH = "wip_branch";
    public static final String KEY_BASE_BRANCH = "base_branch";
    public static final String KEY_REMOTE_NAME = "remote_name";
    public static final String KEY_DEBUG = "debug";
    public static final String KEY_TIMER = "timer";
    public static final String KEY_START_WITH_SHARE = "start_with_share";
    public static final String KEY_NEXT_AT_EXPIRE = "next_at_expire";
    public static final String KEY_WIP_COMMIT_MESSAGE = "wip_commit_message";
    public static final String KEY_NEXT_STAY = "next_stay";
    public static final int TIMER_STATIC_DEFAULT = 10;  // use only if parse error

    public String wipBranch;
    public String baseBranch;
    public String remoteName;
    public boolean debug;
    public int timer;
    public boolean startWithShare;
    public boolean nextAtExpire;
    public String wipCommitMessage;
    public boolean nextStay;

    public static MobProjectSettings getInstance(Project project) {
        return ServiceManager.getService(project, MobProjectSettings.class);
    }

    @Override
    public void loadState(@NotNull Element state) {
        wipBranch = getStringAttributeFromElement(state, KEY_WIP_BRANCH);
        baseBranch = getStringAttributeFromElement(state, KEY_BASE_BRANCH);
        remoteName = getStringAttributeFromElement(state, KEY_REMOTE_NAME);
        debug = getBooleanAttributeFromElement(state, KEY_DEBUG);
        timer = getIntAttributeFromElement(state, KEY_TIMER, TIMER_STATIC_DEFAULT);
        startWithShare = getBooleanAttributeFromElement(state, KEY_START_WITH_SHARE);
        nextAtExpire = getBooleanAttributeFromElement(state, KEY_NEXT_AT_EXPIRE);
        wipCommitMessage = getStringAttributeFromElement(state, KEY_WIP_COMMIT_MESSAGE);
        nextStay = getBooleanAttributeFromElement(state, KEY_NEXT_STAY);
    }

    @Nullable
    @Override
    public Element getState() {
        final Element element = new Element(MOB_SETTINGS);
        element.setAttribute(KEY_WIP_BRANCH, wipBranch);
        element.setAttribute(KEY_BASE_BRANCH, baseBranch);
        element.setAttribute(KEY_REMOTE_NAME, remoteName);
        element.setAttribute(KEY_DEBUG, Boolean.toString(debug));
        element.setAttribute(KEY_TIMER, Integer.toString(timer));
        element.setAttribute(KEY_START_WITH_SHARE, Boolean.toString(startWithShare));
        element.setAttribute(KEY_NEXT_AT_EXPIRE, Boolean.toString(nextAtExpire));
        element.setAttribute(KEY_WIP_COMMIT_MESSAGE, wipCommitMessage);
        element.setAttribute(KEY_NEXT_STAY, Boolean.toString(nextStay));
        return element;
    }

    public void noStateLoaded() {
        wipBranch = readStringDefaultFromResourceBundle("mob.settings.default.wip_branch");
        baseBranch = readStringDefaultFromResourceBundle("mob.settings.default.base_branch");
        remoteName = readStringDefaultFromResourceBundle("mob.settings.default.remote_name");
        debug = readBooleanDefaultFromResourceBundle("mob.settings.default.debug");
        timer = readIntDefaultFromResourceBundle("mob.settings.default.timer", TIMER_STATIC_DEFAULT);
        startWithShare = readBooleanDefaultFromResourceBundle("mob.settings.default.start_with_share");
        nextAtExpire = readBooleanDefaultFromResourceBundle("mob.settings.default.next_at_expire");
        wipCommitMessage = readStringDefaultFromResourceBundle("mob.settings.default.wip_commit_message");
        nextStay = readBooleanDefaultFromResourceBundle("mob.settings.default.next_stay");
    }

    private String getStringAttributeFromElement(Element element, String key) {
        return element.getAttributeValue(key);
    }

    private int getIntAttributeFromElement(Element element, String key, int defaultValue) {
        try {
            return Integer.parseInt(getStringAttributeFromElement(element, key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean getBooleanAttributeFromElement(Element element, String key) {
        return getStringAttributeFromElement(element, key).toLowerCase().equals("true");
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