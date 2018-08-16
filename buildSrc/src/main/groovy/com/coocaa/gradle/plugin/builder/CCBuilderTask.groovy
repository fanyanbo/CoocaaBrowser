package com.coocaa.gradle.plugin.builder

import com.coocaa.gradle.plugin.utils.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public abstract class CCBuilderTask extends DefaultTask {
    private static final String GROUP = "CCBuilder"

    private String name;

    CCBuilderTask(String name) {
        setGroup(GROUP)
        this.name = name
    }

    @TaskAction
    public void action() {
        Logger.d("CCBuilder", "action----->${name}")
    }

    protected final Object getExt(String name) {
        return project.extensions.getByName(name)
    }
}