/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-18         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.MenuController;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.util.BrowserDefine;
import com.skyworth.tv_browser.util.UIUtil;

/**
 * @ClassName MenuToolbarItemLayout
 * @Description 菜单中每一个元素的布局，使用LinearLayout
 * @author wolfboyjiang
 * @date 2016-4-18
 * @version 1
 */
public class MenuToolbarItemLayout extends FrameLayout
{

    private static final String TAG = "MenuToolbarLayout";
    private Context mContext;
    private ImageView itemImageView = null;
    private TextView contentTextView = null;
    private boolean isCollectWeb = false;
    private boolean isCanRefrush = true;
    /**
     * @Fields imageResourceIdd 保存菜单上每一项的图片资源的id
     */
    private int imageResourceIdUnfocus[] = { R.drawable.menu_back_unfocus,
            R.drawable.menu_forward_unfocus, R.drawable.menu_refrush_unfocus,
            R.drawable.menu_collect_unfocus, R.drawable.menu_bookmark_unfocus,
            R.drawable.menu_windows_unfocus, R.drawable.menu_history_unfocus,
            R.drawable.menu_setting_unfocus, R.drawable.menu_exit_unfocus,
            R.drawable.menu_collect_light_unfocus, R.drawable.menu_stop_refrush_unfocus };
    /**
     * @Fields imageResourceIdd 保存菜单上每一项的图片资源的id
     */
    private int imageResourceIdFocus[] = { R.drawable.menu_back_focus,
            R.drawable.menu_forward_focus, R.drawable.menu_refrush_focus,
            R.drawable.menu_collect_foxus, R.drawable.menu_bookmark_focus,
            R.drawable.menu_windows_focus, R.drawable.menu_history_focus,
            R.drawable.menu_setting_focus, R.drawable.menu_exit_focus,
            R.drawable.menu_collect_light_focus, R.drawable.menu_stop_refrush_focus };

    private MenuController browserController = null;

    /**
     * @Description 构造一个菜单的元素
     * @param context
     *            上下文
     * @param imageViewSourceID
     *            元素图片id
     * @param contentResourceId
     *            元素内容id
     */
    public MenuToolbarItemLayout(Context context, int imageViewSourceID, int contentResourceId)
    {
        super(context);
        mContext = context;
        this.setFocusable(true);
        this.setHovered(true);
        initView(imageViewSourceID, contentResourceId);
    }

    @SuppressLint("NewApi")
    private void initView(int imageViewSourceID, int contentResourceId)
    {
        itemImageView = new ImageView(mContext);
        itemImageView.setBackground(mContext.getResources().getDrawable(imageViewSourceID));
        FrameLayout.LayoutParams itemImageLayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(194), UIUtil.getResolutionValue(194));
        itemImageLayoutParams.topMargin = UIUtil.getResolutionValue(62);
        itemImageLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        contentTextView = new TextView(mContext);
        contentTextView.setText(mContext.getResources().getString(contentResourceId));
        contentTextView.setTextSize(UIUtil.getTextDpiValue(36));
        contentTextView.setTextColor(mContext.getResources()
                .getColor(R.color.menu_item_word_5b5b5b));
        FrameLayout.LayoutParams contentTextViewLayoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        contentTextViewLayoutParams.topMargin = UIUtil.getResolutionValue(200);
        contentTextViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        this.addView(itemImageView, itemImageLayoutParams);
        this.addView(contentTextView, contentTextViewLayoutParams);
        this.setOnFocusChangeListener(itemOnFocusChangeListener);
        this.setOnHoverListener(itemOnHoverListener);

    }

    @SuppressLint("NewApi")
    public void updateView(int imageResourceID, int conteentID)
    {
        if (imageResourceID == R.drawable.menu_collect_light_unfocus)
        {
            isCollectWeb = true;

        } else
        {
            isCollectWeb = false;
        }
        if (imageResourceID == R.drawable.menu_stop_refrush_unfocus)
        {
            isCanRefrush = false;
        } else
        {
            isCanRefrush = true;
        }
        itemImageView.setBackground(mContext.getResources().getDrawable(imageResourceID));
        contentTextView.setText(mContext.getResources().getString(conteentID));
    }

    public MenuController getBrowserController()
    {
        return browserController;
    }

    public void setBrowserController(MenuController browserController)
    {
        this.browserController = browserController;
    }

    private void updateView(boolean hasFocus, int imageResourceID)
    {
        SkyLogger.d(TAG, "updateView,hasFocus:" + hasFocus + ",imageResourceID:" + imageResourceID);
        if (hasFocus)
        {
            itemImageView.setBackgroundResource(imageResourceIdFocus[imageResourceID]);
        } else
        {
            itemImageView.setBackgroundResource(imageResourceIdUnfocus[imageResourceID]);
        }
    }

    private void updateCollectView(boolean hasFocus, int imageResourceID)
    {
        SkyLogger.d(TAG, "updateCollectView,hasFocus:" + hasFocus + ",imageResourceID:"
                + imageResourceID);
        if (hasFocus)
        {
            itemImageView.setBackgroundResource(imageResourceIdFocus[imageResourceID]);
            contentTextView.setText(mContext.getResources().getString(R.string.cancel_collect));
        } else
        {
            itemImageView.setBackgroundResource(imageResourceIdUnfocus[imageResourceID]);
            contentTextView.setText(mContext.getResources().getString(R.string.collected));
        }
    }

    private OnHoverListener itemOnHoverListener = new OnHoverListener()
    {

        @Override
        public boolean onHover(View v, MotionEvent event)
        {
            int what = event.getAction();
            SkyLogger.d(TAG, "onHover,event:" + event.toString());
            switch (what)
            {
                case MotionEvent.ACTION_HOVER_ENTER: // 鼠标进入view
                    v.requestFocus();
                    v.requestFocusFromTouch();
                    break;
                case MotionEvent.ACTION_HOVER_MOVE: // 鼠标在view上
                    break;
                case MotionEvent.ACTION_HOVER_EXIT: // 鼠标离开view
                    // v.clearFocus();
                    break;
            }
            return false;
        }
    };

    /**
     * @Fields itemOnFocusChangeListener 菜单中元素焦点变化事件
     */
    OnFocusChangeListener itemOnFocusChangeListener = new OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            SkyLogger.d(TAG,
                    "itemOnFocusChangeListener,hasFocus:" + hasFocus + ",v,id:" + v.getId());
            int viewID = v.getId();
            switch (viewID)
            {
                case BrowserDefine.MENU_VIEW_ID:// 后退
                    updateView(hasFocus, 0);
                    break;
                case BrowserDefine.MENU_VIEW_ID + 1:// 前进
                    updateView(hasFocus, 1);
                    break;
                case BrowserDefine.MENU_VIEW_ID + 2:// 刷新
                    if (isCanRefrush)
                    {
                        updateView(hasFocus, 2);
                    } else
                    {
                        updateView(hasFocus, 10);
                    }
                    break;
                case BrowserDefine.MENU_VIEW_ID + 3:// 收藏网站
                    SkyLogger.d(TAG, "收藏网站,isCollectWeb:" + isCollectWeb);
                    if (isCollectWeb)
                    {
                        updateCollectView(hasFocus, 9);
                    } else
                    {
                        updateView(hasFocus, 3);

                    }
                    break;
                case BrowserDefine.MENU_VIEW_ID + 4:// 收藏夹
                    updateView(hasFocus, 4);
                    break;
                case BrowserDefine.MENU_VIEW_ID + 5:// 多窗口
                    updateView(hasFocus, 5);
                    break;
                case BrowserDefine.MENU_VIEW_ID + 6:// 浏览记录
                    updateView(hasFocus, 6);
                    break;
                case BrowserDefine.MENU_VIEW_ID + 7:// 设置
                    updateView(hasFocus, 7);
                    break;
                case BrowserDefine.MENU_VIEW_ID + 8:// 退出
                    updateView(hasFocus, 8);
                    break;

                default:
                    break;
            }

        }
    };

}
