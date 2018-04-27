package com.skyworth.tv_browser;

import org.apache.cordova.CordovaWebView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class VoiceControllBrowser
{
    private CordovaWebView wv = null;
    private static final String tag = "BrowserActivity";
    private boolean isJSCodeLoaded = false;
    static final String JSCodeAddress = "http://www.skyworth-cloud.com/WebApp/voice_browser/VoiceControlWebBrowser.js";
    private static final String INSERT_SCRIPT_FUNCTION_STRING = "javascript:(function(url,position,last){var script=document.createElement('script');script.type='text/javascript';script.src=url;if(!position){position=document.head;}if(!last){if(position.firstElementChild&&position.firstElementChild.src==url){return;}position.insertBefore(script,position.firstElementChild);}else{if(position.lastElementChild&&position.lastElementChild.src==url){return;}document.head.appendChild(script);}})";
    protected static final int VOICEBROWSERZOOMIN = 0;
    private static final int VOICEBROWSERZOOMOUT = 1;
    private static final int VOICEBROWSERPAGEDOWN = 2;
    private static final int VOICEBROWSERPAGEUP = 3;

    public VoiceControllBrowser(CordovaWebView webview)
    {
        wv = webview;
    }

    public void insertJSCodeAfter(String JSCodeAddress)
    {
        // Log.i(tag, "VoiceBrowser insertJSCodeAfter");
        String str = INSERT_SCRIPT_FUNCTION_STRING;
        str += "('" + JSCodeAddress + "',true,true)";
        wv.loadUrl(str);
    }

    public void insertJSCodeBefore(String JSCodeAddress)
    {
        // Log.i(tag, "VoiceBrowser insertJSCodeBefore");
        String str = INSERT_SCRIPT_FUNCTION_STRING;
        str += "('" + JSCodeAddress + "')";
        wv.loadUrl(str);
    }

    public void clickByText(String text)
    {
        // Log.i(tag, "VoiceBrowser openURL");
        if (!isJSCodeLoaded)
        {
            insertJSCodeAfter(JSCodeAddress);
        }
        wv.loadUrl("javascript:if(window.openURLByText){openURLByText('" + text + "');}");
    }

    public void showLog(String text)
    {
        Log.i(tag, "VoiceBrowser showLog:" + text);
    }

    public void setJSCodeLoaded()
    {
        // Log.i(tag, "VoiceBrowser setJSCodeLoaded");
        isJSCodeLoaded = true;
    }

    public void clearIsJSCodeLoad()
    {
        // Log.i(tag, "VoiceBrowser clearIsJSCodeLoad");
        isJSCodeLoaded = false;
    }

    public Boolean getJSCodeLoadState()
    {
        // Log.i(tag, "VoiceBrowser getJSCodeLoadState");
        return isJSCodeLoaded;
    }

    // @Override
    public void stopLoading()
    {
        // Log.i(tag, "VoiceBrowser stopLoading");
        // TODO Auto-generated method stub
        wv.stopLoading();
    }

    // @Override
    public void reload()
    {
        // Log.i(tag, "VoiceBrowser reload");
        // TODO Auto-generated method stub
        wv.reload();
    }

    // @Override
    public void goBack()
    {
        // Log.i(tag, "VoiceBrowser goBack");
        // TODO Auto-generated method stub
        wv.backHistory();
    }

    // @Override
    public void goForward()
    {
        // Log.i(tag, "VoiceBrowser goBackOrForward");
        // TODO Auto-generated method stub
        wv.goForward();
    }

    public void myPageUp()
    {
        // Log.i(tag, "VoiceBrowser pageUp");
        // TODO Auto-generated method stub
        myPageUp(false);
    }

    public void myPageDown()
    {
        // Log.i(tag, "VoiceBrowser pageDown");
        // TODO Auto-generated method stub
        myPageDown(false);
    }

    public void myPageUp(boolean top)
    {
        // Log.i(tag, "VoiceBrowser pageUp para");
        // TODO Auto-generated method stub
        Message msg = myUIHandler.obtainMessage();
        msg.what = VOICEBROWSERPAGEUP;
        msg.obj = top;
        myUIHandler.sendMessage(msg);
    }

    public void myPageDown(boolean bottom)
    {
        // Log.i(tag, "VoiceBrowser pageDown para");
        // TODO Auto-generated method stub
        Message msg = myUIHandler.obtainMessage();
        msg.what = VOICEBROWSERPAGEDOWN;
        msg.obj = bottom;
        myUIHandler.sendMessage(msg);
    }

    public void myZoomIn()
    {
        // Log.i(tag, "VoiceBrowser myZoomIn");
        // TODO Auto-generated method stub
        myUIHandler.sendEmptyMessage(VOICEBROWSERZOOMIN);
    }

    public void myZoomOut()
    {
        // Log.i(tag, "VoiceBrowser zoomOut");
        // TODO Auto-generated method stub
        myUIHandler.sendEmptyMessage(VOICEBROWSERZOOMOUT);
    }

    // public void setUserAgentString(String type)
    // {
    // wv.getSettings().setUserAgentString(type);
    //
    // }

    public final Handler myUIHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Log.i(tag, "myUIHandler--message---" + msg.what);
            // TODO Auto-generated method stub
            if (msg.what == VOICEBROWSERZOOMIN)
            {
                // wv.zoomIn();
            } else if (msg.what == VOICEBROWSERZOOMOUT)
            {
                // wv.zoomOut();
            } else if (msg.what == VOICEBROWSERPAGEDOWN)
            {
                if ((Boolean) msg.obj)
                {
                    // wv.pageDown(true);
                } else
                {
                    // wv.pageDown(false);
                }
            } else if (msg.what == VOICEBROWSERPAGEUP)
            {
                if ((Boolean) msg.obj)
                {
                    // wv.pageUp(true);
                } else
                {
                    // wv.pageUp(false);
                }
            }
        }
    };

}
