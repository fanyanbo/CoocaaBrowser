package com.coocaa.gradle.plugin.demo

import org.gradle.api.Plugin
import org.gradle.api.Project

public class PluginDemo implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task('testTask') << {
            println "Hello gradle plugin"
        }
    }
}