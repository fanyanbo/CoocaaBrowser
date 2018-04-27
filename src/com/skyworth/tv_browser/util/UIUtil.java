/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-7         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.math.BigDecimal;

public class UIUtil
{

    private static float resolutionDiv;
    private static float dipDiv;

    /**
     * 概述：得到屏幕的宽度<br/>
     * 
     * @param context
     * @return int
     * @date 2013-10-22
     */
    public static int getDisplayWidth(Context context)
    {
        Context thisContext = context;
        Display display = ((WindowManager) thisContext.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        if (display == null)
        {
            return 1920;
        }
        return display.getWidth();
    }

    public static float getDipDiv()
    {
        return dipDiv;
    }

    public static float getResolutionDiv()
    {
        return resolutionDiv;
    }

    /**
     * 概述：得到当前density<br/>
     * 
     * @param context
     * @return float
     * @date 2013-12-20
     */
    public static float getDisplayDensity(Context context)
    {
        Context thisContext = context;
        DisplayMetrics dm = new DisplayMetrics();
        dm = thisContext.getApplicationContext().getResources().getDisplayMetrics();
        float density = dm.density;
        Log.i("uidpi", "density:" + density);
        return density;
    }

    /**
     * 获取当前屏幕div和分辨率参数 需要首先调用,一个工程只需调用一次
     * 
     * @param context
     * @return
     */
    public static void setDpiDiv_Resolution(Context context)
    {
        int width = getDisplayWidth(context);
        switch (width)
        {
            case 3840:
                resolutionDiv = 0.5f;
                break;
            case 1920:
                resolutionDiv = 1;
                break;
            case 1366:
                resolutionDiv = 1.4f;
                break;
            case 1280:
                resolutionDiv = 1.5f;
                break;
            default:
                resolutionDiv = 1;
                break;
        }
        dipDiv = resolutionDiv * getDisplayDensity(context);
        Log.i("uidpi", "dipDiv:" + dipDiv + ", resolutionDiv:" + resolutionDiv);
    }

    /**
     * 获取当前分辨率UI实际需要的大小 根据当前分辨率适配UI
     * 
     * @return
     */
    public static int getResolutionValue(int value)
    {
        if (value < 0)
            return 0;
        String rv = String.valueOf((float) (value / resolutionDiv));
        BigDecimal bd = new BigDecimal(rv).setScale(0, BigDecimal.ROUND_HALF_UP);
        int r_value = bd.intValue();
        return r_value;
    }

    /**
     * 获取当前分辨率文字实际需要的大小 根据DPI值适配当前文字
     * 
     * @return
     */
    public static int getTextDpiValue(int value)
    {
        if (value < 0)
            return 0;

        int r_value = (int) (value / dipDiv);
        return r_value;
    }

}
