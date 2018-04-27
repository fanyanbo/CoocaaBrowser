/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-6-14         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.ui.interfaces.SearchClickListener;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.util.MyFocusFrame;

public class SearchLayout extends FrameLayout
{
    private Context mContext;
    private static final String TAG = "SearchLayout";
    private ImageView searchIconView;
    private EditText searchText;
    private ImageButton searchButton;
    private boolean isOversea = false;
    private SearchClickListener searchClickListener = null;
    private MyFocusFrame mMyFocusFrame = null;

    public void setMyFocusFrame(MyFocusFrame mMyFocusFrame)
    {
        this.mMyFocusFrame = mMyFocusFrame;
    }

    public void setSearchClickListener(SearchClickListener searchClickListener)
    {
        this.searchClickListener = searchClickListener;
    }

    public SearchLayout(Context context, boolean isOversea)
    {
        super(context);
        mContext = context;
        this.isOversea = isOversea;
        initView();
    }

    public void getFocus()
    {
        if (searchText != null)
        {
            searchText.requestFocus();
        }
    }

    private void initView()
    {
        searchIconView = new ImageView(mContext);
        if (isOversea)
        {
            searchIconView.setImageResource(R.drawable.google);
        } else
        {
            searchIconView.setImageResource(R.drawable.bing);
        }
        FrameLayout.LayoutParams searchIconParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, UIUtil.getResolutionValue(90));
        searchIconParams.rightMargin = UIUtil.getResolutionValue(1425);
        searchIconParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        searchText = new EditText(mContext);
        searchText.setTextSize(UIUtil.getTextDpiValue(36));
        // searchText.setTextColor(textColor);
        // searchText.setBackgroundResource(R.drawable.search_text_bg);
        searchText.setBackgroundColor(Color.WHITE);
        searchText.setFocusable(true);
        searchText.requestFocus();
        searchText.setSingleLine();
        searchText.onHoverChanged(true);
        searchText.setOnHoverListener(new OnHoverListener()
        {

            @Override
            public boolean onHover(View v, MotionEvent event)
            {
                SkyLogger.d(TAG, "searchText setOnFocusChangeListener, event:" + event);
                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER)
                {
                    mMyFocusFrame.setVisibility(View.VISIBLE);
                    v.requestFocus();
                }
                if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT)
                {
//                     v.clearFocus();
//                     mMyFocusFrame.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        searchText.setOnFocusChangeListener(new OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                SkyLogger.d(TAG, "searchText setOnFocusChangeListener, hasFocus:" + hasFocus);
                if (hasFocus)
                {
                    v.setBackgroundColor(Color.WHITE);
                    mMyFocusFrame.changeFocusPos(v);
                } else
                {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    v.setBackgroundResource(R.drawable.search_text_bg);
                }

            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                switch (actionId)
                {
                    case EditorInfo.IME_ACTION_SEARCH:
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_GO:
                    {
                        // 先隐藏键盘
                        ((InputMethodManager) mContext
                                .getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        SkyLogger.d(TAG, "searchText setOnEditorActionListener, content:"
                                + searchText.getText().toString());
                        if (searchText.getText().toString().trim().length() > 0)
                        {
                            searchClickListener.onTouch(searchText.getText().toString());

                            return true;
                        }
                    }
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        // searchText.setBackgroundResource(R.drawable.search_edit_selector);
        searchText.setPadding(UIUtil.getResolutionValue(15), UIUtil.getResolutionValue(4), 0, 0);
        try
        {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(searchText, R.drawable.text_cursor_style);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        searchText.setHintTextColor(Color.parseColor("#B9BDC0"));
        searchText.setHint(R.string.search_text_input);

        FrameLayout.LayoutParams searchTextParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(900), UIUtil.getResolutionValue(90));
        searchTextParams.leftMargin = UIUtil.getResolutionValue(525);
        searchTextParams.gravity = Gravity.CENTER_VERTICAL;

        searchButton = new ImageButton(mContext);
        searchButton.setBackgroundResource(R.drawable.search_btn_bg);
        // searchButton.setBackgroundColor(Color.parseColor("#BBDCE0"));

        searchButton.setImageResource(R.drawable.search_btn_icon);
        FrameLayout.LayoutParams searchButtonParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(100), UIUtil.getResolutionValue(90));
        searchButtonParams.leftMargin = UIUtil.getResolutionValue(1424);
        searchButtonParams.gravity = Gravity.CENTER_VERTICAL;
        searchButton.onHoverChanged(true);
        searchButton.setOnHoverListener(new OnHoverListener()
        {
            @Override
            public boolean onHover(View view, MotionEvent event)
            {
                SkyLogger.d(TAG, "searchButton onHover, event:" + event);
                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER)
                {
                    mMyFocusFrame.setVisibility(View.VISIBLE);
                    searchButton.requestFocus();
                    searchButton.requestFocusFromTouch();
                }
                if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT)
                {
                    // mMyFocusFrame.setVisibility(View.INVISIBLE);
                    // view.clearFocus();
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                SkyLogger.d(TAG, "searchButton setOnClickListener, content:"
                        + searchText.getText().toString());
                if (searchText.getText().toString().trim().length() > 0)
                {
                    searchClickListener.onTouch(searchText.getText().toString());

                } else
                {
                    searchText.requestFocus();
                    searchText.setHint(R.string.search_text_input);
                }

            }
        });
        searchButton.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View arg0, MotionEvent event)
            {
                // Log.d("BrowserActivity", "setOnTouchListener---"
                // +event.getAction()+" "+MotionEvent.ACTION_DOWN);
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    SkyLogger.d(TAG, "searchButton onTouch, content:"
                            + searchText.getText().toString());
                    if (searchText.getText().toString().trim().length() > 0)
                    {
                        searchClickListener.onTouch(searchText.getText().toString());

                    } else
                    {
                        // searchText.setHint(R.string.search_text_input);
                    }
                }

                return true;
            }
        });

        searchButton.setOnFocusChangeListener(new OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                SkyLogger.d(TAG, "searchButton setOnFocusChangeListener, hasFocus:" + hasFocus);
                if (hasFocus)
                {
                    //v.setBackgroundColor(Color.WHITE);
                    //v.setBackgroundColor(Color.parseColor("#0171D6"));
                    v.setBackgroundColor(Color.TRANSPARENT);
                    mMyFocusFrame.changeFocusPos(v);
                } else
                {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    v.setBackgroundResource(R.drawable.search_btn_bg);
                }

            }
        });

        this.addView(searchIconView, searchIconParams);
        this.addView(searchText, searchTextParams);
        this.addView(searchButton, searchButtonParams);

    }
}
