package com.nowsprinting.intellij_mob.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.sun.istack.NotNull;

import java.util.Date;

public class TimerService {
    private Date startTime;
    private Date expireTime;

    public static TimerService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, TimerService.class);
    }

    public boolean isRunning() {
        return false;   // TODO:
    }
}