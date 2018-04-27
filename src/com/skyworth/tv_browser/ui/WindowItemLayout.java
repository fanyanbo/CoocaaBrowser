/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-20         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.util.UIUtil;

/**
 * @ClassName WindowItemLayout
 * @Description 多窗口每一个元素的布局
 * @author wolfboyjiang
 * @date 2016-4-20
 * @version 1
 */
public class WindowItemLayout extends LinearLayout
{

    private Context mContext = null;
    /**
     * @Fields webImageView 网页的图片
     */
    private ImageView webImageView = null;
    private LinearLayout titleLinearLayout = null;

    /**
     * @Fields deleteImageView 删除按钮元素
     */
    private ImageView deleteImageView = null;
    /**
     * @Fields webTitleTextView 网页的title文字
     */
    private TextView webTitleTextView = null;

    /**
     * @Description 构造一个元素对象
     * @param context
     *            上下文
     * @param webImageBitmap
     *            窗口显示的图片
     * @param webTitle
     *            窗口显示的网站标题
     */
    public WindowItemLayout(Context context, Bitmap webImageBitmap, String webTitle)
    {
        super(context);
        mContext = context;
        this.setOrientation(LinearLayout.VERTICAL);
        initView(webImageBitmap, webTitle);
    }

    @SuppressLint("ResourceAsColor")
    private void initView(Bitmap webImageBitmap, String webTitle)
    {
        webImageView = new ImageView(mContext);
        webImageView.setImageBitmap(webImageBitmap);
        webImageView.setFocusable(true);
        LinearLayout.LayoutParams webImageLayoutParams = new LayoutParams(
                UIUtil.getResolutionValue(500), UIUtil.getResolutionValue(280));

        titleLinearLayout = new LinearLayout(mContext);
        titleLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams titleLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                UIUtil.getResolutionValue(66));
        webTitleTextView = new TextView(mContext);
        webTitleTextView.setText(webTitle);
        webTitleTextView.setTextSize(UIUtil.getTextDpiValue(36));
        webTitleTextView.setTextColor(R.color.menu_item_word_5b5b5b);
        LinearLayout.LayoutParams webTitleLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        deleteImageView = new ImageView(mContext);
        deleteImageView.setFocusable(true);
        deleteImageView.setImageResource(R.drawable.window_item_close_unfocus);
        LinearLayout.LayoutParams delImageLayoutParams = new LayoutParams(
                UIUtil.getResolutionValue(66), UIUtil.getResolutionValue(66));

        this.addView(webImageView, webImageLayoutParams);
        titleLinearLayout.addView(webTitleTextView, webTitleLayoutParams);
        titleLinearLayout.addView(deleteImageView, delImageLayoutParams);
        this.addView(titleLinearLayout, titleLayoutParams);
    }

    /**
     * @Description 更新<br/>
     * @param hasFocus
     * @param webImageBitmap
     * @param webTitle
     * @date 2016-4-20
     */
    public void updateView(Boolean hasFocus, Bitmap webImageBitmap, String webTitle)
    {

    }

}
