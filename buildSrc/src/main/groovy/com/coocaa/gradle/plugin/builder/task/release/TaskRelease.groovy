package com.coocaa.gradle.plugin.builder.task.release

import com.coocaa.gradle.plugin.builder.CCBuilderTask
import com.coocaa.gradle.plugin.builder.utils.PropertyFileUtils
import org.gradle.api.Project
import com.coocaa.gradle.plugin.utils.Logger

public class TaskRelease extends CCBuilderTask {
    public static class Extension {
        public static final String NAME = "ccbuilder_release_ext"

        public Extension() {}

        public Extension(int ver_build, String release_path) {
            this.ver_build = ver_build
            this.release_path = release_path
        }
        int ver_build
        String release_path
        String build_time = Date.newInstance().format("yyMMddHHmm")
        String name_suffix = null
    }

    public static final void init(Project project) {
        project.extensions.add(Extension.NAME, new Extension(getBuildVersion(project).toInteger(), "${project.rootProject.projectDir.absolutePath}/release"))
        project.task(NAME, dependsOn: ['assemble'], type: this)
    }

    public static final String NAME = "ccbuilder_release"

    private static String defaultSuffix(task, variant) {
        return '_' + task.getExt(Extension.NAME).build_time + "_${variant.versionName}"
    }

    TaskRelease() {
        super(NAME)
    }

    @Override
    void action() {
        super.action()
        copy2release()
        updateBuildVersion(project)
    }

    private void copy2release() {
        project.android.applicationVariants.all { variant ->
            variant.outputs.each {
                output ->
                    File file = output.outputFile
                    Logger.d("copy ${file.absolutePath} to ${getExt(Extension.NAME).release_path}")
                    project.copy {
                        from(file.getAbsolutePath()) {
                            if (getExt(Extension.NAME).name_suffix == null)
                                rename '.apk', "${TaskRelease.defaultSuffix(this, variant)}.apk"
                            else
                                rename '.apk', "${getExt(Extension.NAME).name_suffix}.apk"
                        }
                        into(getExt(Extension.NAME).release_path)
                    }
            }
        }
    }

    static String getVersionPropertyFilePath(Project project) {
        return project.projectDir.getAbsolutePath() + File.separator + 'version.property'
    }

    static String getBuildVersion(Project project) {
        return PropertyFileUtils.getProperty(getVersionPropertyFilePath(project), 'VERSION_BUILD', '0')
    }

    static void updateBuildVersion(Project project) {
        int ver_build = getBuildVersion(project).toInteger() + 1
        PropertyFileUtils.setProperty(getVersionPropertyFilePath(project), 'VERSION_BUILD', ver_build.toString())
    }
}