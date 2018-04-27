/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-24         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui.interfaces;

public interface SettingClickListener
{
    public void clearHistory();

    public void clearCache();

    public void setUA(String UAName);
}
