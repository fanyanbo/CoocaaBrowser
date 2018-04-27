/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-26 下午5:38     mikan
 *
 */

package com.skyworth.tv_browser.ui.interfaces;

import com.skyworth.tv_browser.ui.data.HistoryData;

import java.util.List;

/**
 * <p>
 * Description:浏览历史界面相关接口
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className HistoryClickListener
 * @date 16/4/26
 */
public interface HistoryClickListener
{
    /**
     * 概述：获取浏览历史<br/>
     * 适用条件：<br/>
     * 执行流程：<br/>
     * 使用方法：<br/>
     * 注意事项：<br/>
     * 
     * @return 浏览历史列表 date 16/4/26
     */
    public List<HistoryData> getHistoryList();

    /**
     * 概述：跳转到某个界面<br/>
     * 适用条件：<br/>
     * 执行流程：<br/>
     * 使用方法：<br/>
     * 注意事项：<br/>
     * 
     * date 16/5/5
     */
    public void jumpToWebsite(String url);
}
