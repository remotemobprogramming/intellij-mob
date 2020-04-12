package com.nowsprinting.intellij_mob;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.Date;

public class MobTimer {
    private Date startTime;
    private Date expireTime;

    public static MobTimer getInstance(Project project) {
        return ServiceManager.getService(project, MobTimer.class);
    }

    public boolean isRunning() {
        return false;   // TODO:
    }
}