/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-18 下午8:24     mikan
 *
 */

package com.skyworth.tv_browser.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.ui.interfaces.SettingClickListener;
import com.skyworth.tv_browser.util.BrowserDefine;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.customview.SlideFocusView;
import com.skyworth.util.MyFocusFrame;

/**
 * <p>
 * Description: 设置界面ui实现
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className SettingLayout
 * @date 16/4/18
 */
public class SettingLayout extends BaseLayout implements OnClickListener
{
    private static final String TAG = "SettingLayout";
    /**
     * @Fields contentFrameLayout 主布局
     */
    private FrameLayout contentFrameLayout = null;
    /**
     * @Fields iPADUALayout ipad UA布局
     */
    private SettingItemLayout iPADUALayout = null;
    private static final int IPAD_VIEW_ID = 20160421;
    /**
     * @Fields desktopUALayout 桌面UA布局
     */
    private SettingItemLayout desktopUALayout = null;
    private static final int DESKTOP_VIEW_ID = 20160422;
    /**
     * @Fields clearCacheLayout 清楚cache布局
     */
    private SettingItemLayout clearCacheLayout = null;
    private static final int CLEAR_CHCHE_VIEW_ID = 20160423;
    /**
     * @Fields clearHistoryLayout 清楚历史布局
     */
    private SettingItemLayout clearHistoryLayout = null;
    private static final int CLEAR_HISTORY_VIEW_ID = 20160424;

    private String mUAName = "";

    private SettingClickListener settingClickListener = null;
    private MyFocusFrame mMyFocusFrame = null;

    public SettingLayout(Context context, String uAName)
    {
        super(context);
        SkyLogger.d(TAG, "SettingLayout,uAName:" + uAName);
        if (uAName != null && !"".equals(uAName))
        {
            mUAName = uAName;
        } else
        {
            mUAName = BrowserDefine.IPAD_USERAGENT;
        }

        initUI();
    }

    @Override
    protected int getTitleIconResId()
    {
        return R.drawable.setting_icon;
    }

    @Override
    protected String getTitleText()
    {
        return mContext.getString(R.string.setting_title);
    }

    @SuppressLint({ "ResourceAsColor", "DefaultLocale" })
    @Override
    protected View getContentView(SlideFocusView slideFocusView)
    {
        SkyLogger.d(TAG, "getContentView,,uAName:" + mUAName);
        contentFrameLayout = new FrameLayout(mContext);
        initFocusFrame();
        /**
         * @Fields uASettingTextView UA设置元素标题
         */
        TextView uASettingTextView = new TextView(mContext);
        uASettingTextView.setText(R.string.ua_setting_tip);
        uASettingTextView.setTextSize(UIUtil.getTextDpiValue(36));
        uASettingTextView.setTextColor(R.color.menu_item_word_5b5b5b);
        FrameLayout.LayoutParams uASettingLayoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        uASettingLayoutParams.leftMargin = UIUtil.getResolutionValue(512);
        uASettingLayoutParams.topMargin = UIUtil.getResolutionValue(155 + 95);

        if (mUAName.toLowerCase().contains("ipad"))
        {
            iPADUALayout = new SettingItemLayout(mContext, IPAD_VIEW_ID,
                    R.drawable.setting_selecet_icon, R.string.ipad_ua);
            iPADUALayout.requestFocus();
            iPADUALayout.post(new Runnable()
            {

                @Override
                public void run()
                {
                    mMyFocusFrame.initStarPosition(UIUtil.getResolutionValue(627),
                            UIUtil.getResolutionValue(147),
                            UIUtil.getResolutionValue(500 + 83 * 2),
                            UIUtil.getResolutionValue(90 + 83 * 2));

                }
            });
            SkyLogger.d(TAG, "SettingLayout,IPAD_USERAGENT true:");
        } else
        {
            iPADUALayout = new SettingItemLayout(mContext, IPAD_VIEW_ID,
                    R.drawable.setting_unselect_icon, R.string.ipad_ua);
            SkyLogger.d(TAG, "SettingLayout,IPAD_USERAGENT false:");
        }
        iPADUALayout.setMyFocusFrame(mMyFocusFrame);
        iPADUALayout.setOnClickListener(this);
        FrameLayout.LayoutParams iPADUALayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(500), UIUtil.getResolutionValue(90));
        iPADUALayoutParams.setMargins(UIUtil.getResolutionValue(710),
                UIUtil.getResolutionValue(155 + 75), 0, 0);

        if (mUAName.toLowerCase().contains("windows"))
        {
            desktopUALayout = new SettingItemLayout(mContext, DESKTOP_VIEW_ID,
                    R.drawable.setting_selecet_icon, R.string.desktop_ua);
            SkyLogger.d(TAG, "SettingLayout,ANDROID_USERAGENT true:");
            desktopUALayout.requestFocus();
            desktopUALayout.post(new Runnable()
            {

                @Override
                public void run()
                {
                    mMyFocusFrame.initStarPosition(UIUtil.getResolutionValue(627),
                            UIUtil.getResolutionValue(282),
                            UIUtil.getResolutionValue(500 + 83 * 2),
                            UIUtil.getResolutionValue(90 + 83 * 2));

                }
            });
        } else
        {
            desktopUALayout = new SettingItemLayout(mContext, DESKTOP_VIEW_ID,
                    R.drawable.setting_unselect_icon, R.string.desktop_ua);
            SkyLogger.d(TAG, "SettingLayout,ANDROID_USERAGENT false:");
        }
        desktopUALayout.setMyFocusFrame(mMyFocusFrame);
        desktopUALayout.setOnClickListener(this);
        // desktopUALayout.setBackgroundColor(Color.RED);
        FrameLayout.LayoutParams desktopUALayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(500), UIUtil.getResolutionValue(90));
        desktopUALayoutParams.setMargins(UIUtil.getResolutionValue(710),
                UIUtil.getResolutionValue(210 + 155), 0, 0);

        TextView clearCacheTipTextView = new TextView(mContext);
        clearCacheTipTextView.setText(R.string.clear_cache_tip);
        clearCacheTipTextView.setTextSize(UIUtil.getTextDpiValue(36));
        clearCacheTipTextView.setTextColor(R.color.menu_item_word_5b5b5b);
        FrameLayout.LayoutParams clearCacheTipLayoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        clearCacheTipLayoutParams.leftMargin = UIUtil.getResolutionValue(485);
        clearCacheTipLayoutParams.topMargin = UIUtil.getResolutionValue(425 + 155);

        clearCacheLayout = new SettingItemLayout(mContext, CLEAR_CHCHE_VIEW_ID,
                R.string.clear_cache);
        clearCacheLayout.setMyFocusFrame(mMyFocusFrame);
        clearCacheLayout.setOnClickListener(this);
        // clearCacheLayout.setBackgroundColor(Color.RED);
        FrameLayout.LayoutParams clearCacheLayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(500), UIUtil.getResolutionValue(90));
        clearCacheLayoutParams.setMargins(UIUtil.getResolutionValue(710),
                UIUtil.getResolutionValue(405 + 155), 0, 0);

        TextView clearCacheFileTextView = new TextView(mContext);
        clearCacheFileTextView.setText(R.string.clear_cache_small_tip);
        clearCacheFileTextView.setTextSize(UIUtil.getTextDpiValue(28));
        clearCacheFileTextView.setTextColor(R.color.huise_999999);
        FrameLayout.LayoutParams clearCacheFileTextViewLayoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        clearCacheFileTextViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        clearCacheFileTextViewLayoutParams.topMargin = UIUtil.getResolutionValue(505 + 155);

        clearHistoryLayout = new SettingItemLayout(mContext, CLEAR_HISTORY_VIEW_ID,
                R.string.clear_history);
        clearHistoryLayout.setMyFocusFrame(mMyFocusFrame);
        clearHistoryLayout.setOnClickListener(this);
        // clearHistoryLayout.setBackgroundColor(Color.RED);
        FrameLayout.LayoutParams clearHistoryLayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(500), UIUtil.getResolutionValue(90));
        clearHistoryLayoutParams.setMargins(UIUtil.getResolutionValue(710),
                UIUtil.getResolutionValue(555 + 155), 0, 0);

        TextView clearHistoryFileTextView = new TextView(mContext);
        clearHistoryFileTextView.setText(R.string.clear_cache_small_tip);
        clearHistoryFileTextView.setTextSize(UIUtil.getTextDpiValue(28));
        clearHistoryFileTextView.setTextColor(R.color.huise_999999);
        FrameLayout.LayoutParams clearHistoryFileTextViewLayoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        clearHistoryFileTextViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        clearHistoryFileTextViewLayoutParams.topMargin = UIUtil.getResolutionValue(655 + 155);

        contentFrameLayout.addView(uASettingTextView, uASettingLayoutParams);
        contentFrameLayout.addView(iPADUALayout, iPADUALayoutParams);
        contentFrameLayout.addView(desktopUALayout, desktopUALayoutParams);
        contentFrameLayout.addView(clearCacheTipTextView, clearCacheTipLayoutParams);
        contentFrameLayout.addView(clearCacheLayout, clearCacheLayoutParams);
        contentFrameLayout.addView(clearCacheFileTextView, clearCacheFileTextViewLayoutParams);
        contentFrameLayout.addView(clearHistoryLayout, clearHistoryLayoutParams);
        contentFrameLayout.addView(clearHistoryFileTextView, clearHistoryFileTextViewLayoutParams);
        return contentFrameLayout;
    }

    public void setSettingClickListener(SettingClickListener settingClickListener)
    {
        this.settingClickListener = settingClickListener;
    }

    private void initFocusFrame()
    {
        mMyFocusFrame = new MyFocusFrame(mContext, UIUtil.getResolutionValue(83));
        mMyFocusFrame.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_bg);
        // mMyFocusFrame.setAnimDuration(200);
        mMyFocusFrame.setFocusable(false);
        mMyFocusFrame.needAnimtion(true);
        contentFrameLayout.addView(mMyFocusFrame);
    }

    @Override
    protected LayoutParams getContentViewLayoutParams()
    {
        LayoutParams contentViewParams = new LayoutParams(UIUtil.getResolutionValue(1920),
                UIUtil.getResolutionValue(1080));
        // contentViewParams.topMargin = UIUtil.getResolutionValue(285);
        // contentViewParams.leftMargin = UIUtil.getResolutionValue(230);
        return contentViewParams;
    }

    @Override
    public void onClick(View v)
    {
        SkyLogger.i(TAG, "onClick,viewId:" + v.getId());
        v.requestFocus();
        switch (v.getId())
        {
            case IPAD_VIEW_ID:// 选择ipadUA
                if (v instanceof SettingItemLayout)
                {
                    SettingItemLayout itemLayout = (SettingItemLayout) v;
                    itemLayout.setUASelect(true);
                }
                desktopUALayout.setUASelect(false);
                settingClickListener.setUA(BrowserDefine.IPAD_USERAGENT);
                break;
            case DESKTOP_VIEW_ID:// 选择桌面UA
                if (v instanceof SettingItemLayout)
                {
                    SettingItemLayout itemLayout = (SettingItemLayout) v;
                    itemLayout.setUASelect(true);
                }
                iPADUALayout.setUASelect(false);
                settingClickListener.setUA(BrowserDefine.IE9_USERAGENT);
                break;
            case CLEAR_CHCHE_VIEW_ID:// 清除缓存
                settingClickListener.clearCache();
                break;
            case CLEAR_HISTORY_VIEW_ID:// 清楚历史痕迹
                settingClickListener.clearHistory();
                break;

            default:
                break;
        }
    }

}
