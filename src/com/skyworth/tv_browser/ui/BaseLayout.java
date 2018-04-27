/**
 * Copyright (C) 2016 The SkyTvOS Project
 * <p/>
 * Version     Date           Author
 * ─────────────────────────────────────
 * 1.0       2016-4-16         wolfboyjiang
 */

package com.skyworth.tv_browser.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.ui.customView.TitleBar;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.blurbg.BlurBgLayout;
import com.skyworth.ui.customview.SlideFocusView;

/**
 * <p>
 * Description:界面基类
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className BaseLayout
 * @date 16/4/16
 */
public abstract class BaseLayout extends SlideFocusView
{
    private static final String TAG = "BaseLayout";
    protected Context mContext = null;

    private BlurBgLayout mBgLayout = null;
    private TitleBar mTitleBar = null;
	private View mContentView = null;

    public BaseLayout(Context context)
    {
        super(context, com.skyworth.commen.ui.R.drawable.ui_sdk_btn_focus_shadow_no_bg);

        mContext = context;
//        initUI();
    }

	/**
	 * 概述：初始化界面UI<br/>
	 * 适用条件：<br/>
	 * 执行流程：<br/>
	 * 使用方法：<br/>
	 * 注意事项：<br/>
	 *
	 * date 16/4/26
	 */
    public void initUI()
    {
        LayoutParams baseLayoutParams = new LayoutParams(UIUtil.getResolutionValue(1920),
                UIUtil.getResolutionValue(1080));
        this.setLayoutParams(baseLayoutParams);

        mBgLayout = new BlurBgLayout(mContext);
        mBgLayout.setPageType(BlurBgLayout.PAGETYPE.FIRST_PAGE);
        this.addView(mBgLayout, new RelativeLayout.LayoutParams(UIUtil.getResolutionValue(1920),
                UIUtil.getResolutionValue(1080)));

        addTitleBar();

        mContentView = getContentView(this);
        if (mContentView == null)
        {
            SkyLogger.e(TAG, "initUI: mContentView = null");
            return;
        }

        addView(mContentView, getContentViewLayoutParams());

        getFocusView().bringToFront();
    }

    private void addTitleBar()
    {
        mTitleBar = new TitleBar(mContext);
        mTitleBar.initUI(getTitleIconResId(), getTitleText());

        LayoutParams mTitleBarLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mTitleBarLayoutParams.topMargin = UIUtil.getResolutionValue(100);
        mTitleBarLayoutParams.leftMargin = UIUtil.getResolutionValue(100);
        this.addView(mTitleBar, mTitleBarLayoutParams);
    }

    /**
     * 概述：获取标题图标资源ID<br/>
     * 适用条件：<br/>
     * 执行流程：<br/>
     * 使用方法：<br/>
     * 注意事项：<br/>
     * 
     * @return 资源id date 16/4/16
     */
    protected abstract int getTitleIconResId();

    /**
     * 概述：获取标题内容<br/>
     * 适用条件：<br/>
     * 执行流程：<br/>
     * 使用方法：<br/>
     * 注意事项：<br/>
     * 
     * @return 标题 date 16/4/16
     */
    protected abstract String getTitleText();

    /**
     * 概述：获取具体内容view<br/>
     * 适用条件：<br/>
     * 执行流程：<br/>
     * 使用方法：<br/>
     * 注意事项：<br/>
     * 
     * @return 具体内容view date 16/4/16
     */
    protected abstract View getContentView(SlideFocusView slideFocusView);

    protected LayoutParams getContentViewLayoutParams()
    {
        LayoutParams contentViewParams = new LayoutParams(UIUtil.getResolutionValue(1920),
                UIUtil.getResolutionValue(925));
        contentViewParams.topMargin = UIUtil.getResolutionValue(155);
        return contentViewParams;
    }

	/**
	 * 概述：刷新当前窗口content内容<br/>
	 * 适用条件：<br/>
	 * 执行流程：<br/>
	 * 使用方法：<br/>
	 * 注意事项：<br/>
	 *
	 * date 16/5/5
	 */
	protected void refreshContentView()
	{
		this.removeView(mContentView);
		mContentView = getContentView(this);
		addView(mContentView, getContentViewLayoutParams());
	}
}
