/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-15         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.data.BookmarkSupport;
import com.skyworth.tv_browser.data.HistorySupport;
import com.skyworth.tv_browser.data.WindowData;
import com.skyworth.tv_browser.ui.data.CollectionData;
import com.skyworth.tv_browser.ui.data.HistoryData;
import com.skyworth.tv_browser.ui.interfaces.CollectionClickListener;
import com.skyworth.tv_browser.ui.interfaces.HistoryClickListener;
import com.skyworth.tv_browser.ui.interfaces.MenuToolControlItemListener;
import com.skyworth.tv_browser.ui.interfaces.MenuToolbarLayoutListener;
import com.skyworth.tv_browser.ui.interfaces.MultWindowClickListener;
import com.skyworth.tv_browser.ui.interfaces.SettingClickListener;
import com.skyworth.tv_browser.util.BrowserDefine;
import com.skyworth.tv_browser.util.UIUtil;

/**
 * @ClassName MenuToolbarLayout
 * @Description 主界面菜单主ui
 * @author wolfboyjiang
 * @date 2016-4-16
 * @version 1
 */
public class MenuToolbarLayout extends FrameLayout implements MenuToolbarLayoutListener
{

    private static final String TAG = "MenuToolbarLayout";
    private Context mContext = null;
    private MenuToolControlItemListener controlItemListener = null;
    private LinearLayout mainLayout = null;
    private boolean isCollectWeb = false;
    private boolean isCanRefrushWeb = true;

    /**
     * @Fields imageResourceIdd 保存菜单上每一项的图片资源的id
     */
    private int imageResourceIdd[] = { R.drawable.menu_back_unfocus,
            R.drawable.menu_forward_unfocus, R.drawable.menu_refrush_unfocus,
            R.drawable.menu_collect_unfocus, R.drawable.menu_bookmark_unfocus,
            R.drawable.menu_windows_unfocus, R.drawable.menu_history_unfocus,
            R.drawable.menu_setting_unfocus, R.drawable.menu_exit_unfocus };
    /**
     * @Fields contentResourceId 保存菜单上每一项的文字资源的id
     */
    private int contentResourceId[] = { R.string.back, R.string.forward, R.string.refrush,
            R.string.collect_website, R.string.collection_title, R.string.windows,
            R.string.history_title, R.string.setting, R.string.exitbtn };

    private ArrayList<MenuToolbarItemLayout> menuItemsList = null;
    private MenuItemDialog menuItemDialog = null;

    public MenuToolbarLayout(Context context)
    {
        super(context);
        mContext = context;
        initView();

    }

    private void initView()
    {
        mainLayout = new LinearLayout(mContext);
        // mainLayout.setBackgroundColor(Color.parseColor("#B1D6F9"));
        mainLayout.setBackgroundResource(R.drawable.menu_bg);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);

        menuItemsList = new ArrayList<MenuToolbarItemLayout>();

        for (int i = 0; i < imageResourceIdd.length; i++)
        {
            MenuToolbarItemLayout itemLayout = new MenuToolbarItemLayout(mContext,
                    imageResourceIdd[i], contentResourceId[i]);
            itemLayout.setId(BrowserDefine.MENU_VIEW_ID + i);
            itemLayout.setOnClickListener(itemOnClickListener);
            LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(
                    UIUtil.getResolutionValue(202), LayoutParams.MATCH_PARENT);
            if (i == 0)
            {
                itemLayoutParams.leftMargin = UIUtil.getResolutionValue(55);
            }
            mainLayout.addView(itemLayout, itemLayoutParams);
            menuItemsList.add(itemLayout);
        }

        // menuItemDialog = new MenuItemDialog(mContext);

        this.addView(mainLayout, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setControlItemListener(MenuToolControlItemListener controlItemListener)
    {
        this.controlItemListener = controlItemListener;
    }

    public void updateBarState(boolean isCanBack, boolean isCanFoword, boolean isCanRefrush)
    {
        SkyLogger.d(TAG, "updateBarState,iscanback:" + isCanBack + ",iscanfoword：" + isCanFoword
                + ",isCanRefrush:" + isCanRefrush);
        if (isCanBack)
        {
            menuItemsList.get(0).setFocusable(true);
            menuItemsList.get(0).updateView(R.drawable.menu_back_unfocus, R.string.back);
        }
        if (isCanFoword)
        {
            menuItemsList.get(1).setFocusable(true);
            menuItemsList.get(1).updateView(R.drawable.menu_forward_unfocus, R.string.forward);
        }
        isCanRefrushWeb = isCanRefrush;
        if (isCanRefrushWeb)
        {
            menuItemsList.get(2).updateView(R.drawable.menu_refrush_unfocus, R.string.refrush);
        }
    }

    public void show(boolean isSaved, boolean isCanBack, boolean isCanFoword, boolean isCanRefrush,
            boolean isHomeShow)
    {
        SkyLogger.d(TAG, "show,isSaved:" + isSaved + ",iscanback:" + isCanBack + ",iscanfoword："
                + isCanFoword + ",isCanRefrush:" + isCanRefrush + ",isHomeShow:" + isHomeShow);
        this.getfocus();
        isCollectWeb = isSaved;
        if (!isCanBack)
        {
            menuItemsList.get(0).setFocusable(false);
            menuItemsList.get(0).updateView(R.drawable.menu_back_unfocusable, R.string.back);

        } else
        {
            menuItemsList.get(0).setFocusable(true);
            menuItemsList.get(0).updateView(R.drawable.menu_back_unfocus, R.string.back);
        }
        if (!isCanFoword)
        {
            menuItemsList.get(1).setFocusable(false);
            menuItemsList.get(1).updateView(R.drawable.menu_forward_unfocusable, R.string.forward);
        } else
        {
            menuItemsList.get(1).setFocusable(true);
            menuItemsList.get(1).updateView(R.drawable.menu_forward_unfocus, R.string.forward);
        }
        if (isHomeShow)
        {
            menuItemsList.get(2).setFocusable(false);
            menuItemsList.get(2).updateView(R.drawable.menu_refrush_unfocusable, R.string.refrush);
            menuItemsList.get(3).setFocusable(false);
            menuItemsList.get(3).updateView(R.drawable.menu_collect_unfocusable,
                    R.string.collect_website);
        } else
        {
            menuItemsList.get(2).setFocusable(true);
            menuItemsList.get(3).setFocusable(true);

            if (!isCanRefrush)
            {
                menuItemsList.get(2).updateView(R.drawable.menu_stop_refrush_unfocus,
                        R.string.stop_refrush);
            } else
            {
                menuItemsList.get(2).updateView(R.drawable.menu_refrush_unfocus, R.string.refrush);
            }
            if (isSaved)
            {
                menuItemsList.get(3).updateView(R.drawable.menu_collect_light_unfocus,
                        R.string.collected);
            } else
            {
                menuItemsList.get(3).updateView(R.drawable.menu_collect_unfocus,
                        R.string.collect_website);
            }
        }

        isCanRefrushWeb = isCanRefrush;
        if (menuItemsList != null && menuItemsList.get(4) != null)
        {
            SkyLogger.d(TAG, "show,4 requestFocus:");
            menuItemsList.get(4).requestFocus();
            menuItemsList.get(4).requestFocusFromTouch();
            // menuItemsList.get(4).updateView(R.drawable.menu_bookmark_focus,
            // R.string.collection_title);
        }
    }

    public void getfocus()
    {
        if (menuItemsList != null && menuItemsList.get(4) != null)
        {
            menuItemsList.get(4).requestFocus();
            menuItemsList.get(4).requestFocusFromTouch();
            SkyLogger.d(TAG, "show,4 requestFocus:");
        }
    }

    private boolean itemsFocusable()
    {
        for (int i = 0; i < 4; i++)
        {
            if (menuItemsList.get(i).hasFocusable())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event)
    {

        SkyLogger.d(TAG, "MenuToolbarLayout,dispatchKeyEventPreIme event:" + event.toString());
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (event.getKeyCode())
            {
                case KeyEvent.KEYCODE_DPAD_UP:

                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:

                    if (!itemsFocusable() && menuItemsList.get(4).hasFocus())
                    {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }

        return super.dispatchKeyEventPreIme(event);
    }

    /**
     * @Fields itemOnClickListener 响应元素的点击事件
     */
    View.OnClickListener itemOnClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            int viewID = v.getId();
            SkyLogger.d(TAG, "MenuToolbarLayout,onClick viewID:" + viewID);
            if (menuItemDialog == null)
            {
                menuItemDialog = new MenuItemDialog(mContext);
            }
            switch (viewID)
            {
                case BrowserDefine.MENU_VIEW_ID: // 后退
                    controlItemListener.back();
                    break;
                case BrowserDefine.MENU_VIEW_ID + 1: // 前进
                    controlItemListener.forward();
                    break;
                case BrowserDefine.MENU_VIEW_ID + 2:// 刷新
                    controlItemListener.refresh(!isCanRefrushWeb);
                    break;
                case BrowserDefine.MENU_VIEW_ID + 3:// 收藏网站
                    if (isCollectWeb)
                    {
                        controlItemListener.removeBookmark();

                    } else
                    {
                        controlItemListener.saveBookMark();
                    }

                    break;
                case BrowserDefine.MENU_VIEW_ID + 4:// 收藏夹
                    CollectionLayout collectionLayout = new CollectionLayout(mContext,
                            mCollectionClickListener, MenuToolbarLayout.this);
                    menuItemDialog.setContentView(
                            collectionLayout,
                            new LayoutParams(UIUtil.getResolutionValue(1920), UIUtil
                                    .getResolutionValue(1080)));
                    menuItemDialog.show();
                    break;
                case BrowserDefine.MENU_VIEW_ID + 5:// 多窗口
                    ArrayList<WindowData> windowsList = new ArrayList<WindowData>();
                    Bitmap webImageBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.window_add_new_window_unfocus);
                    windowsList.add(new WindowData(mContext.getString(R.string.add_new_window_str),
                            webImageBitmap, ""));
                    // controlItemListener.getWindows();
                    for (WindowData windowData : controlItemListener.getWindows())
                    {
                        windowsList.add(windowData);
                    }
                    // test data
                    // windowsList.add(new WindowData("百度一下", webImageBitmap,
                    // "http://www.baidu.com"));
                    // windowsList.add(new WindowData("软件研究院", webImageBitmap,
                    // "http://srt.skyworth.com/phpwind/index.php?m=bbs"));
                    // windowsList.add(new WindowData("知乎", webImageBitmap,
                    // "https://www.zhihu.com/"));

                    MultiWindowLayout multiWindowLayout = new MultiWindowLayout(mContext,
                            windowsList, multWindowClickListener,
                            controlItemListener.getWindowPosition());
                    menuItemDialog.setContentView(
                            multiWindowLayout,
                            new LayoutParams(UIUtil.getResolutionValue(1920), UIUtil
                                    .getResolutionValue(1080)));
                    menuItemDialog.show();
                    break;
                case BrowserDefine.MENU_VIEW_ID + 6:// 浏览记录
                    HistoryLayout historyLayout = new HistoryLayout(mContext, mHistoryClickListener);

                    menuItemDialog.setContentView(
                            historyLayout,
                            new LayoutParams(UIUtil.getResolutionValue(1920), UIUtil
                                    .getResolutionValue(1080)));
                    menuItemDialog.show();
                    break;
                case BrowserDefine.MENU_VIEW_ID + 7:// 设置
                    // String ua = controlItemListener.getUA();
                    SettingLayout settingLayout = new SettingLayout(mContext,
                            controlItemListener.getUA());
                    settingLayout.setSettingClickListener(settingClickListener);
                    menuItemDialog.setContentView(
                            settingLayout,
                            new LayoutParams(UIUtil.getResolutionValue(1920), UIUtil
                                    .getResolutionValue(1080)));
                    menuItemDialog.show();
                    break;
                case BrowserDefine.MENU_VIEW_ID + 8:// 退出
                    controlItemListener.exit();
                    break;

                default:
                    break;
            }
            controlItemListener.showMenuIcon();
        }
    };

    private SettingClickListener settingClickListener = new SettingClickListener()
    {

        @Override
        public void setUA(String UAName)
        {
            controlItemListener.setUA(UAName);

        }

        @Override
        public void clearHistory()
        {
            controlItemListener.clearHistory();
        }

        @Override
        public void clearCache()
        {
            controlItemListener.clearCache();
        }
    };

    private HistoryClickListener mHistoryClickListener = new HistoryClickListener()
    {
        @Override
        public List<HistoryData> getHistoryList()
        {

            List<HistorySupport> historySupportList = controlItemListener.getHistoryList();
            if (historySupportList == null || historySupportList.isEmpty())
            {
                return null;
            } else
            {
                List<HistoryData> historyDataArrayList = new ArrayList<HistoryData>();
                for (HistorySupport historySupport : historySupportList)
                {
                    historyDataArrayList.add(new HistoryData(historySupport));
                }
                return historyDataArrayList;
            }
        }

        @Override
        public void jumpToWebsite(String url)
        {
            controlItemListener.jumpToWebsite(url);
            controlItemListener.deleteHistory(url);
            dismissMenuItemDialog();

        }
    };

    private CollectionClickListener mCollectionClickListener = new CollectionClickListener()
    {
        @Override
        public List<CollectionData> getCollectList()
        {
            List<BookmarkSupport> bookmarkSupportList = controlItemListener.getCollectList();
            if (bookmarkSupportList == null || bookmarkSupportList.isEmpty())
            {
                return null;
            } else
            {
                List<CollectionData> collectionDataList = new ArrayList<CollectionData>();
                for (BookmarkSupport bookmarkSupport : bookmarkSupportList)
                {
                    collectionDataList.add(new CollectionData(bookmarkSupport));
                }
                return collectionDataList;
            }
        }

        @Override
        public boolean removeCollect(CollectionData data)
        {
            return controlItemListener.removeCollect(data.url);
        }

        @Override
        public void jumpToWebsite(String url)
        {
            controlItemListener.jumpToWebsite(url);
            dismissMenuItemDialog();
        }
    };

    private MultWindowClickListener multWindowClickListener = new MultWindowClickListener()
    {

        @Override
        public void removeWindow(int location)
        {
            SkyLogger.d(TAG, "MultWindowClickListener,removeWindow location:" + location);
            controlItemListener.removeWindow(location);
            dismissMenuItemDialog();
        }

        @Override
        public void clickWindow(WindowData data, int location)
        {
            SkyLogger.d(TAG, "MultWindowClickListener,clickWindow data:" + data.toString()
                    + ",location:" + location);
            controlItemListener.jumpToWindow(data.getUrl(), location);
            dismissMenuItemDialog();
        }

        @Override
        public void addNewWindow()
        {
            SkyLogger.d(TAG, "MultWindowClickListener,addNewWindow:");
            controlItemListener.addWindow();
            dismissMenuItemDialog();
        }
    };

    @Override
    public void dismissMenuItemDialog()
    {
        if (menuItemDialog == null)
        {
            SkyLogger.w(TAG, "dismiss: is null , cancel dismiss work");
            return;
        }
        menuItemDialog.dismiss();
        menuItemDialog = null;

    }
}
