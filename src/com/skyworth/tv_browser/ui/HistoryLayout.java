/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-18 下午8:23     mikan
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
import com.skyworth.tv_browser.ui.customView.HistoryListView;
import com.skyworth.tv_browser.ui.data.HistoryData;
import com.skyworth.tv_browser.ui.interfaces.HistoryClickListener;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.customview.SlideFocusView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author mikan
 * @version V1.0.0
 * @className HistoryLayout
 * @date 16/4/18
 */
public class HistoryLayout extends BaseLayout
{
	private static final String TAG = "HistoryLayout";

	private HistoryClickListener mHistoryClickListener = null;

	public HistoryLayout(Context context, HistoryClickListener historyClickListener)
	{
		super(context);

		mHistoryClickListener = historyClickListener;
		initUI();
	}

	@Override
	protected int getTitleIconResId()
	{
		return R.drawable.history_icon;
	}

	@Override
	protected String getTitleText()
	{
		return mContext.getString(R.string.history_title);
	}

	@Override
	protected View getContentView(SlideFocusView slideFocusView)
	{
		List<HistoryData> mHistoryDataList = getHistoryList();
		if (mHistoryDataList == null || mHistoryDataList.isEmpty())
		{
			SkyLogger.i(TAG, "getExistHistory: is empty");
			return getNotExistHistory();
		}
		else
		{
			SkyLogger.i(TAG, "getExistHistory: not empty");
			return getExistHistory(mHistoryDataList, slideFocusView);
		}
	}

	private View getExistHistory(List<HistoryData> historyDataList, SlideFocusView slideFocusView)
	{
		// TODO 存在浏览记录的页面时候, 对应的view

		HistoryListView mHistoryListView = new HistoryListView(mContext, slideFocusView);
		mHistoryListView.setHistoryClickListener(mHistoryClickListener);
		mHistoryListView.refreshValue(historyDataList);

		return mHistoryListView;
	}

	private List<HistoryData> getHistoryList()
	{
		if (mHistoryClickListener == null)
		{
			SkyLogger.e(TAG, "getHistoryList: mHistoryClickListener is null");
			return null;
		}

		return mHistoryClickListener.getHistoryList();
	}

	private List<HistoryData> getTestHisoryList()
	{
		ArrayList<HistoryData> mHistoryDataArrayList = new ArrayList<HistoryData>();

		HistoryData data1 = new HistoryData(
				"1234567890@1234567890#1234567890$1234567890@1234567890@1234567890@1234567890@",
				"2016.04.19", "www.baidu.com");
		HistoryData data2 = new HistoryData(
				"去哪里网-去哪里网Qunar.com#去哪里网-去哪里网Qunar.com#去哪里网-去哪里网Qunar.com#去哪里网-去哪里网Qunar.com#",
				"2016.04.15", "www.baidu.com");
		HistoryData data3 = new HistoryData("3", "2016.04.12", "www.baidu.com");
		HistoryData data4 = new HistoryData("4", "2016.04.15", "www.baidu.com");
		HistoryData data5 = new HistoryData("5", "2016.04.19", "www.baidu.com");
		HistoryData data6 = new HistoryData("6", "2016.04.09", "www.baidu.com");
		HistoryData data7 = new HistoryData("7", "2016.04.09", "www.baidu.com");
		HistoryData data8 = new HistoryData("8", "2016.04.09", "www.baidu.com");
		HistoryData data9 = new HistoryData("9", "2016.04.09", "www.baidu.com");
		HistoryData data10 = new HistoryData("10", "2016.04.09", "www.baidu.com");

		mHistoryDataArrayList.add(data1);
		mHistoryDataArrayList.add(data2);
		mHistoryDataArrayList.add(data3);
		mHistoryDataArrayList.add(data4);
		mHistoryDataArrayList.add(data5);
		mHistoryDataArrayList.add(data6);
		mHistoryDataArrayList.add(data7);
		mHistoryDataArrayList.add(data8);
		mHistoryDataArrayList.add(data9);
		mHistoryDataArrayList.add(data10);

		return mHistoryDataArrayList;
	}

	private View getNotExistHistory()
	{
		final int EMOJI_HEIGHT = UIUtil.getResolutionValue(144);
		final int EMOJI_WIDTH = UIUtil.getResolutionValue(144);
		final int EMOJI_TOP_MARGIN = UIUtil.getResolutionValue(415);
		final int TIPS_TEXT_SIZE = UIUtil.getTextDpiValue(36);
		final int TIPS_TEXT_TOP_MARGIN = UIUtil.getResolutionValue(628);

		RelativeLayout mContentView = new RelativeLayout(mContext);

		ImageView emojiView = new ImageView(mContext);
		emojiView.setImageResource(R.drawable.emoji);

		RelativeLayout.LayoutParams emojiViewParams =
				new RelativeLayout.LayoutParams(EMOJI_WIDTH, EMOJI_HEIGHT);
		emojiViewParams.topMargin = EMOJI_TOP_MARGIN;
		emojiViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		TextView tipsTextView = new TextView(mContext);
		tipsTextView.setIncludeFontPadding(false);
		tipsTextView.setText(R.string.history_tips);
		tipsTextView.setTextSize(TIPS_TEXT_SIZE);
		tipsTextView.setTextColor(getResources().getColor(R.color.unfocus_text_color));

		RelativeLayout.LayoutParams tipsTextViewParams =
				new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
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
		LayoutParams contentViewParams =
				new LayoutParams(UIUtil.getResolutionValue(1920), UIUtil.getResolutionValue(1080));
		//		contentViewParams.topMargin = UIUtil.getResolutionValue(285);
		//		contentViewParams.leftMargin = UIUtil.getResolutionValue(230);
		return contentViewParams;
	}
}
