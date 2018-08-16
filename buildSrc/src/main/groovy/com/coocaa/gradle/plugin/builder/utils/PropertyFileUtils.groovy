package com.coocaa.gradle.plugin.builder.utils

public class PropertyFileUtils {

    public static String getProperty(String file, String key, String defaultValue) {
        def File _file = new File(file)
        if (_file.canRead()) {
            Properties properties = new Properties()
            properties.load(new FileInputStream(_file))
            def ret = properties.getProperty(key)
            return ret != null ? ret : defaultValue
        } else {
            _file.createNewFile()
            Properties properties = new Properties()
            properties.load(new FileInputStream(_file))
            properties[key] = defaultValue
            properties.store(_file.newWriter(), null)
            return defaultValue
        }
    }

    public static String setProperty(String file, String key, String value) {
        def File _file = new File(file);
        if (!_file.canWrite()) {
            _file.createNewFile()
        }
        Properties properties = new Properties()
        properties.setProperty(key, value)
        properties.store(_file.newWriter(), null)
        return value
    }
}