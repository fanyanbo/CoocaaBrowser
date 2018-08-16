package com.coocaa.gradle.plugin.config

import com.android.build.gradle.internal.dsl.SigningConfig
import com.coocaa.gradle.plugin.config.task.init.TaskInit
import com.coocaa.gradle.plugin.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

public class ConfigPlugin implements Plugin<Project> {
    private static final String CONFIG_DIR = "buildSrc${File.separator}.cc"
    private static final String KEYSTORES_DIR = "${CONFIG_DIR}${File.separator}keystores"
    private static final int API_LEVEL = 23
    private static final String BUILD_TOOLS = "23.0.3"
    private static final int MIN_SDK_VERSION = 17
    private static final int TARGET_SDK_VERSION = API_LEVEL

    private Project mProject

    @Override
    void apply(Project project) {
        mProject = project
        initConfigs(project)
        initTasks(project)
    }

    private void initConfigs(Project project) {
        initBuildEnv(project)
        initAndroidSDK(project)
        initSignPlatform(project)
    }

    private void initTasks(Project project) {
        project.task('configTask') << {
            println "Hello gradle plugin"
        }
        TaskInit.init(project)
    }

    private void initAndroidSDK(Project project) {
        project.rootProject.gradle.ext.androidJar = getAndroidJar()
        project.rootProject.gradle.ext.layoutlibJar = getLayoutlibJar()
    }

    private void initBuildEnv(Project project) {
        project.rootProject.gradle.ext.api = API_LEVEL
        project.rootProject.gradle.ext.buildTools = BUILD_TOOLS
        project.rootProject.gradle.ext.minSdkVersion = MIN_SDK_VERSION
        project.rootProject.gradle.ext.targetSdkVersion = TARGET_SDK_VERSION
    }

    private void initSignPlatform(Project project) {
        project.rootProject.gradle.ext.sign_plaform =
                createSigningConfig("platform.keystore", "android", "skyworth", "android")
        project.rootProject.gradle.ext.sign_cloudtv801 =
                createSigningConfig("cloudtv_801.keystore", "android", "skyworth", "android")
        project.rootProject.gradle.ext.sign_superx =
                createSigningConfig("superx.keystore", "superx", "superx", "superx")
        project.rootProject.gradle.ext.sign_tianci801 =
                createSigningConfig("tianci_801.keystore", "android", "skyworth", "android")
        project.rootProject.gradle.ext.sign_android_default =
                createSigningConfig("android_default.keystore", "android", "skyworth", "android")
    }

    private SigningConfig createSigningConfig(String fileName, String storePassword, String keyAlias, String keyPassword) {
        String root = new File("").getAbsolutePath()
        SigningConfig sc = new SigningConfig()
        sc.storeFile = new File("${root}${File.separator}${KEYSTORES_DIR}${File.separator}${fileName}")
        sc.storePassword = storePassword
        sc.keyAlias = keyAlias
        sc.keyPassword = keyPassword
        sc
    }

    private String getLayoutlibJar() {
        def path = getSDKPath()
        def layoutlibPath = "${path}${File.separator}data${File.separator}layoutlib.jar"
        Logger.d('layoutlibJar:' + layoutlibPath + '@' + API_LEVEL)
        return layoutlibPath
    }

    private String getAndroidJar() {
        def path = getSDKPath()
        def androidPath = "${path}${File.separator}android.jar"
        Logger.d('androidJar:' + androidPath + '@' + API_LEVEL)
        return androidPath
    }

    private String getSDKPath() {
        def android_home = System.getenv()['ANDROID_HOME']
        if (android_home == null || android_home.equals("")) {
            android_home = getSDKPathFromLocalProperty()
        }
        if (android_home == null || android_home.equals(""))
            return 'not found android.jar'
        def compileSdkVersion = 'android-' + API_LEVEL
        def path = "${android_home}${File.separator}platforms${File.separator}${compileSdkVersion}"
        return path
    }

    private String getSDKPathFromLocalProperty() {
        def rootDir = mProject.rootDir
        def localProperties = new File(rootDir, "local.properties")
        if (localProperties.exists()) {
            Properties properties = new Properties()
            localProperties.withInputStream {
                instr -> properties.load(instr)
            }
            return properties.getProperty('sdk.dir')
        }
        return ""
    }
}