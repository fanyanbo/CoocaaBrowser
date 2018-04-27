/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-24         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.skyworth.tv_browser.data.BookmarkSupport;
import com.skyworth.tv_browser.data.HistorySupport;
import com.skyworth.tv_browser.data.WindowData;

/**
 * @ClassName MenuToolControlItemLisentner
 * @Description 控制菜单元素的点击事件监听回调
 * @author wolfboyjiang
 * @date 2016-4-24
 * @version 1
 */
public interface MenuToolControlItemListener
{
    public void exit();

    public void back();

    public void forward();

    public void refresh(boolean isLoading);

    public void saveBookMark();

    public void removeBookmark();

    public void clearHistory();

    public void deleteHistory(String url);

    public void clearCache();

    public void setUA(String UAName);

    public String getUA();

    /**
     * 概述：获取浏览历史<br/>
     * 适用条件：<br/>
     * 执行流程：<br/>
     * 使用方法：<br/>
     * 注意事项：<br/>
     *
     * @return 浏览历史列表
     * date 16/4/26
     */
    public List<HistorySupport> getHistoryList();

    /**
     * 概述：获取收藏列表<br/>
     *
     * @return 收藏列表
     * date 16/4/26
     */
    public List<BookmarkSupport> getCollectList();

    /**
     * 概述：移除某个收藏网页<br/>
     *
     * @return 移除是否成功
     * date 16/4/26
     */
    public boolean removeCollect(String title);

    /**
     * 概述：跳转到某个网址<br/>
     *
     * date 16/5/5
     */
    public void jumpToWebsite(String url);
    
    /**
     * 概述：跳转到某个网址,同时更新当前window位置<br/>
     *
     * date 16/5/5
     */
    public void jumpToWindow(String url,int location);
    /**
     * @Description 删除window窗口<br/>
     * @param index 
     * void
     * @date 2016-5-9
     */
    public void removeWindow(int index);
    /**
     * @Description 添加一个window窗口<br/>
     * @date 2016-5-9
     */
    public void addWindow();
    /**
     * @Description 获取当前所有的window窗口<br/>
     * @return  ArrayList<WindowData> 返回的window列表
     * @date 2016-5-9
     */
    public ArrayList<WindowData> getWindows();
    
    public void showMenuIcon();
    
    /**
     * @Description 获取当前焦点的window位置<br/>
     * @return 
     * int 当前焦点的window位置
     * @date 2016-6-14
     */
    public int getWindowPosition();
    
}
