/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-20         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

/**
 * @ClassName WindowItemLayout
 * @Description 多窗口每一个元素的布局
 * @author wolfboyjiang
 * @date 2016-4-20
 * @version 1
 */
public class MultiWindowsItemImageView extends ImageView
{

    private Context mContext = null;

    /**
     * @Description 构造一个元素对象
     * @param context
     *            上下文
     * @param webImageBitmap
     *            窗口显示的图片
     * @param webTitle
     *            窗口显示的网站标题
     */
    public MultiWindowsItemImageView(Context context, Bitmap webImageBitmap)
    {
        super(context);
        mContext = context;
        this.setFocusable(true);
        this.setHovered(true);
        this.setClickable(true);
        this.setScaleType(ScaleType.FIT_CENTER);
        if (webImageBitmap != null)
        {
            this.setImageBitmap(webImageBitmap);
        } else
        {
            this.setBackgroundColor(Color.WHITE);
        }
       
    }

}
