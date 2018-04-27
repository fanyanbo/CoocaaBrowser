/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-15         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.ui.customView.CollectionListView;
import com.skyworth.tv_browser.ui.data.CollectionData;
import com.skyworth.tv_browser.ui.interfaces.CollectionClickListener;
import com.skyworth.tv_browser.ui.interfaces.CollectionLayoutListener;
import com.skyworth.tv_browser.ui.interfaces.MenuToolbarLayoutListener;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.customview.SlideFocusView;

import java.util.ArrayList;

/**
 * <p>
 * Description:收藏界面
 * </p>
 *
 * @author mikan
 * @version V1.0.0
 * @className CollectionLayout
 * @date 16/4/16
 */
public class CollectionLayout extends BaseLayout implements CollectionLayoutListener
{

    private static final String TAG = "CollectLayout";

	private CollectionClickListener mCollectionClickListener = null;
	private MenuToolbarLayoutListener mMenuToolbarLayoutListener = null;

	public CollectionLayout(Context context, CollectionClickListener collectionClickListener,
	                        MenuToolbarLayoutListener menuToolbarLayoutListener)
	{
		super(context);

		mCollectionClickListener = collectionClickListener;
		mMenuToolbarLayoutListener = menuToolbarLayoutListener;
		initUI();
	}

    @Override
    protected int getTitleIconResId()
    {
        return R.drawable.collect_icon;
    }

    @Override
    protected String getTitleText()
    {
        return mContext.getString(R.string.collection_title);
    }

    @Override
    protected View getContentView(SlideFocusView slideFocusView)
    {
	    ArrayList<CollectionData> collectionDataArrayList = getCollectWebList();

	    if (collectionDataArrayList == null || collectionDataArrayList.isEmpty())
	    {
		    return getNotExistCollectWeb();
	    }
	    else
	    {
		    return getExistCollectWeb(collectionDataArrayList, slideFocusView);
	    }
    }

	private ArrayList<CollectionData> getCollectWebList()
	{
//		CollectionData data1 = new CollectionData(null, "去哪里网-去哪里网Qunar.com#去哪里网-去哪里网Qunar.com#去哪里网-去哪里网Qunar.com#去哪里网-去哪里网Qunar.com#", "baidu");
//		CollectionData data2 = new CollectionData(null, "222222", "xinlang");
//		CollectionData data3 = new CollectionData(null, "333333", "coocaa");
//		CollectionData data4 = new CollectionData(null, "44444", null);
//		CollectionData data5 = new CollectionData(null, "555555", null);
//		CollectionData data6 = new CollectionData(null, "666666", null);
//		CollectionData data7 = new CollectionData(null, "777777", null);
//		CollectionData data8 = new CollectionData(null, "888888", null);
//		CollectionData data9 = new CollectionData(null, "999999", null);
//		CollectionData data10 = new CollectionData(null, "10101010", null);
//
//		ArrayList<CollectionData> dataArrayList = new ArrayList<CollectionData>();
//		dataArrayList.add(data1);
//		dataArrayList.add(data2);
//		dataArrayList.add(data3);
//		dataArrayList.add(data4);
//		dataArrayList.add(data5);
//		dataArrayList.add(data6);
//		dataArrayList.add(data7);
//		dataArrayList.add(data8);
//		dataArrayList.add(data9);
//		dataArrayList.add(data10);
//		return dataArrayList;

		if (mCollectionClickListener == null)
		{
			SkyLogger.e(TAG, "getCollectWebList: mCollectionClickListener is null");
			return null;
		}

		return (ArrayList<CollectionData>) mCollectionClickListener.getCollectList();
	}

	private View getExistCollectWeb(ArrayList<CollectionData> collectionDataArrayList, SlideFocusView slideFocusView)
	{
		CollectionListView mCollectionListView = new CollectionListView(mContext, slideFocusView, this);
		mCollectionListView.refreshValue(collectionDataArrayList);
		mCollectionListView.setCollectionClickListener(mCollectionClickListener);
		mCollectionListView.setMenuToolbarLayoutListener(mMenuToolbarLayoutListener);

		return mCollectionListView;
	}

	/**
	 * 概述：不存在收藏时显示的界面<br/>
	 *
	 * @return View
	 * date 16/4/18
	 */
    private View getNotExistCollectWeb()
    {
	    final int EMOJI_HEIGHT = UIUtil.getResolutionValue(144);
	    final int EMOJI_WIDTH = UIUtil.getResolutionValue(144);
	    final int EMOJI_TOP_MARGIN = UIUtil.getResolutionValue(415);
	    final int TIPS_TEXT_SIZE = UIUtil.getTextDpiValue(36);
	    final int TIPS_TEXT_TOP_MARGIN = UIUtil.getResolutionValue(628);

        RelativeLayout mContentView = new RelativeLayout(mContext);

        ImageView emojiView = new ImageView(mContext);
        emojiView.setImageResource(R.drawable.emoji);

        RelativeLayout.LayoutParams emojiViewParams = new RelativeLayout.LayoutParams(EMOJI_WIDTH,
                                                                                      EMOJI_HEIGHT);
        emojiViewParams.topMargin = EMOJI_TOP_MARGIN;
	    emojiViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView tipsTextView = new TextView(mContext);
        tipsTextView.setIncludeFontPadding(false);
        tipsTextView.setText(R.string.collection_tips);
        tipsTextView.setTextSize(TIPS_TEXT_SIZE);
	    tipsTextView.setTextColor(getResources().getColor(R.color.unfocus_text_color));

	    RelativeLayout.LayoutParams tipsTextViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                           ViewGroup.LayoutParams.WRAP_CONTENT);
        tipsTextViewParams.topMargin = TIPS_TEXT_TOP_MARGIN;
	    tipsTextViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


        mContentView.addView(emojiView, emojiViewParams);
        mContentView.addView(tipsTextView, tipsTextViewParams);

        return mContentView;
    }

	@Override
	protected LayoutParams getContentViewLayoutParams()
	{
		LayoutParams contentViewParams = new LayoutParams(UIUtil.getResolutionValue(1920),
		                                                  UIUtil.getResolutionValue(1080));
//				contentViewParams.topMargin = UIUtil.getResolutionValue(285);
//				contentViewParams.leftMargin = UIUtil.getResolutionValue(230);
		return contentViewParams;
	}

	@Override
	public void refreshContent()
	{
		refreshContentView();
	}

}
