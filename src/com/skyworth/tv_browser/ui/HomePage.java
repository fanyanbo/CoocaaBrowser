/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-6-14         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.skyworth.framework.skysdk.android.SkySystemUtil;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.ui.interfaces.HomepageListener;
import com.skyworth.tv_browser.ui.interfaces.SearchClickListener;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.blurbg.BlurBgLayout;
import com.skyworth.util.MyFocusFrame;

public class HomePage extends FrameLayout
{

    private Context mContext;
    private static final String TAG = "HomePage";
    private SearchLayout searchLayout = null;
    private HomepageListener mHomepageListener = null;
    private BlurBgLayout mBgLayout;
    private MyFocusFrame mMyFocusFrame = null;

    public HomePage(Context context)
    {
        super(context);
        mContext = context;
        initView();
    }

    public void setHomepageListener(HomepageListener homepageListener)
    {
        this.mHomepageListener = homepageListener;
    }

    private void initFocusFrame()
    {
        mMyFocusFrame = new MyFocusFrame(mContext, UIUtil.getResolutionValue(31));
        mMyFocusFrame.setImgResourse(R.drawable.sky_focus_bg);
        // mMyFocusFrame.setAnimDuration(200);
        mMyFocusFrame.setFocusable(false);
        mMyFocusFrame.bringToFront();
        mMyFocusFrame.needAnimtion(true);
        this.addView(mMyFocusFrame);
    }

    public void setFocusVisible(boolean visible)
    {
        if (mMyFocusFrame != null)
        {
            if (visible)
            {
                mMyFocusFrame.setVisibility(View.VISIBLE);
                searchLayout.getFocus();
            } else
            {
                mMyFocusFrame.setVisibility(View.INVISIBLE);
            }

        }
    }

    private void initView()
    {
        // add 背景
        mBgLayout = new BlurBgLayout(mContext);
        mBgLayout.setPageType(BlurBgLayout.PAGETYPE.FIRST_PAGE);
        this.addView(mBgLayout, new FrameLayout.LayoutParams(UIUtil.getResolutionValue(1920),
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
        initFocusFrame();

        searchLayout = new SearchLayout(mContext, SkySystemUtil.isOverseasProduct());
        searchLayout.setSearchClickListener(searchClickListener);
        searchLayout.setMyFocusFrame(mMyFocusFrame);

        FrameLayout.LayoutParams searchParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(1920), UIUtil.getResolutionValue(90));
        searchParams.topMargin = UIUtil.getResolutionValue(393);
        searchParams.gravity = Gravity.CENTER_HORIZONTAL;

        this.addView(searchLayout, searchParams);

        this.post(new Runnable()
        {

            @Override
            public void run()
            {
                mMyFocusFrame.initStarPosition(UIUtil.getResolutionValue(525 - 31),
                        UIUtil.getResolutionValue(393 - 31), UIUtil.getResolutionValue(900 + 62),
                        UIUtil.getResolutionValue(90 + 62));

            }
        });
    }

    public void getFocus()
    {
        searchLayout.getFocus();
    }

    private SearchClickListener searchClickListener = new SearchClickListener()
    {

        @Override
        public void onTouch(String content)
        {
            mHomepageListener.onSearchTouch(content);

        }
    };
}
