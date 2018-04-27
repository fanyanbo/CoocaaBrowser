/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-5-10         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui.interfaces;

import com.skyworth.tv_browser.data.WindowData;

public interface MultWindowClickListener
{

    public void addNewWindow();

    public void removeWindow(int location);

    public void clickWindow(WindowData data,int location);
}
