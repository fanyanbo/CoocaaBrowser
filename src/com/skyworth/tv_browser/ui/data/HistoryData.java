/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-19 上午10:26     mikan
 *
 */

package com.skyworth.tv_browser.ui.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.data.HistorySupport;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className HistoryData
 * @date 16/4/19
 */
public class HistoryData
{
    private static final String TAG = "HistoryData";

    public String historyTitle = null;
    public String data = null;
    public String url = null;

    // private String id = null;

    public HistoryData(String historyTitle, String data, String url)
    {
        this.historyTitle = historyTitle;
        this.data = data;
        this.url = url;
    }

    public HistoryData(HistorySupport historySupport)
    {
        historyTitle = historySupport.getTitle();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date dt = new Date(historySupport.getTime());
        data = simpleDateFormat.format(dt);
        SkyLogger.i(TAG, "HistoryData: data = " + data);
        url = historySupport.getUrl();
        // id = historySupport.getId();
    }

    public String getHistoryTitle()
    {
        return historyTitle;
    }

    public void setHistoryTitle(String historyTitle)
    {
        this.historyTitle = historyTitle;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "HistoryData [historyTitle=" + historyTitle + ", data=" + data + ", url=" + url
                + "]";
    }

}
