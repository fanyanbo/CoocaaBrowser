/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-21 下午8:25     mikan
 *
 */

package com.skyworth.tv_browser.ui.customView;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.ui.data.CollectionData;
import com.skyworth.tv_browser.ui.interfaces.CollectionClickListener;
import com.skyworth.tv_browser.ui.interfaces.CollectionLayoutListener;
import com.skyworth.tv_browser.ui.interfaces.MenuToolbarLayoutListener;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.customview.SlideFocusView;
import com.skyworth.ui.listview.AdapterItem;
import com.skyworth.ui.listview.MetroAdapter;
import com.skyworth.ui.listview.MetroListView;

import java.util.List;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author mikan
 * @version V1.0.0
 * @className CollectionListView
 * @date 16/4/21
 */
public class CollectionListView extends FrameLayout implements
        MetroListView.OnItemSelectedListener, MetroListView.OnScrollStateListener,
        MetroListView.OnItemOnKeyListener, AdapterView.OnItemClickListener,
        MetroListView.OnScrollBarOnKeyListener
{
    private static final String TAG = "CollectionListView";

    private Context mContext = null;
    private SlideFocusView mSlideFocusView = null;

    private MetroAdapter<CollectionData> mAdapter;
    private MetroListView mGridView;
    // private MetroListViewScrollBar mScrollBar;

    private List<CollectionData> mCollectionDataList;
    private CollectionLayoutListener mCollectionLayoutListener = null;

    public CollectionListView(Context context, SlideFocusView slideFocusView,
            CollectionLayoutListener collectionLayoutListener)
    {
        super(context);

        mContext = context;
        mSlideFocusView = slideFocusView;
        initUI();
        mCollectionLayoutListener = collectionLayoutListener;
    }

    private void initUI()
    {
        initListView();
    }

    private void initListView()
    {
        mGridView = new MetroListView(mContext);

        mGridView.setSlidFocusView(mSlideFocusView);
        mGridView.setColmusNum(1);
        mGridView.setScrollDuration(200);
        // mGridView.setScrollBarView(mScrollBar);

        FrameLayout.LayoutParams list_p = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(1920), UIUtil.getResolutionValue(840));
        list_p.topMargin = UIUtil.getResolutionValue(230);
        list_p.leftMargin = UIUtil.getResolutionValue(335);

        this.addView(mGridView, list_p);

        // mGridView.setBackgroundColor(Color.BLUE);

        mGridView.setOnItemSelectedListener(this);
        mGridView.setOnScrollStateListener(this);
        mGridView.setOnItemOnKeyListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollBarOnKeyListener(this);
    }

    public void refreshValue(List<CollectionData> infos)
    {
        mCollectionDataList = infos;
        mAdapter = new MetroAdapter<CollectionData>(mCollectionDataList)
        {
            @Override
            public AdapterItem<CollectionData> onCreateItem(Object type)
            {
                CollectionItem item = new CollectionItem(mContext);
                item.setSlideFocusView(mSlideFocusView);
                return item;
            }
        };

        final int focusTopMargin = UIUtil.getResolutionValue(70);
        final int focusLeftMargin = UIUtil.getResolutionValue(70);
        final int focusRightMargin = -UIUtil.getResolutionValue(190);
        final int focusBottomMargin = UIUtil.getResolutionValue(65);
        mAdapter.setFocusChangedEvent(mSlideFocusView.focusChangedEvent,
                new SlideFocusView.FocusViewRevision(focusLeftMargin, focusTopMargin,
                        focusRightMargin, focusBottomMargin), itemFocusChangeListener);
        mGridView.setAdapter(mAdapter);

        mSlideFocusView.stopAnimationOnce();
        // mGridView.post(new Runnable()
        // {
        // @Override
        // public void run()
        // {
        // mGridView.setSelection(0);
        // }
        // });
        initFocusSize(0);
    }

    private OnFocusChangeListener itemFocusChangeListener = new OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            SkyLogger.i(TAG, "itemFocusChangeListener: onFocusChange:");
            if (v instanceof CollectionItem)
            {
                CollectionItem mCollectionItem = (CollectionItem) v;
                if (hasFocus)
                {
                    SkyLogger.i(
                            TAG,
                            "onItemSelected: isFocusOnCancel() = "
                                    + mCollectionItem.isFocusOnCancel());
                    if (!mCollectionItem.isFocusOnCancel())
                    {
                        mCollectionItem.focus();
                    } else
                    {
                        mCollectionItem.focusCancelCollect();
                    }
                } else
                {
                    mCollectionItem.unfocus();

                    mCollectionItem.unFocusCancelCollect();
                }
            }
        }
    };

    
    private void initFocusSize(final int i)
    {
        mGridView.post(new Runnable()
        {
            @Override
            public void run()
            {
                mGridView.setSelection(i);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        SkyLogger.i(TAG, "onItemClick: i = " + i);

        if (mCollectionDataList != null && !mCollectionDataList.isEmpty())
        {
            CollectionItem mCollectionItem = (CollectionItem) view;
            SkyLogger.i(TAG, "onItemClick: isFocusOnCancel = " + mCollectionItem.isFocusOnCancel());
            if (mCollectionItem.isFocusOnCancel())
            {
                // cancel btn click
                if (mCollectionClickListener != null)
                {
                    mCollectionClickListener.removeCollect(mCollectionDataList.get(i));

                    mCollectionDataList.remove(i);
                    mCollectionItem.unfocus();
                }

                if (mCollectionDataList == null || mCollectionDataList.isEmpty())
                {
                    SkyLogger.i(TAG, "onItemClick: data list is empty, refresh content view");
                    mCollectionLayoutListener.refreshContent();

                    mSlideFocusView.getFocusView().setVisibility(INVISIBLE);
                } else
                {
                    SkyLogger.i(TAG, "onItemClick: data list not empty, refresh list");
                    mAdapter.notifyDataSetChanaged();

                    // CollectionItem selectedItem =
                    // (CollectionItem) adapterView.getSelectedItem();
                    // if (selectedItem != null)
                    // {
                    // selectedItem.focus();
                    // }
                    // else
                    // {
                    // SkyLogger.w(TAG, "onItemClick: selectedItem is null");
                    // }

                    // if (mCollectionDataList.size() < i && i > 0)
                    // {
                    // initFocusSize(i-1);
                    // }

                    if (i - 1 >= 0)
                    {
                        initFocusSize(i - 1);
                    } else
                    {
                        initFocusSize(0);
                    }
                }
            } else
            {
                CollectionData data = mCollectionDataList.get(i);
                String url = data.url;
                SkyLogger.i(TAG, "onItemOnKeyEvent: url = " + url);

                // TODO mikan 跳转到某个页面
                mCollectionClickListener.jumpToWebsite(url);
                if (mMenuToolbarLayoutListener != null)
                {
                    mMenuToolbarLayoutListener.dismissMenuItemDialog();
                } else
                {
                    SkyLogger.w(TAG, "onItemClick: mMenuToolbarLayoutListener is null");
                }
            }
        }
    }

    @Override
    public boolean onBorderItemOnKeyEvent(View view, int keyCode, int position)
    {
        SkyLogger.i(TAG, "onBorderItemOnKeyEvent: keycode = " + keyCode);
        final int focusTopMargin = UIUtil.getResolutionValue(70);
        final int focusLeftMargin = UIUtil.getResolutionValue(70);
        final int focusRightMargin = UIUtil.getResolutionValue(70);
        final int focusBottomMargin = UIUtil.getResolutionValue(71);
        SlideFocusView.FocusViewRevision revision = new SlideFocusView.FocusViewRevision(
                focusLeftMargin, focusTopMargin, focusRightMargin, focusBottomMargin);
        CollectionItem mCollectionItem = (CollectionItem) view;
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mCollectionItem.unfocus();
                mCollectionItem.focusCancelCollect();
                mSlideFocusView.moveFocusTo(((CollectionItem) view).mCancelCollFrameLayout,
                        revision);
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                mCollectionItem.focus();
                mCollectionItem.unFocusCancelCollect();
                mSlideFocusView.moveFocusTo(((CollectionItem) view).mShaderLayout, revision);
                return true;

            default:

                break;
        }
        return false;
    }

    @Override
    public boolean onItemOnKeyEvent(View view, int keyCode, int position)
    {
        SkyLogger.i(TAG, "onItemOnKeyEvent: key code = " + keyCode);
        SkyLogger.i(TAG, "onItemOnKeyEvent: position = " + position);

        // switch (keyCode)
        // {
        // case KeyEvent.KEYCODE_DPAD_CENTER:
        // SkyLogger.i(TAG, "onItemOnzhege KeyEvent: center in");
        // if (mCollectionDataList != null && !mCollectionDataList.isEmpty())
        // {
        // CollectionData data = mCollectionDataList.get(position);
        // String url = data.url;
        // SkyLogger.i(TAG, "onItemOnKeyEvent: url = " + url);
        // }
        // break;
        //
        // default:
        // break;
        // }
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
        return false;
    }

    @Override
    public void onScrollEnd(MetroListView parent, int firstPostion, int endPosition)
    {

    }

    private CollectionClickListener mCollectionClickListener = null;

    public void setCollectionClickListener(CollectionClickListener collectionClickListener)
    {
        mCollectionClickListener = collectionClickListener;
    }

    private MenuToolbarLayoutListener mMenuToolbarLayoutListener = null;

    public void setMenuToolbarLayoutListener(MenuToolbarLayoutListener menuToolbarLayoutListener)
    {
        mMenuToolbarLayoutListener = menuToolbarLayoutListener;
    }
}
