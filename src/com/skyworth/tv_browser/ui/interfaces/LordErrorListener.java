/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-6-6         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui.interfaces;

public interface LordErrorListener
{
    /**
     * @Description 加载网页的响应事件<br/>
     * @param isNetError
     *            是否是网络异常错误 void
     * @date 2016-6-6
     */
    public void onclick(boolean isNetError);

}
