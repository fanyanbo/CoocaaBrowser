/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-18 下午8:24     mikan
 *
 */

package com.skyworth.tv_browser.ui;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.View.OnHoverListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.data.WindowData;
import com.skyworth.tv_browser.ui.interfaces.MultWindowClickListener;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.customview.SlideFocusView;
import com.skyworth.util.MyFocusFrame;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className MultiWindowLayout
 * @date 16/4/18
 */
public class MultiWindowLayout extends BaseLayout
{
    private static final String TAG = "MultiWindowLayout";
    // private HorizontalScrollView multiwindowScrollView = null;
    private FrameLayout multiWindowsItemsLayout = null;
    private static int BookMarkCount = 3;
    private static int ITEMS_ID = 42456;
    private ArrayList<MultiWindowsItemImageView> itemsList = null;
    private ArrayList<LayoutParams> itemsParamsList = null;
    private int itemPositionX = 0;
    private int itemPositionY = 0;
    private static final int ITEM_WIDTH = 500;
    private static final int ITEM_HEIGHT = 280;

    private static final int ITEM_ANIMATE_MOVE_WIDTH = 540;
    private static final int ITEM_FOCUS_ANIMATE_MOVE_WIDTH = 665;

    private static float SCALE_SMALL = 0.68f;
    private static float SCALE_BIG = 1.5f;
    private static float SCALE_NOMORL = 1.0f;
    private static int selectIndex = 1;
    private ArrayList<WindowData> windowsList = null;
    private MultWindowClickListener multWindowClickListener = null;

    /**
     * @Fields deleteImageView 删除按钮元素
     */
    private ImageView deleteImageView = null;
    /**
     * @Fields webTitleTextView 网页的title文字
     */
    private TextView webTitleTextView = null;
    private MyFocusFrame mMyFocusFrame = null;

    private boolean canKey = true;

    public MultiWindowLayout(Context context, ArrayList<WindowData> list,
            MultWindowClickListener windowClickListener,int windowPosition)
    {
        super(context);
        SkyLogger.d(TAG, "MultiWindowLayout:");
        windowsList = list;
        multWindowClickListener = windowClickListener;
        selectIndex = windowPosition;
        if (list.size() > 1)
        {
//            selectIndex = list.size() - 1;
            BookMarkCount = list.size() - 1;
        }
        initUI();
    }

    @Override
    protected int getTitleIconResId()
    {
        return R.drawable.multiwindow_icon;
    }

    @Override
    protected String getTitleText()
    {
        return mContext.getString(R.string.multi_window_title);
    }

    @Override
    protected View getContentView(SlideFocusView slideFocusView)
    {

        multiWindowsItemsLayout = new FrameLayout(mContext);
        itemsList = new ArrayList<MultiWindowsItemImageView>();
        itemsParamsList = new ArrayList<FrameLayout.LayoutParams>();
        initFocusFrame();
        itemPositionX = UIUtil.getResolutionValue(45) - (selectIndex - 1)
                * UIUtil.getResolutionValue(ITEM_ANIMATE_MOVE_WIDTH);
        itemPositionY = UIUtil.getResolutionValue(215);
        initWindowsItems(selectIndex);
        initTitleIcon(windowsList.get(selectIndex).getTitle());
        return multiWindowsItemsLayout;
    }

    private void initFocusFrame()
    {
        mMyFocusFrame = new MyFocusFrame(mContext, UIUtil.getResolutionValue(83));
        mMyFocusFrame.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_bg);
        // mMyFocusFrame.setAnimDuration(200);
        mMyFocusFrame.setFocusable(false);
        mMyFocusFrame.needAnimtion(true);
        multiWindowsItemsLayout.addView(mMyFocusFrame);
    }

    @Override
    protected LayoutParams getContentViewLayoutParams()
    {
        LayoutParams contentViewParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                UIUtil.getResolutionValue(925));
        contentViewParams.topMargin = UIUtil.getResolutionValue(155);
        return contentViewParams;
    }

    /**
     * @Description 添加第一个添加新窗口的按钮<br/>
     * @date 2016-4-26
     */
    private void addFirstAddWindowItem()
    {
        SkyLogger.d(TAG, "addFirstAddWindowItem itemPositionX:" + itemPositionX + ",itemPositionY:"
                + itemPositionY);
        MultiWindowsItemImageView windowItemLayout = new MultiWindowsItemImageView(mContext,
                windowsList.get(0).getBm());
        windowItemLayout.setId(ITEMS_ID);
        windowItemLayout.setOnClickListener(onClickListener);
        windowItemLayout.setOnHoverListener(itemOnHoverListener);
        windowItemLayout.setOnFocusChangeListener(itemFocusChangeListener);
        FrameLayout.LayoutParams windowItemLayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(ITEM_WIDTH), UIUtil.getResolutionValue(ITEM_HEIGHT));
        windowItemLayoutParams.leftMargin = itemPositionX;
        windowItemLayoutParams.topMargin = itemPositionY;

        multiWindowsItemsLayout.addView(windowItemLayout, windowItemLayoutParams);

        itemsList.add(windowItemLayout);
        itemsParamsList.add(windowItemLayoutParams);

    }

    private void initWindowsItems(int select)
    {
        SkyLogger.d(TAG, "initWindowsItems:");
        addFirstAddWindowItem();
        // selectIndex = select;
        for (int i = 0; i < BookMarkCount; i++)
        {
            SkyLogger.d(TAG, "initWindowsItems: I：" + i);
            try {
                final MultiWindowsItemImageView windowItem = new MultiWindowsItemImageView(mContext,
                        windowsList.get(i + 1).getBm());
                windowItem.setId(ITEMS_ID + i + 1);
                windowItem.setOnClickListener(onClickListener);
                windowItem.setOnHoverListener(itemOnHoverListener);
                windowItem.setOnFocusChangeListener(itemFocusChangeListener);
                FrameLayout.LayoutParams windowItemLayoutParams = null;
                // 如果是最后一个添加的窗口，则最后一个窗口需要变大，同时X,y的值变化
                if (i == (select - 1)) {
                    itemPositionX += UIUtil.getResolutionValue(665);
                    // itemPositionY -= 70;

                    windowItemLayoutParams = new FrameLayout.LayoutParams(
                            UIUtil.getResolutionValue(ITEM_WIDTH),
                            UIUtil.getResolutionValue(ITEM_HEIGHT));
                    windowItemLayoutParams.leftMargin = itemPositionX;
                    windowItemLayoutParams.topMargin = itemPositionY;
                    windowItem.requestFocus();
                    windowItem.post(new Runnable() {

                        @Override
                        public void run() {
                            ScaleAnimate(windowItem, SCALE_BIG);
                            mMyFocusFrame.initStarPosition(UIUtil.getResolutionValue(505),
                                    UIUtil.getResolutionValue(62),
                                    UIUtil.getResolutionValue(745 + 80 * 2),
                                    UIUtil.getResolutionValue(425 + 80 * 2));
                        }
                    });
                    itemPositionX += UIUtil.getResolutionValue(125);
                    // itemPositionY += 70;
                } else { // 普通的只是x位置变化了，Y值没有变化
                    itemPositionX += UIUtil.getResolutionValue(540);
                    windowItemLayoutParams = new FrameLayout.LayoutParams(
                            UIUtil.getResolutionValue(ITEM_WIDTH),
                            UIUtil.getResolutionValue(ITEM_HEIGHT));
                    windowItemLayoutParams.leftMargin = itemPositionX;
                    windowItemLayoutParams.topMargin = itemPositionY;
                }
                // windowItemLayout.setOnFocusChangeListener(itemFocusChangeListener);
                multiWindowsItemsLayout.addView(windowItem, windowItemLayoutParams);

                itemsList.add(windowItem);
                itemsParamsList.add(windowItemLayoutParams);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private void initTitleIcon(String title)
    {
        SkyLogger.d(TAG, "initTitleIcon:");
        webTitleTextView = new TextView(mContext);
        webTitleTextView.setText(title);
        webTitleTextView.setGravity(Gravity.CENTER);
        webTitleTextView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        webTitleTextView.setSingleLine();
        webTitleTextView.setMarqueeRepeatLimit(-1);
        webTitleTextView.setTextSize(UIUtil.getTextDpiValue(36));
        webTitleTextView.setTextColor(R.color.menu_item_word_5b5b5b);
        FrameLayout.LayoutParams webTitleLayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(555), LayoutParams.WRAP_CONTENT);
        webTitleLayoutParams.topMargin = UIUtil.getResolutionValue(600);
        webTitleLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        deleteImageView = new ImageView(mContext);
        deleteImageView.setFocusable(true);
        deleteImageView.setBackgroundColor(getResources().getColor(R.color.unfocus_text_color));
        deleteImageView.setOnHoverListener(itemOnHoverListener);
        deleteImageView.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    // webTitleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    v.setBackgroundResource(R.drawable.window_item_close_focus);
                    mMyFocusFrame.changeFocusPos(UIUtil.getResolutionValue(1187),
                            UIUtil.getResolutionValue(502), UIUtil.getResolutionValue(66 + 83 * 2),
                            UIUtil.getResolutionValue(66 + 83 * 2));
                } else
                {
                    webTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                    v.setBackgroundResource(R.drawable.window_item_close_unfocus);
                }

            }
        });

        deleteImageView.setOnClickListener(deleteClickListener);
        deleteImageView.setBackgroundResource(R.drawable.window_item_close_unfocus);
        FrameLayout.LayoutParams delImageLayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(66), UIUtil.getResolutionValue(66));
        delImageLayoutParams.leftMargin = UIUtil.getResolutionValue(1270);
        delImageLayoutParams.topMargin = UIUtil.getResolutionValue(585);

        multiWindowsItemsLayout.addView(webTitleTextView, webTitleLayoutParams);
        multiWindowsItemsLayout.addView(deleteImageView, delImageLayoutParams);
    }

    private OnFocusChangeListener itemFocusChangeListener = new OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            SkyLogger.d(TAG, "onFocusChange,hasFocus:" + hasFocus);
            if (hasFocus)
            {
                if (mMyFocusFrame != null)
                {
                    mMyFocusFrame.changeFocusPos(UIUtil.getResolutionValue(505),
                            UIUtil.getResolutionValue(62), UIUtil.getResolutionValue(745 + 81 * 2),
                            UIUtil.getResolutionValue(425 + 80 * 2));

                }
            } else
            {

            }

        }
    };

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

    private void updateTitle(String title)
    {
        if (title != null && !"".equals(title) && webTitleTextView != null)
        {
            SkyLogger.d(TAG, "updateTitleIcon,title:" + title);
            webTitleTextView.setText(title);
        }
        if (selectIndex == 0)
        {
            deleteImageView.setVisibility(View.GONE);
        } else
        {
            deleteImageView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        SkyLogger.d(TAG, "dispatchKeyEvent:" + event.getKeyCode() + ",event:" + event.getAction());
        if (event.getAction() == KeyEvent.ACTION_DOWN && canKey)
        {
            SkyLogger.d(TAG, "keyCode:" + event.getKeyCode() + ",event:" + event.getAction()
                    + ",selectIndex:" + selectIndex);
            switch (event.getKeyCode())
            {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    SkyLogger.d(TAG, "keyCode: KEYCODE_DPAD_LEFT");

                    if (selectIndex < (itemsList.size() - 1))
                    {
                        for (int i = 0; i < itemsList.size(); i++)
                        {
                            MultiWindowsItemImageView itemLayout = itemsList.get(i);
                            SkyLogger.d(TAG, "keyCode: x");
                            if (i == selectIndex)
                            {
                                moveAnimate(itemLayout,
                                        -UIUtil.getResolutionValue(ITEM_FOCUS_ANIMATE_MOVE_WIDTH),
                                        0, SCALE_NOMORL);
                            } else if (i == (selectIndex + 1))
                            {
                                moveAnimate(itemLayout,
                                        -UIUtil.getResolutionValue(ITEM_FOCUS_ANIMATE_MOVE_WIDTH),
                                        0, SCALE_BIG);
                                itemLayout.requestFocus();
                            } else if (i < selectIndex)
                            {
                                moveAnimate(itemLayout,
                                        -UIUtil.getResolutionValue(ITEM_ANIMATE_MOVE_WIDTH), 0,
                                        SCALE_NOMORL);
                            } else
                            {
                                moveAnimate(itemLayout,
                                        -UIUtil.getResolutionValue(ITEM_ANIMATE_MOVE_WIDTH), 0,
                                        SCALE_NOMORL);
                            }

                        }
                        selectIndex++;
                        updateTitle(windowsList.get(selectIndex).getTitle());
                    }

                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    SkyLogger.d(TAG, "keyCode: KEYCODE_DPAD_RIGHT");
                    if (selectIndex > 0)
                    {
                        for (int i = 0; i < itemsList.size(); i++)
                        {
                            MultiWindowsItemImageView itemLayout = itemsList.get(i);
                            SkyLogger.d(TAG, "keyCode: x");
                            if (i == selectIndex)
                            {
                                moveAnimate(itemLayout,
                                        UIUtil.getResolutionValue(ITEM_FOCUS_ANIMATE_MOVE_WIDTH),
                                        0, SCALE_NOMORL);
                            } else if (i == (selectIndex - 1))
                            {
                                moveAnimate(itemLayout,
                                        UIUtil.getResolutionValue(ITEM_FOCUS_ANIMATE_MOVE_WIDTH),
                                        0, SCALE_BIG);
                                itemLayout.requestFocus();
                            } else if (i > selectIndex)
                            {
                                moveAnimate(itemLayout,
                                        UIUtil.getResolutionValue(ITEM_ANIMATE_MOVE_WIDTH), 0,
                                        SCALE_NOMORL);
                            } else
                            {
                                moveAnimate(itemLayout,
                                        UIUtil.getResolutionValue(ITEM_ANIMATE_MOVE_WIDTH), 0,
                                        SCALE_NOMORL);
                            }
                        }
                        selectIndex--;
                        updateTitle(windowsList.get(selectIndex).getTitle());
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    SkyLogger.d(TAG, "keyCode: KEYCODE_DPAD_UP");

                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    SkyLogger.d(TAG, "keyCode: KEYCODE_DPAD_DOWN");

                    break;

                default:
                    break;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    private void moveAnimate(MultiWindowsItemImageView itemLayout, int moveXDistance,
            int moveYDistance, float scale)
    {
        SkyLogger.d(TAG, "moveAnimate,moveXDistance:" + moveXDistance + ",moveYDistance:"
                + moveYDistance + ", scale:" + scale);
        float itemx = itemLayout.getX();
        float itemy = itemLayout.getY();
        SkyLogger.d(TAG, "item: x:" + itemx + ", y:" + itemy);
        ViewPropertyAnimator animate = itemLayout.animate();
        animate.x(itemx + moveXDistance).y(itemy + moveYDistance).scaleX(scale).scaleY(scale)
                .setDuration(200).setListener(animListener).start();

    }

    private AnimatorListener animListener = new AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            canKey = false;
            deleteImageView.setFocusable(false);
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            canKey = true;
            for (int i = 0; i < itemsList.size(); i++)
            {
                if (i == selectIndex)
                {
                    itemsList.get(i).requestFocus();
                }
            }
            deleteImageView.setFocusable(true);

        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            canKey = true;
        }
    };

    private void ScaleAnimate(MultiWindowsItemImageView itemLayout, float scale)
    {
        SkyLogger.d(TAG, "ScaleAnimate" + ", scale:" + scale);
        ViewPropertyAnimator animate = itemLayout.animate();
        animate.scaleX(scale).scaleY(scale).setDuration(0).setListener(null).start();

    }

    private OnClickListener deleteClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            SkyLogger.d(TAG, "delete window item click,selectIndex:" + (selectIndex - 1));
            if (selectIndex > 0)
            {
                multWindowClickListener.removeWindow(selectIndex - 1);
            }

        }
    };

    private OnClickListener onClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            SkyLogger.d(TAG, "window item click,selectIndex:" + selectIndex);
            if (selectIndex <= 0)
            {
                multWindowClickListener.addNewWindow();
            } else
            {
                multWindowClickListener.clickWindow(windowsList.get(selectIndex), selectIndex - 1);

            }
        }
    };
}
