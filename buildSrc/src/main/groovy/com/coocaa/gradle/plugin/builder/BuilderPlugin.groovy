package com.coocaa.gradle.plugin.builder

import com.coocaa.gradle.plugin.builder.task.release.TaskRelease
import org.gradle.api.Plugin
import org.gradle.api.Project

public class BuilderPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.gradle.addListener(new BuildTimeListener())
        TaskRelease.init(project)
    }
}