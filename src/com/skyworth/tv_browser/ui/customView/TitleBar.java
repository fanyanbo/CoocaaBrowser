/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-18 下午3:50     mikan
 *
 */

package com.skyworth.tv_browser.ui.customView;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skyworth.tv_browser.util.UIUtil;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author mikan
 * @version V1.0.0
 * @className TitleBar
 * @date 16/4/16
 */
public class TitleBar extends LinearLayout
{
	private static final String TAG = "TitleBar";
	private Context mContext = null;

	private final int TITLE_ICON_WIDTH = UIUtil.getResolutionValue(40);
	private final int TITLE_ICON_HEIGHT = UIUtil.getResolutionValue(40);

	private final int TITLE_TOP_MARGIN = UIUtil.getResolutionValue(100);
	private final int TITLE_LEFT_MARGIN = UIUtil.getResolutionValue(100);
	private final int TITLE_TEXT_SIZE = UIUtil.getResolutionValue(25);

	private final int TITLE_TEXT_PADDING_LEFT = UIUtil.getTextDpiValue(36);
	private final int TITLE_TEXT_PADDING_TOP = UIUtil.getResolutionValue(0);
	private final int TITLE_TEXT_PADDING_RIGHT = UIUtil.getResolutionValue(0);
	private final int TITLE_TEXT_PADDING_BOTTOM = UIUtil.getResolutionValue(0);

	public TitleBar(Context context)
	{
		super(context);

		mContext = context;

		this.setOrientation(HORIZONTAL);
		RelativeLayout.LayoutParams mLayoutParams =
				new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				                                ViewGroup.LayoutParams.WRAP_CONTENT);
		mLayoutParams.topMargin = TITLE_TOP_MARGIN;
		mLayoutParams.leftMargin = TITLE_LEFT_MARGIN;
		this.setLayoutParams(mLayoutParams);
//		this.setBackgroundColor(Color.RED);
	}

	public void initUI(int iconResId, String title)
	{
		ImageView titleIcon = new ImageView(mContext);
		titleIcon.setImageResource(iconResId);

		LayoutParams mTitleIconParams = new LayoutParams(TITLE_ICON_WIDTH, TITLE_ICON_HEIGHT);
		titleIcon.setLayoutParams(mTitleIconParams);

		TextView titleText = new TextView(mContext);
		titleText.setIncludeFontPadding(false);
		titleText.setPadding(TITLE_TEXT_PADDING_LEFT, TITLE_TEXT_PADDING_TOP,
		                     TITLE_TEXT_PADDING_RIGHT, TITLE_TEXT_PADDING_BOTTOM);
		titleText.setTextSize(TITLE_TEXT_SIZE);
		titleText.setText(title);
		titleText.setTextColor(Color.parseColor("#5b5b5b"));

		this.addView(titleIcon);
		this.addView(titleText);
	}

}
