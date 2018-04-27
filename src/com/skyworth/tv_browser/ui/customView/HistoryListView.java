/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-19 下午2:55     mikan
 *
 */

package com.skyworth.tv_browser.ui.customView;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.ui.data.HistoryData;
import com.skyworth.tv_browser.ui.interfaces.HistoryClickListener;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.customview.SlideFocusView;
import com.skyworth.ui.listview.AdapterItem;
import com.skyworth.ui.listview.MetroAdapter;
import com.skyworth.ui.listview.MetroListView;
import com.skyworth.ui.listview.MetroListViewScrollBar;
import com.skyworth.util.SkyScreenParams;

import java.util.List;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className HistoryGridView
 * @date 16/4/19
 */
public class HistoryListView extends FrameLayout implements MetroListView.OnItemSelectedListener,
        MetroListView.OnScrollStateListener, MetroListView.OnItemOnKeyListener,
        AdapterView.OnItemClickListener, MetroListView.OnScrollBarOnKeyListener
{
    private static final String TAG = "HistoryGridView";

    private Context mContext = null;
    private SlideFocusView mSlideFocusView = null;

    private MetroAdapter<HistoryData> mAdapter;
    private MetroListView mGridView;
    private MetroListViewScrollBar mScrollBar;

    private List<HistoryData> mHistoryDataList;
    private HistoryClickListener mHistoryClickListener = null;

    /**
     * @param context
     */
    public HistoryListView(Context context, SlideFocusView slideFocusView)
    {
        super(context);

        mContext = context;
        mSlideFocusView = slideFocusView;
        initUI();
    }

    public void setHistoryClickListener(HistoryClickListener mHistoryClickListener)
    {
        this.mHistoryClickListener = mHistoryClickListener;
    }

    private void initUI()
    {
        initScrollBar();

        initListView();
    }

    private void initScrollBar()
    {
        mScrollBar = new MetroListViewScrollBar(mContext);
        mScrollBar.setScrollBarBg(com.skyworth.commen.ui.R.drawable.ic_custom_scrollbar_bg);
        mScrollBar
                .setScrollBarIcon(com.skyworth.commen.ui.R.drawable.ic_custom_scrollbar_focus_icon);
        mScrollBar.setSlidFocusView(mSlideFocusView);

        int focusMargin = SkyScreenParams.getInstence(mContext).getResolutionValue(75);
        int left = focusMargin;
        int top = focusMargin;
        int right = focusMargin;
        int bottom = focusMargin;
        mScrollBar.setFocusChangedEvent(mSlideFocusView.focusChangedEvent,
                new SlideFocusView.FocusViewRevision(left, top, right, bottom), null);
        mScrollBar.setScrollBarFocusble(true);

        FrameLayout.LayoutParams scrollBar_p = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, UIUtil.getResolutionValue(815));
        scrollBar_p.topMargin = UIUtil.getResolutionValue(240);
        scrollBar_p.leftMargin = UIUtil.getResolutionValue(1708);

        this.addView(mScrollBar, scrollBar_p);
    }

    private void initListView()
    {
        mGridView = new MetroListView(mContext);

        mGridView.setSlidFocusView(mSlideFocusView);
        mGridView.setColmusNum(1);
        mGridView.setScrollDuration(200);
        mGridView.setScrollBarView(mScrollBar);

        FrameLayout.LayoutParams list_p = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(1920), UIUtil.getResolutionValue(840));
        list_p.topMargin = UIUtil.getResolutionValue(230);
        list_p.leftMargin = UIUtil.getResolutionValue(285);

        this.addView(mGridView, list_p);

        // mGridView.setBackgroundColor(Color.BLUE);

        mGridView.setOnItemSelectedListener(this);
        mGridView.setOnScrollStateListener(this);
        mGridView.setOnItemOnKeyListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollBarOnKeyListener(this);
    }

    public void refreshValue(List<HistoryData> infos)
    {
        mHistoryDataList = infos;
        mAdapter = new MetroAdapter<HistoryData>(mHistoryDataList)
        {
            @Override
            public AdapterItem<HistoryData> onCreateItem(Object type)
            {
                HistoryItem item = new HistoryItem(mContext);
                return item;
            }
        };

        final int focusTopMargin = UIUtil.getResolutionValue(70);
        final int focusLeftMargin = UIUtil.getResolutionValue(70);
        final int focusRightMargin = UIUtil.getResolutionValue(70);
        final int focusBottomMargin = UIUtil.getResolutionValue(65);
        mAdapter.setFocusChangedEvent(mSlideFocusView.focusChangedEvent,
                new SlideFocusView.FocusViewRevision(focusLeftMargin, focusTopMargin,
                        focusRightMargin, focusBottomMargin), itemFocusChangeListener);
        mGridView.setAdapter(mAdapter);

        mSlideFocusView.stopAnimationOnce();
        mGridView.post(new Runnable()
        {
            @Override
            public void run()
            {
                mGridView.setSelection(0);
            }
        });
    }

    private OnFocusChangeListener itemFocusChangeListener = new OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            SkyLogger.i(TAG, "itemFocusChangeListener: onFocusChange:");
            if (v instanceof HistoryItem)
            {
                HistoryItem mHistoryItem = (HistoryItem) v;
                if (hasFocus)
                {
                    mHistoryItem.focus();
                } else
                {
                    mHistoryItem.unfocus();
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        SkyLogger.i(TAG, "onItemClick: start:" + mHistoryDataList.get(i).toString());
        mHistoryClickListener.jumpToWebsite(mHistoryDataList.get(i).getUrl());
    }

    @Override
    public boolean onBorderItemOnKeyEvent(View view, int keyCode, int position)
    {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        {
            if (mScrollBar.getVisibility() == View.VISIBLE)
            {
                mSlideFocusView.getFocusView().setBackgroundResource(
                        com.skyworth.commen.ui.R.drawable.ic_custom_scrollbar_focus_bg);
                mScrollBar
                        .setScrollBarIcon(com.skyworth.commen.ui.R.drawable.ic_custom_scrollbar_focus_icon);
                HistoryItem mHistoryItem = (HistoryItem) view;
                mHistoryItem.unfocus();
            }
        }
        return false;
    }

    @Override
    public boolean onItemOnKeyEvent(View view, int keyCode, int position)
    {
        return false;
    }

    @Override
    public void onItemSelected(MetroListView parent, View selectView, int selectPosition)
    {
        SkyLogger.i(TAG, "onItemSelected: selectPosition = " + selectPosition);

    }

    @Override
    public void onItemUnSelected(MetroListView parent, View unSelectView)
    {
        SkyLogger.i(TAG, "onItemUnSelected: in");

    }

    @Override
    public boolean onScrollBarOnKey(View view, int keyCode)
    {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
        {
            mSlideFocusView.getFocusView().setBackgroundResource(
                    com.skyworth.commen.ui.R.drawable.ui_sdk_btn_focus_shadow_no_bg);
            mScrollBar
                    .setScrollBarIcon(com.skyworth.commen.ui.R.drawable.ic_custom_scrollbar_unfocus_icon);
        }
        return false;
    }

    @Override
    public void onScrollEnd(MetroListView parent, int firstPostion, int endPosition)
    {

    }
}
