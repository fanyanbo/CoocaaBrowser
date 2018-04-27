/**
 * Copyright (C) 2012 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *           2015年4月7日         wen
 *
 */

package com.skyworth.tv_browser.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.util.UIUtil;

/**
 * @ClassName ProgressBarLayout
 * @Description 浏览器加载进度条
 * @author wen
 * @date 2016年5月3日
 * @version V1.0
 */
public class ProgressBarLayout extends FrameLayout
{
    private Context context;
    private ImageView progressImg;
    private final int IMAGE_WIDTH;

    /**
     * @Description 升级下载进度条
     * @param context
     * @param viewType
     *            当前升级阶段
     * @param isRepair
     *            是否修复升级，修复升级全屏显示，普通升级半屏显示
     */
    public ProgressBarLayout(Context context)
    {
        super(context);
        this.context = context;
        IMAGE_WIDTH = UIUtil.getResolutionValue(1920);
        initView();
    }

    private void initView()
    {
        setFocusable(false);
        setBackgroundResource(R.drawable.progress_bg);

        final int IMAGE_FG_HEIGTH = UIUtil.getResolutionValue(12);
        progressImg = new ImageView(context);
        setFocusable(false);
        progressImg.setBackgroundResource(R.drawable.progress_fg);
        FrameLayout.LayoutParams propressImgLp = new FrameLayout.LayoutParams(0, IMAGE_FG_HEIGTH);
        propressImgLp.gravity = Gravity.START | Gravity.TOP;
        progressImg.setLayoutParams(propressImgLp);
        addView(progressImg);
    }

    /**
     * @Description 根据页面加载进度更新进度条View<br/>
     * @param percent
     *            加载进度百分比，0到100
     * @date 2016年5月3日
     */
    public void updatePercent(int percent)
    {
        // if (percent >= 0 && percent <= 100)
        if (percent > 0 && percent < 100)
        {
            setPercent(percent);
            setVisible(VISIBLE);
        } else
        {
            setVisible(GONE);
        }
    }

    private void setVisible(int tmpVisible)
    {
        if (getVisibility() != tmpVisible)
        {
            setVisibility(tmpVisible);
        }
    }

    /**
     * @Description 根据下载百分比，刷新进度条及下载进度<br/>
     * @param precent
     *            void
     * @date 2015年4月7日
     */
    private void setPercent(int percent)
    {
        // Log.d(TAG, "percent:=" + percent);
        FrameLayout.LayoutParams updateLP = (FrameLayout.LayoutParams) progressImg
                .getLayoutParams();
        updateLP.width = (percent * IMAGE_WIDTH / 100);
        progressImg.setLayoutParams(updateLP);
    }
}
