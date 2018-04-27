package com.skyworth.tv_browser.data;

import android.graphics.Bitmap;

public class WindowData
{
    public String title;
    public Bitmap bm;
    public String url;

    public WindowData(String title, Bitmap bm, String url)
    {
        this.title = title;
        this.bm = bm;
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Bitmap getBm()
    {
        return bm;
    }

    public void setBm(Bitmap bm)
    {
        this.bm = bm;
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
        return "WindowData [title=" + title + ", bm=" + bm + ", url=" + url + "]";
    }

}
