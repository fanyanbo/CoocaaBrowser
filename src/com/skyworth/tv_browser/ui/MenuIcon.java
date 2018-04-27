/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-6-1         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.ViewPropertyAnimator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.ui.interfaces.MenuIconClickLisenter;
import com.skyworth.tv_browser.util.UIUtil;

public class MenuIcon extends FrameLayout implements OnClickListener, OnHoverListener
{
    private static String TAG = "MenuIcon";
    private Context mContext = null;
    // private LinearLayout mainLayout = null;
    private ImageView iconImageView = null;
    private TextView menuTextView = null;
    private MenuIconClickLisenter mMenuIconClickLisenter = null;

    public MenuIcon(Context context)
    {
        super(context);
        mContext = context;
        initView();
    }

    public void setMenuIconClickLisenter(MenuIconClickLisenter menuIconClickLisenter)
    {
        this.mMenuIconClickLisenter = menuIconClickLisenter;
    }

    private void initView()
    {
        this.setHovered(true);
        this.setClickable(true);
        this.setOnClickListener(this);
        this.setOnHoverListener(this);

        this.setBackgroundResource(R.drawable.menu_icon_bg);

        iconImageView = new ImageView(mContext);
        iconImageView.setImageResource(R.drawable.menu_icon);
        FrameLayout.LayoutParams iconLayoutParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        iconLayoutParams.leftMargin = UIUtil.getResolutionValue(36);
        iconLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        this.addView(iconImageView, iconLayoutParams);

        menuTextView = new TextView(mContext);
        menuTextView.setText(R.string.menu);
        menuTextView.setTextSize(UIUtil.getTextDpiValue(36));
        FrameLayout.LayoutParams menutextParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        menutextParams.leftMargin = UIUtil.getResolutionValue(108);
        menutextParams.gravity = Gravity.CENTER_VERTICAL;
        this.addView(menuTextView, menutextParams);
    }

    @Override
    public boolean onHover(View v, MotionEvent event)
    {
        SkyLogger.d(TAG, "MenuIcon onHover,event:" + event.toString());
        if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER)
        {
            zoomIn();
        } else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT)
        {
            zoomOut();
        }
        return false;
    }

    public void zoomIn()
    {
        SkyLogger.d(TAG, "zoomin, x:" + this.getX() + ",y:" + this.getY());
        float itemx = this.getX();
        float itemy = this.getY();
        ViewPropertyAnimator animate = this.animate();
        animate.x(UIUtil.getResolutionValue(1720)).y(itemy).setDuration(200).setListener(null).start();
    }

    public void zoomOut()
    {
        SkyLogger.d(TAG, "zoomOut, x:" + this.getX() + ",y:" + this.getY());
        float itemx = this.getX();
        float itemy = this.getY();
        ViewPropertyAnimator animate = this.animate();
        animate.x(UIUtil.getResolutionValue(1820)).y(itemy).setDuration(200).setListener(null).start();

    }

    @Override
    public void onClick(View v)
    {
        SkyLogger.d(TAG, "MenuIcon onClick:");
        mMenuIconClickLisenter.onclick();
        // dismiss();
    }

}
