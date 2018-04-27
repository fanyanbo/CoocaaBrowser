/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-21         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.util.MyFocusFrame;

/**
 * @ClassName SettingItemLayout
 * @Description 创建一个设置的子元素控件
 * @author wolfboyjiang
 * @date 2016-4-22
 * @version 1
 */
public class SettingItemLayout extends FrameLayout implements OnFocusChangeListener
{
    private static final String TAG = "SettingLayout";
    private Context mContext = null;
    private ImageView UAImage = null;
    private TextView UATextView = null;
    private boolean hasImage = false;
    private static final int IPAD_VIEW_ID = 20160421;
    private static final int DESKTOP_VIEW_ID = 20160422;
    private static final int CLEAR_CHCHE_VIEW_ID = 20160423;
    private static final int CLEAR_HISTORY_VIEW_ID = 20160424;
    private MyFocusFrame mMyFocusFrame = null;

    /**
     * @Description 构造一个设置子元素，有一个图片也有一个文字
     * @param context
     *            上下文
     * @param viewId
     *            控件id
     * @param imageSourceId
     *            图片的资源id
     * @param textSourceId
     *            文字提示的资源id
     */
    public SettingItemLayout(Context context, int viewId, int textSourceId)
    {
        super(context);
        mContext = context;
        this.setId(viewId);
        hasImage = false;
        this.setFocusable(true);
        this.setHovered(true);
        this.setOnHoverListener(itemOnHoverListener);
        this.setOnFocusChangeListener(this);
        this.setBackgroundResource(com.skyworth.commen.ui.R.drawable.ui_sdk_btn_unfocus_small_shadow);

        hasImage = false;
        initView(textSourceId);
    }

    /**
     * @Description 构造一个设置子元素,只有中间的文字
     * @param context
     *            上下文
     * @param viewId
     *            控件id
     * @param textSourceId
     *            文字提示的资源id
     */
    public SettingItemLayout(Context context, int viewId, int imageSourceId, int textSourceId)
    {
        super(context);
        mContext = context;
        this.setId(viewId);
        hasImage = true;
        this.setFocusable(true);
        this.setHovered(true);
        this.setOnHoverListener(itemOnHoverListener);
        this.setOnFocusChangeListener(this);
        this.setBackgroundResource(com.skyworth.commen.ui.R.drawable.ui_sdk_btn_unfocus_small_shadow);

        initView(imageSourceId, textSourceId);
    }

    @SuppressLint("ResourceAsColor")
    private void initView(int imageSourceId, int textSourceId)
    {
        UAImage = new ImageView(mContext);
        UAImage.setImageDrawable(mContext.getResources().getDrawable(imageSourceId));
        FrameLayout.LayoutParams UAImageLayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(37), UIUtil.getResolutionValue(37));
        UAImageLayoutParams.leftMargin = UIUtil.getResolutionValue(150);
        UAImageLayoutParams.gravity = Gravity.CENTER_VERTICAL;

        this.addView(UAImage, UAImageLayoutParams);

        UATextView = new TextView(mContext);
        UATextView.setText(mContext.getResources().getString(textSourceId));
        UATextView.setTextSize(UIUtil.getTextDpiValue(36));
        UATextView.setTextColor(R.color.menu_item_word_5b5b5b);
        FrameLayout.LayoutParams UATextViewLayoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        UATextViewLayoutParams.leftMargin = UIUtil.getResolutionValue(205);
        UATextViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;

        this.addView(UATextView, UATextViewLayoutParams);
    }

    @SuppressLint("ResourceAsColor")
    private void initView(int textSourceId)
    {
        UATextView = new TextView(mContext);
        
        UATextView.setText(mContext.getResources().getString(textSourceId));
        UATextView.setTextSize(UIUtil.getTextDpiValue(36));
        UATextView.setTextColor(R.color.menu_item_word_5b5b5b);
        FrameLayout.LayoutParams UATextViewLayoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        UATextViewLayoutParams.gravity = Gravity.CENTER;

        this.addView(UATextView, UATextViewLayoutParams);
    }

    public void setMyFocusFrame(MyFocusFrame mMyFocusFrame)
    {
        this.mMyFocusFrame = mMyFocusFrame;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        SkyLogger.i(TAG, "onFocusChange,viewId:" + v.getId() + ",hasFocus:" + hasFocus);
        if (hasFocus)
        {
            v.setBackgroundColor(Color.WHITE);
            UATextView.setTextColor(R.color.ff000000);
            setContentTextColor(v.getId(), hasFocus);
            if (mMyFocusFrame != null)
            {
                mMyFocusFrame.changeFocusPos(v);
            }
        } else
        {
            v.setBackgroundResource(com.skyworth.commen.ui.R.drawable.ui_sdk_btn_unfocus_small_shadow);
            UATextView.setTextColor(R.color.menu_item_word_5b5b5b);
            setContentTextColor(v.getId(), hasFocus);
        }
    }

    public void setUASelect(boolean hasChoiced)
    {
        if (hasChoiced)
        {
            UAImage.setImageResource(R.drawable.setting_selecet_icon);
        } else
        {
            UAImage.setImageResource(R.drawable.setting_unselect_icon);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setContentTextColor(int viewId, boolean hasFocus)
    {
        SkyLogger.i(TAG, "viewId:" + viewId + ",hasFocus:" + hasFocus);
        if (hasFocus)
        {

            UATextView.setTextColor(R.color.ff000000);
        } else
        {

            UATextView.setTextColor(R.color.menu_item_word_5b5b5b);
        }

        if (hasFocus)
        {
            UATextView.setTextColor(R.color.ff000000);
        } else
        {
            UATextView.setTextColor(R.color.menu_item_word_5b5b5b);
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
                    break;
                case MotionEvent.ACTION_HOVER_MOVE: // 鼠标在view上
                    break;
                case MotionEvent.ACTION_HOVER_EXIT: // 鼠标离开view
//                    v.clearFocus();
                    break;
            }
            return false;
        }
    };
}
