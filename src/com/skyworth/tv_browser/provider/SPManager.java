package com.skyworth.tv_browser.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPManager
{
    private SharedPreferences sharedPreferences = null;
    private static SPManager sp = null;

    public static SPManager getInstance(Context c)
    {
        if (sp == null)
        {
            sp = new SPManager(c);
        }
        return sp;
    }

    private SPManager(Context c)
    {
        sharedPreferences = c.getSharedPreferences("tvbrowser_config", Context.MODE_PRIVATE);
    }

    public synchronized String getStringValue(String key)
    {
        return sharedPreferences.getString(key, "");
    }

    public synchronized void saveString(String key, String value)
    {
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
