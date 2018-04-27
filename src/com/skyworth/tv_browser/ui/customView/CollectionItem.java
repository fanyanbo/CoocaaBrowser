/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-21 下午4:02     mikan
 *
 */

package com.skyworth.tv_browser.ui.customView;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnHoverListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.ui.data.CollectionData;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.api.SkyLogo;
import com.skyworth.ui.customview.SlideFocusView;
import com.skyworth.ui.listview.AdapterItem;
import com.skyworth.ui.listview.MetroListItem;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className CollectionItem
 * @date 16/4/21
 */
public class CollectionItem extends MetroListItem implements AdapterItem<CollectionData>
{
    private static final String TAG = "CollectionItem";

    private Context mContext = null;

    private CollectionData mCollectionData = null;

    private ImageView mIconView = null;
    private TextView mTitleView = null;
    private TextView mCancelCollectView = null;

    ImageView mBackgroundImage = null;
    /**
     * @Fields mDefaultBackImage 加载的过程中的默认背景
     */
    private ImageView mDefaultBackImage = null;
    private int unFocusColor = Color.TRANSPARENT;
    public FrameLayout mShaderLayout = null;

    public FrameLayout mCancelCollFrameLayout = null;
    ImageView cancelCollectBackground = null;

    private SlideFocusView mSlideFocusView = null;

    public CollectionItem(Context context)
    {
        super(context);
        mContext = context;

        initUI();
    }

    public void setSlideFocusView(SlideFocusView slideFocusView)
    {
        mSlideFocusView = slideFocusView;
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

        mBackgroundImage = new ImageView(mContext);

        LayoutParams mBackgroundImageLayoutParams = new LayoutParams(
                UIUtil.getResolutionValue(1200), UIUtil.getResolutionValue(90));
        mShaderLayout.addView(mBackgroundImage, mBackgroundImageLayoutParams);

        // mDefaultBackImage = new ImageView(mContext);
        // mDefaultBackImage.setVisibility(View.GONE);
        // mDefaultBackImage.setBackgroundResource(SkyLogo.getInstence().getLogoRes(false));
        // mShaderLayout.addView(mDefaultBackImage, mBackgroundImageLayoutParams);

        mIconView = new ImageView(mContext);
        mIconView.setImageResource(R.drawable.collect_web_defalut_icon);
        LayoutParams mIconLayoutParams = new LayoutParams(UIUtil.getResolutionValue(60),
                UIUtil.getResolutionValue(60));
        mIconLayoutParams.leftMargin = UIUtil.getResolutionValue(15);
        mIconLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        mShaderLayout.addView(mIconView, mIconLayoutParams);

        final int TEXT_SIZE = UIUtil.getTextDpiValue(36);
        mTitleView = new TextView(mContext);
        mTitleView.setSingleLine();
        mTitleView.setTextSize(TEXT_SIZE);
        // mTitleView.setEllipsize(TextUtils.TruncateAt.END);
        mTitleView.setMarqueeRepeatLimit(-1);
        mTitleView.setGravity(Gravity.CENTER_VERTICAL);
        // titleView.setBackgroundColor(Color.GREEN);

        final int TITLE_VIEW_WIDTH = UIUtil.getResolutionValue(1065);
        final int TITLE_VIEW_HEIGHT = UIUtil.getResolutionValue(90);
        LayoutParams mTitleViewLayoutParams = new LayoutParams(TITLE_VIEW_WIDTH, TITLE_VIEW_HEIGHT);
        mTitleViewLayoutParams.leftMargin = UIUtil.getResolutionValue(90);
        mShaderLayout.addView(mTitleView, mTitleViewLayoutParams);

        mCancelCollFrameLayout = new FrameLayout(mContext);
        mCancelCollFrameLayout.setHovered(true);
        // mCancelCollFrameLayout.setOnHoverListener(cancleOnHoverListener);
        mCancelCollFrameLayout
                .setBackgroundResource(com.skyworth.commen.ui.R.drawable.ui_sdk_btn_unfocus_big_shadow);

        LayoutParams cancelCollLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelCollLayoutParams.leftMargin = UIUtil.getResolutionValue(1260);
        this.addView(mCancelCollFrameLayout, cancelCollLayoutParams);

        cancelCollectBackground = new ImageView(mContext);
        // cancelCollectBackground.setBackgroundColor(Color.RED);
        LayoutParams cancelCollectBackgroundLayoutParams = new LayoutParams(
                UIUtil.getResolutionValue(200), UIUtil.getResolutionValue(90));
        cancelCollectBackgroundLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        mCancelCollFrameLayout
                .addView(cancelCollectBackground, cancelCollectBackgroundLayoutParams);

        mCancelCollectView = new TextView(mContext);
        mCancelCollectView.setSingleLine();
        mCancelCollectView.setTextSize(TEXT_SIZE);
        mCancelCollectView.setEllipsize(TextUtils.TruncateAt.END);
        mCancelCollectView.setMarqueeRepeatLimit(-1);
        mCancelCollectView.setGravity(Gravity.CENTER);
        mCancelCollectView.setText(mContext.getString(R.string.cancel_collect));
        // mCancelCollectView.setBackgroundColor(Color.BLUE);

        LayoutParams cancelCollectViewLayoutParams = new LayoutParams(
                UIUtil.getResolutionValue(200), UIUtil.getResolutionValue(90));
        mCancelCollFrameLayout.addView(mCancelCollectView, cancelCollectViewLayoutParams);

        mCancelCollFrameLayout.setVisibility(INVISIBLE);
    }

    @Override
    public View getLayout()
    {
        return this;
    }

    @Override
    public void onUpdateItemValue(CollectionData model, int position, int viewType)
    {
        SkyLogger.i(TAG, "onUpdateItemValue");
        mCollectionData = model;
        if (mTitleView != null)
        {
            mTitleView.setText(mCollectionData.collectionTitle);
            mTitleView.setVisibility(VISIBLE);

            SkyLogger.i(TAG, "run: title = " + mCollectionData.collectionTitle);
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
        if (mTitleView != null)
        {
            mTitleView.setVisibility(INVISIBLE);
        }

        if (mIconView != null)
        {
            // mIconView.setBackgroundColor(Color.TRANSPARENT);
            mIconView.setImageResource(R.drawable.collect_web_defalut_icon);
        }
    }

    @Override
    public void refreshView()
    {
        SkyLogger.i(TAG, "refreshView");

        if (mCollectionData != null)
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
                    if (mIconView != null)
                    {
                        // mIconView.setBackgroundColor(Color.BLUE);

                        File iconFile = new File(mCollectionData.iconPath);
                        if (iconFile.exists())
                        {
                            BitmapFactory.Options opt = new BitmapFactory.Options();
                            // opt.inPreferredConfig = Bitmap.Config.RGB_565;
                            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            opt.inPurgeable = true;
                            opt.inInputShareable = true;
                            Bitmap bitmap = BitmapFactory.decodeFile(mCollectionData.iconPath, opt);

                            mIconView.setImageBitmap(bitmap);
                            mIconView.setVisibility(VISIBLE);
                        } else
                        {
                            SkyLogger.w(TAG, "refreshView: icon file is not exist ");
                            mIconView.setImageResource(R.drawable.collect_web_defalut_icon);
                            // mIconView.setVisibility(VISIBLE);
                        }
                    }

                    // if (mCancelCollectView != null)
                    // {
                    // if (!isFocusOnCancel)
                    // {
                    // mCancelCollFrameLayout.setVisibility(INVISIBLE);
                    // }
                    // }
                    SkyLogger.i(TAG, "refreshView: title = " + mTitleView.getText());
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
        mShaderLayout.setSelected(true);
        mBackgroundImage.setBackgroundColor(Color.WHITE);
        mCancelCollFrameLayout.setVisibility(VISIBLE);
        mCancelCollFrameLayout.setHovered(true);
        mCancelCollFrameLayout.setOnHoverListener(cancleOnHoverListener);

        mTitleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTitleView.setTextColor(getResources().getColor(R.color.focus_text_color));
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
        mShaderLayout.setSelected(false);
        mBackgroundImage.setBackgroundColor(unFocusColor);
        mCancelCollFrameLayout.setVisibility(INVISIBLE);
        mCancelCollFrameLayout.setFocusable(false);
        mTitleView.setEllipsize(TextUtils.TruncateAt.END);
        mTitleView.setTextColor(getResources().getColor(R.color.unfocus_text_color));
    }

    private boolean isFocusOnCancel = false;

    public void focusCancelCollect()
    {
        SkyLogger.i(TAG, "focusCancelCollect: start");
        mCancelCollFrameLayout.setVisibility(VISIBLE);
        // mCancelCollFrameLayout.setSelected(true);
        cancelCollectBackground.setBackgroundColor(Color.WHITE);

        isFocusOnCancel = true;
        mCancelCollectView.setTextColor(getResources().getColor(R.color.focus_text_color));
    }

    public void unFocusCancelCollect()
    {
        SkyLogger.i(TAG, "unFocusCancelCollect: start");
        // mCancelCollFrameLayout.setSelected(false);
        cancelCollectBackground.setBackgroundColor(unFocusColor);

        isFocusOnCancel = false;
        mCancelCollectView.setTextColor(getResources().getColor(R.color.unfocus_text_color));
    }

    public boolean isFocusOnCancel()
    {
        return isFocusOnCancel;
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
    private OnHoverListener cancleOnHoverListener = new OnHoverListener()
    {

        @Override
        public boolean onHover(View v, MotionEvent event)
        {
            SkyLogger.i(TAG, "cancleOnHoverListener: event = " + event);
            final int focusTopMargin = UIUtil.getResolutionValue(70);
            final int focusLeftMargin = UIUtil.getResolutionValue(70);
            final int focusRightMargin = UIUtil.getResolutionValue(70);
            final int focusBottomMargin = UIUtil.getResolutionValue(71);
            SlideFocusView.FocusViewRevision revision = new SlideFocusView.FocusViewRevision(
                    focusLeftMargin, focusTopMargin, focusRightMargin, focusBottomMargin);
            int what = event.getAction();
            SkyLogger.d(TAG, "cancleOnHoverListeneronHover,event:" + event.toString());
            switch (what)
            {
                case MotionEvent.ACTION_HOVER_ENTER: // 鼠标进入view
                    unfocus();
                    focusCancelCollect();
                    mSlideFocusView.moveFocusTo(mCancelCollFrameLayout,
                            revision);
                    break;
                case MotionEvent.ACTION_HOVER_MOVE: // 鼠标在view上
                    break;
                case MotionEvent.ACTION_HOVER_EXIT: // 鼠标离开view
                    // v.clearFocus();
                    unFocusCancelCollect();
                    mSlideFocusView.moveFocusTo(mShaderLayout, revision);
                    break;
            }
            return false;
        }
    };

}
