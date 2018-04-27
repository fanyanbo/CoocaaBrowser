/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-19 上午10:33     mikan
 *
 */

package com.skyworth.tv_browser.ui.customView;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.ui.data.HistoryData;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.api.SkyLogo;
import com.skyworth.ui.listview.AdapterItem;
import com.skyworth.ui.listview.MetroListItem;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className HistoryItem
 * @date 16/4/19
 */
public class HistoryItem extends MetroListItem implements AdapterItem<HistoryData>
{
    private static final String TAG = "HistoryItem";

    private Context mContext = null;

    private TextView titleView = null;
    private TextView dataView = null;

    ImageView mBackgroudImage = null;
    /**
     * @Fields mDefaultBackImage 加载的过程中的默认背景
     */
    private ImageView mDefaultBackImage = null;
    private HistoryData mHistoryData = null;

    private int unFocusColor = Color.TRANSPARENT;
    private FrameLayout mShaderLayout = null;

    public HistoryItem(Context context)
    {
        super(context);

        mContext = context;
        initUI();
    }

    private void initUI()
    {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setHovered(true);
        this.setOnHoverListener(itemOnHoverListener);

        final int bottomPadding = UIUtil.getResolutionValue(6);
        this.setPadding(0, 0, 0, bottomPadding);

        mShaderLayout = new FrameLayout(mContext);
        mShaderLayout
                .setBackgroundResource(com.skyworth.commen.ui.R.drawable.ui_sdk_btn_unfocus_big_shadow);
        this.addView(mShaderLayout);

        mBackgroudImage = new ImageView(mContext);

        LayoutParams mBackgroundImageLayoutParams = new LayoutParams(
                UIUtil.getResolutionValue(1300), UIUtil.getResolutionValue(90));
        mShaderLayout.addView(mBackgroudImage, mBackgroundImageLayoutParams);

        // mDefaultBackImage = new ImageView(mContext);
        // mDefaultBackImage.setVisibility(View.GONE);
        // mDefaultBackImage.setBackgroundResource(SkyLogo.getInstence().getLogoRes(false));
        // mShaderLayout.addView(mDefaultBackImage, mBackgroundImageLayoutParams);

        final int TEXT_SIZE = UIUtil.getTextDpiValue(36);
        titleView = new TextView(mContext);
        titleView.setSingleLine();
        titleView.setTextSize(TEXT_SIZE);
        // titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setMarqueeRepeatLimit(-1);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        // titleView.setBackgroundColor(Color.GREEN);

        final int TITLE_VIEW_WIDTH = UIUtil.getResolutionValue(945);
        final int TITLE_VIEW_HEIGHT = UIUtil.getResolutionValue(90);
        LayoutParams mTitleViewLayoutParams = new LayoutParams(TITLE_VIEW_WIDTH, TITLE_VIEW_HEIGHT);
        mTitleViewLayoutParams.leftMargin = UIUtil.getResolutionValue(20);
        mShaderLayout.addView(titleView, mTitleViewLayoutParams);

        dataView = new TextView(mContext);
        dataView.setSingleLine();
        dataView.setTextSize(TEXT_SIZE);
        dataView.setTextColor(getResources().getColor(R.color.unfocus_text_color));
        // dataView.setBackgroundColor(Color.BLUE);
        dataView.setGravity(Gravity.CENTER_VERTICAL);

        final int DATA_VIEW_WIDTH = UIUtil.getResolutionValue(200);
        final int DATA_VIEW_HEIGHT = UIUtil.getResolutionValue(90);
        LayoutParams mDataViLayoutParams = new LayoutParams(DATA_VIEW_WIDTH, DATA_VIEW_HEIGHT);
        mDataViLayoutParams.gravity = Gravity.RIGHT;
        mShaderLayout.addView(dataView, mDataViLayoutParams);
    }

    @Override
    public View getLayout()
    {
        return this;
    }

    @Override
    public void onUpdateItemValue(HistoryData model, int position, int viewType)
    {
        SkyLogger.i(TAG, "onUpdateItemValue");
        mHistoryData = model;
        if (titleView != null)
        {
            titleView.setText(mHistoryData.historyTitle);
            titleView.setVisibility(View.VISIBLE);
        }

        if (dataView != null)
        {
            dataView.setText(mHistoryData.data);
            dataView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void clearItem()
    {
        SkyLogger.i(TAG, "clearItem");

        // if (mDefaultBackImage != null)
        // {
        // mDefaultBackImage.setVisibility(View.VISIBLE);
        // }
        if (titleView != null)
        {
            titleView.setVisibility(View.INVISIBLE);
        }

        if (dataView != null)
        {
            dataView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void refreshView()
    {
        SkyLogger.i(TAG, "refreshView");

        if (mHistoryData != null)
        {
            this.post(new Runnable()
            {
                @Override
                public void run()
                {
                    // if (mDefaultBackImage != null)
                    // {
                    // mDefaultBackImage.setVisibility(View.GONE);
                    // }
                    if (titleView != null)
                    {
                        titleView.setText(mHistoryData.historyTitle);
                        titleView.setVisibility(View.VISIBLE);
                    }

                    if (dataView != null)
                    {
                        dataView.setText(mHistoryData.data);
                        dataView.setVisibility(View.VISIBLE);
                    }
                    SkyLogger.i(TAG, "refreshView: title = " + titleView.getText());
                }
            });
        }

    }

    /**
     * 概述：获得焦点时的相应处理<br/>
     * 适用条件：<br/>
     * 执行流程：<br/>
     * 使用方法：<br/>
     * 注意事项：<br/>
     * 
     * date 16/4/20
     */
    public void focus()
    {
        SkyLogger.i(TAG, "item: focus id:= " + this.getId());
        mBackgroudImage.setBackgroundColor(Color.WHITE);
        titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleView.setSelected(true);
        titleView.setTextColor(getResources().getColor(R.color.focus_text_color));
        dataView.setTextColor(getResources().getColor(R.color.focus_text_color));
    }

    /**
     * 概述：丢失焦点的相应处理<br/>
     * 适用条件：<br/>
     * 执行流程：<br/>
     * 使用方法：<br/>
     * 注意事项：<br/>
     * 
     * date 16/4/20
     */
    public void unfocus()
    {
        SkyLogger.i(TAG, "item: unFocus id:= " + this.getId());
        mBackgroudImage.setBackgroundColor(unFocusColor);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setSelected(false);
        titleView.setTextColor(getResources().getColor(R.color.unfocus_text_color));
        dataView.setTextColor(getResources().getColor(R.color.unfocus_text_color));
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
}
