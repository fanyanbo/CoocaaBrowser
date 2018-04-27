/**
 * Copyright (C) 2012 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *           2016年5月4日         wen
 *
 */

package com.skyworth.tv_browser.util;

import android.view.KeyEvent;

import com.skyworth.framework.skycommondefine.SkyworthKeyMap.SkyworthKey;
import com.skyworth.framework.skysdk.jni.VirtualInput;
import com.skyworth.framework.skysdk.logger.SkyLogger;

/**
 * @ClassName BrowserVirtualInput
 * @Description 浏览器虚拟键值输入——光标移动功能
 * @author wen
 * @date 2016年5月4日
 * @version V1.0
 */
public class BrowserVirtualInput
{
    private final boolean HANDLE_RES = true;
    private final String TAG = "BrowserVirtualInput";
    private static BrowserVirtualInput instance;
    private VirtualInput virtualInput;
    // private float flag_X = 0.0f;
    // private float flag_y = 0.0f;
    //
    // // 根据不同分辨率电视，选择不同的上，下，左，右标准
    // private static float up_y = 0.0f;
    // private static float down_y = 0.0f;
    // private static float right_x = 0.0f;
    // private static float left_x = 0.0f;

    private int plus = 2; // 步进正常倍数
    private int max_speed = 200; // 最大步进速度
    // private int move_ud = 18; // 上下基础步进
    // private int move_lr = 20; // 左右基础步进
    private int move_length = 18;
    // private int move_length_lr = 20;
    private long firt_press = 0;
    private long last_press = 0;
    private int last_keycode;
    private int i_increase = 1;

    private BrowserVirtualInput()
    {
        virtualInput = new VirtualInput();
        virtualInput.open("com.skyworth.tv_browser"); // TODO 动态获取包名
    }

    public static BrowserVirtualInput getInstance()
    {
        if (instance == null)
        {
            instance = new BrowserVirtualInput();
        }
        return instance;
    }

    private int getMoveLength(int keyCode)
    {
        if ((last_press - firt_press < MAX_LIMIT) && (keyCode == last_keycode))
        {
            i_increase++;
            move_length += plus * i_increase;
            if (move_length >= max_speed)
            {
                move_length = max_speed;
            }
        } else
        {
            i_increase = 1;
            // move_length = move_ud;
            move_length = getDefaultMoveLength(keyCode);
        }

        last_keycode = keyCode;
        SkyLogger.d(TAG, "move_length---" + move_length);
        return move_length;
    }

    private final int MAX_LIMIT = 220;
    /**
     * @Fields DEFAULT_MOVE_LEN_V 垂直方向（上、下）移动时默认长度
     */
    private final int DEFAULT_MOVE_LEN_V = 18;
    /**
     * @Fields DEFAULT_MOVE_LEN_H 水平方向（左、右）移动时默认长度
     */
    private final int DEFAULT_MOVE_LEN_H = 20;

    private int getDefaultMoveLength(int keyCode)
    {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)
        {
            return DEFAULT_MOVE_LEN_V;
        } else
        {
            return DEFAULT_MOVE_LEN_H;
        }
    }

    public boolean moveUp(int keyCode)
    {
        int moveLen = getMoveLength(keyCode);
        SkyLogger.d(TAG, "up---move_length---" + moveLen);
        virtualInput.moveMouse(0, -moveLen);
        return HANDLE_RES;
    }

    public boolean moveDown(int keyCode)
    {
        int moveLen = getMoveLength(keyCode);

        SkyLogger.d(TAG, "down---move_length---" + moveLen);
        virtualInput.moveMouse(0, moveLen);
        return HANDLE_RES;
    }

    public boolean moveRight(int keyCode)
    {
        int moveLen = getMoveLength(keyCode);
        SkyLogger.d(TAG, "right---move_length---" + moveLen);
        virtualInput.moveMouse(moveLen, 0);
        return HANDLE_RES;
    }

    public boolean moveLeft(int keyCode)
    {
        int moveLen = getMoveLength(keyCode);
        // SkyLogger.d(TAG, "last_press - firt_press----" + (last_press - firt_press));
        SkyLogger.d(TAG, "left---move_length---" + moveLen);
        virtualInput.moveMouse(-moveLen, 0);
        return HANDLE_RES;
    }

    public boolean clickCenter()
    {
        SkyLogger.d(TAG, "press center....");
        virtualInput.clickButton(SkyworthKey.SKY_KEY_MOUSE_OK.key_value());
        return HANDLE_RES;
    }
}
