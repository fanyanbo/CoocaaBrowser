package com.coocaa.gradle.plugin.utils


public class Logger {
    public static d(String tag, String msg) {
        println("<${tag}>${msg}")
    }

    public static d(String msg) {
        d("CCGradle", msg)
    }
}