/**
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *   1.0       2016-4-19         wolfboyjiang
 *
 */

package com.skyworth.tv_browser.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.skyworth.tv_browser.R;
import com.skyworth.tv_browser.util.UIUtil;

/**
 * @ClassName MenuItemDialog
 * @Description 菜单元素中点击后，响应对应元素的dialog
 * @author wolfboyjiang
 * @date 2016-4-19
 * @version 1
 */
public class MenuItemDialog extends Dialog
{
    private String TAG = "BrowserActivity";
    private Context mContext;

    /**
     * @Description 构造一个dialog
     * @param context
     *            上下文
     */
    public MenuItemDialog(Context context)
    {
        super(context,R.style.menuItemDialog);
        mContext = context;
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.LEFT);
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
        lp.width = UIUtil.getResolutionValue(1920); //
        lp.height = UIUtil.getResolutionValue(1080); //
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void setContentView(View view, LayoutParams params)
    {
        super.setContentView(view, params);
    }

    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
    }

}
