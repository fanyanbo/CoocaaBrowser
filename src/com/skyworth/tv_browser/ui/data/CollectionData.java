/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-21 下午4:04     mikan
 *
 */

package com.skyworth.tv_browser.ui.data;

import com.skyworth.tv_browser.data.BookmarkSupport;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className CollectionData
 * @date 16/4/21
 */
public class CollectionData
{
    private static final String TAG = "CollectionData";

    public String iconPath = null;
    public String collectionTitle = null;
    public String url = null;

    // public String id = null;

    public CollectionData(String iconPath, String collectionTitle, String url)
    {
        this.iconPath = iconPath;
        this.collectionTitle = collectionTitle;
        this.url = url;
    }

    public CollectionData(BookmarkSupport bookmarkData)
    {
        // TODO mikan need icon
        iconPath = bookmarkData.getFav();
        collectionTitle = bookmarkData.getTitle();
        url = bookmarkData.getUrl();
    }

    public String getIconPath()
    {
        return iconPath;
    }

    public void setIconPath(String iconPath)
    {
        this.iconPath = iconPath;
    }

    public String getCollectionTitle()
    {
        return collectionTitle;
    }

    public void setCollectionTitle(String collectionTitle)
    {
        this.collectionTitle = collectionTitle;
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
        return "CollectionData [iconPath=" + iconPath + ", collectionTitle=" + collectionTitle
                + ", url=" + url + "]";
    }

}
