/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-6-6         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui.nonet;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.ui.interfaces.LordErrorListener;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.blurbg.BlurBgLayout;
import com.skyworth.util.MyFocusFrame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName LordErrorPage
 * @Description 网页加载出错的界面
 * @author wolfboyjiang
 * @date 2016-6-6
 * @version 1.0
 */
public class LordErrorPage extends FrameLayout
{

    private Context mContext = null;
    /**
     * @Fields mBgLayout 主界面背景布局
     */
    private BlurBgLayout mBgLayout = null;
    private ImageView errorImageView = null;
    private TextView errorTextView = null;
    private Button errorButton = null;
    private boolean isNetError = false;
    private LordErrorListener mLordErrorListener = null;
    private MyFocusFrame mMyFocusFrame = null;

    public LordErrorPage(Context context)
    {
        super(context);
        mContext = context;
        initView();
    }

    public void setLordErrorListener(LordErrorListener lordErrorListener)
    {
        mLordErrorListener = lordErrorListener;
    }

    private void initFocusFrame()
    {
        mMyFocusFrame = new MyFocusFrame(mContext, UIUtil.getResolutionValue(83));
        mMyFocusFrame.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_bg);
        // mMyFocusFrame.setAnimDuration(200);
        mMyFocusFrame.setFocusable(false);
        mMyFocusFrame.needAnimtion(true);
        this.addView(mMyFocusFrame);
    }

    @SuppressLint("ResourceAsColor")
    private void initView()
    {
        // add 背景
        mBgLayout = new BlurBgLayout(mContext);
        mBgLayout.setPageType(BlurBgLayout.PAGETYPE.FIRST_PAGE);
        this.addView(mBgLayout, new FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));

        initFocusFrame();

        errorImageView = new ImageView(mContext);
        errorImageView.setImageResource(R.drawable.network_disconnected);
        FrameLayout.LayoutParams errorimagelp = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(160), UIUtil.getResolutionValue(146));
        errorimagelp.topMargin = UIUtil.getResolutionValue(312);
        errorimagelp.gravity = Gravity.CENTER_HORIZONTAL;

        errorTextView = new TextView(mContext);
        errorTextView.setTextColor(R.color.error_text_color);
        errorTextView.setTextSize(UIUtil.getTextDpiValue(42));
        FrameLayout.LayoutParams errorTextViewlp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        errorTextViewlp.topMargin = UIUtil.getResolutionValue(500);
        errorTextViewlp.gravity = Gravity.CENTER_HORIZONTAL;

        errorButton = new Button(mContext);
        errorButton.requestFocus();
        errorButton.setBackgroundColor(Color.TRANSPARENT);
        errorButton.setOnClickListener(errorbuttonClickListener);
        errorButton.setTextColor(Color.BLACK);
        errorButton.setTextSize(UIUtil.getTextDpiValue(36));
        FrameLayout.LayoutParams errorButtonlp = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(362), UIUtil.getResolutionValue(100));
        errorButtonlp.topMargin = UIUtil.getResolutionValue(615);
        errorButtonlp.gravity = Gravity.CENTER_HORIZONTAL;

        errorButton.post(new Runnable()
        {

            @Override
            public void run()
            {
                mMyFocusFrame.initStarPosition(UIUtil.getResolutionValue(702),
                        UIUtil.getResolutionValue(534), UIUtil.getResolutionValue(350 + 83 * 2),
                        UIUtil.getResolutionValue(90 + 83 * 2));
                errorButton.requestFocus();
            }
        });

        this.addView(errorImageView, errorimagelp);
        this.addView(errorTextView, errorTextViewlp);
        this.addView(errorButton, errorButtonlp);
        errorButton.requestFocus();
        // errorButton.setOnKeyListener(new OnKeyListener()
        // {
        //
        // @Override
        // public boolean onKey(View v, int keyCode, KeyEvent event)
        // {
        // SkyLogger.d("errorpage", "onKey:"+event);
        // if (event.getAction() == KeyEvent.ACTION_DOWN)
        // {
        // if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)
        // {
        // mLordErrorListener.onclick(isNetError);
        // }
        // }
        // return false;
        // }
        // });

    }

    public void updateView(boolean isNetError)
    {
        SkyLogger.d("errorpage", "updateView,isNetError:" + isNetError);
        this.isNetError = isNetError;
        if (isNetError)
        {
            errorImageView.setImageResource(R.drawable.network_disconnected);
            errorTextView.setText(R.string.net_error_title);
            errorButton.setText(R.string.set_net_title);

        } else
        {
            errorImageView.setImageResource(R.drawable.network_refresh_error);
            errorTextView.setText(R.string.load_content_error);
            errorButton.setText(R.string.try_refrush);
        }
        errorButton.requestFocus();
        errorButton.requestFocusFromTouch();
    }

    private OnClickListener errorbuttonClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            SkyLogger.d("errorpage", "onClick:");
            mLordErrorListener.onclick(isNetError);
        }
    };

}
