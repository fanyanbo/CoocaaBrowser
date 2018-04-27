package com.skyworth.tv_browser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaInterfaceImpl.CordovaInterfaceListener;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewEngine;
import org.apache.cordova.CordovaWebViewImpl;
import org.coocaa.webview.CoocaaOSConnecter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.framework.skysdk.properties.SkyGeneralProperties;
import com.skyworth.framework.skysdk.properties.SkyGeneralProperties.GeneralPropKey;
import com.skyworth.tv_browser.MenuController.BrowserControllerListener;
import com.skyworth.tv_browser.data.BookmarkSupport;
import com.skyworth.tv_browser.data.HistorySupport;
import com.skyworth.tv_browser.data.WindowData;
import com.skyworth.tv_browser.ui.HomePage;
import com.skyworth.tv_browser.ui.MenuIcon;
import com.skyworth.tv_browser.ui.MenuToolbarLayout;
import com.skyworth.tv_browser.ui.ProgressBarLayout;
import com.skyworth.tv_browser.ui.interfaces.HomepageListener;
import com.skyworth.tv_browser.ui.interfaces.LordErrorListener;
import com.skyworth.tv_browser.ui.interfaces.MenuIconClickLisenter;
import com.skyworth.tv_browser.ui.interfaces.MenuToolControlItemListener;
import com.skyworth.tv_browser.ui.nonet.LordErrorPage;
import com.skyworth.tv_browser.util.BrowserDefine;
import com.skyworth.tv_browser.util.BrowserVirtualInput;
import com.skyworth.tv_browser.util.UIUtil;
import com.skyworth.ui.api.SkyToastView;
import com.skyworth.ui.api.SkyToastView.ShowTime;
import com.skyworth.ui.blurbg.BlurBgLayout;

public class BrowserActivity extends Activity implements BrowserControllerListener,
        CordovaInterface
{

    private static String TAG = "BrowserActivity";
    /**
     * @Fields mBgLayout 主界面背景布局
     */
    private BlurBgLayout mBgLayout = null;
    private Context mContext;
    /**
     * @Fields browserMainLayout 主activity的xml中根layout
     */
    private FrameLayout browserMainLayout = null;
    /**
     * @Fields menuToolbarLayout 菜单布局layout
     */
    private HomePage homepage = null;
    private MenuToolbarLayout menuToolbarLayout = null;
    /**
     * @Fields menuIconImageView 菜单提示按钮
     */
    private MenuIcon menuIcon = null;
    private LordErrorPage lordErrorPage = null;
    private CordovaInterfaceImpl cordovaImpl = null;
    private CordovaWebView webView = null;
    private ProgressBarLayout progressView = null;
    protected boolean keepRunning = true;
    private VoiceControllBrowser voiceControllBrowser = null;

    private static String DEFAULT_URL = "http://cn.bing.com";
    private MenuController menuController = null;

    private final Handler mHandler = new MyHandler(this);
    private static final int SAVE_BOOK_MARK = 2001;
    private static final int SHOW_TOAST = 2002;
    private static final int SHOW_CAPTURE_CHANAGE = 2003;
    private static final int UPDATE_PROGRESS = 2004;
    private static final int ADD_HISTORY = 2005;
    private static final int UPDATE_COLLECT_STATUS = 2006;
    private static final int SHOW_MENU_ICON = 2008;
    private static final int UPDATE_ERROR_PAGE = 2009;
    private static int windowPosition = 0;

    List<BookmarkSupport> collectBookmarks = null;
    private boolean isTheWebCollected = false;
    private boolean isCanRefrush = false;
    private boolean isNoMenu = false;
    private boolean isCanBack = false;
    private boolean isCanforward = false;
    private ConnectivityManager connectManager;
    private Thread myExitBroserThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mContext = this;
        UIUtil.setDpiDiv_Resolution(this.getApplicationContext());
        connectManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        initScrollEdge();
        // 初始化的顺序很重要，不要变化
        Config.init(this);
        cordovaImpl = new CordovaInterfaceImpl(this);
        cordovaImpl.setCordovaInterfaceListener(new CordovaInterfaceListenerImpl());

        initMenuController();
        initView();

        SkyLogger.i("uidpi", "356:getResolutionValue:" + UIUtil.getResolutionValue(356)
                + ", getTextDpiValue" + UIUtil.getTextDpiValue(356));
        // loadUrl(getBrowserHomepage());
        onStartInetentUrl();

        voiceControllBrowser = new VoiceControllBrowser(webView);

        // 进度条测试代码
        // mHandler.sendMessageDelayed(mHandler.obtainMessage(UPDATE_PROGRESS, progressValue, 0),
        // 1000);
    }

    private String formatCommand(String str)
    {
        String res = null;
        final String functionNames[] = { "clickByText", "goBack", "goForward", "stopLoading",
                "reload", "myPageDown", "myPageUp", "setUserAgentString", "myZoomIn", "myZoomOut" };
        if (str != null && !str.equals(""))
        {
            SkyLogger.i(TAG, str);
            if (str.startsWith("javascript:") || str.startsWith("http://"))
            {
                res = str;
            } else
            {
                for (String funName : functionNames)
                {
                    if (str.startsWith(funName))
                    {
                        res = "javascript:if(window.voiceBrowser){window.voiceBrowser." + str
                                + "};";
                        break;
                    }
                }
                SkyLogger.i(TAG, "formatCommand-----" + res);
            }
        }
        return res;
    }

    /**
     * @Description 浏览器启动的时候如果是第三方启动的浏览器，则加载制定网页<br/>
     *              void
     * @date 2016-6-15
     */
    private void onStartInetentUrl()
    {
        SkyLogger.d(TAG, "getBrowserHomepage");
        String defaukltUrl = null;
        isNoMenu = false;
        // 获取第三方调用浏览器的启动方式
        boolean showMenu = true;
        String showMenuStr = this.getIntent().getStringExtra("showMenu");
        if (showMenuStr != null && "false".equals(showMenuStr))
        {
            showMenu = false;
        }else {
            showMenu = this.getIntent().getBooleanExtra("showMenu", true);
        }
        SkyLogger.d(TAG, "getBrowserHomepage showMenu:" + showMenu);
        if (!showMenu)
        {
            defaukltUrl = this.getIntent().getStringExtra("url");
            isNoMenu = true;
            if (defaukltUrl == null || defaukltUrl.equals(""))
            {
                defaukltUrl = "http://cn.bing.com/";
            } else
            {
                loadUrl(defaukltUrl);
            }
            SkyLogger.d(TAG, "getBrowserHomepage showMenu:" + showMenu + ",defaukltUrl:"
                    + defaukltUrl);
        } else
        {
            // android标准的浏览器跳转方式
            SkyLogger.d(TAG, "getData");
            Uri uri = this.getIntent().getData();
            Bundle bundle = this.getIntent().getExtras();
            if (uri != null)
            {
                SkyLogger.d(TAG, "Data is not null----" + uri.toString());
                isNoMenu = true;
                defaukltUrl = uri.toString();
                if (defaukltUrl.startsWith("about:blank"))
                {
                    defaukltUrl = "http://cn.bing.com/";
                } else
                {
                    loadUrl(defaukltUrl);
                }
            } else if (bundle != null)
            {// 将启动浏览器的方式换成携带bundle，而不是data
                SkyLogger.d(TAG, "bundle  no null");
                String soundStr = bundle.getString("keyword");// 对接语音的逻辑
                String firstUrl = bundle.getString("url");
                if (firstUrl != null && !"".equals(firstUrl))
                {
                    SkyLogger.d(TAG, "bundle  start url----" + firstUrl);
                    defaukltUrl = firstUrl;
                    isNoMenu = true;
                    loadUrl(defaukltUrl);
                } else
                {
                    defaukltUrl = "http://cn.bing.com/";
                }
                if (soundStr != null)
                {
                    String sub_url = formatCommand(soundStr);
                    SkyLogger.d(TAG, "voice  start url-sub_url:" + sub_url + ",defaukltUrl:"
                            + defaukltUrl);
                    if (sub_url == null || sub_url.contains("clickByText"))
                    {
                        defaukltUrl = BrowserDefine.DEFAULT_URL;
                    } else
                    {
                        isNoMenu = true;
                        defaukltUrl = sub_url;
                        loadUrl(defaukltUrl);
                    }
                } else
                {
                    defaukltUrl = BrowserDefine.DEFAULT_URL;
                }

            }
        }
    }

    /**
     * @Description 获取浏览器启动的主页面url<br/>
     * @return String 返回首页
     * @date 2016-4-28
     */
    private String getBrowserHomepage()
    {
        SkyLogger.d(TAG, "getBrowserHomepage");
        String defaukltUrl = null;

        // 加载默认url
        defaukltUrl = SkyGeneralProperties.getProperty(GeneralPropKey.BROWSER_HOMEPAGE);
        if (defaukltUrl == null | "".equals(defaukltUrl))
        {
            defaukltUrl = "http://cn.bing.com/";
        }
        isNoMenu = false;
        DEFAULT_URL = defaukltUrl;

        SkyLogger.d(TAG, "getBrowserHomepage,defaukltUrl:" + defaukltUrl + ",isNoMenu:" + isNoMenu);
        return defaukltUrl;
    }

    public View reConstructWebView(String ua)
    {
        SkyLogger.d(TAG, "reConstructWebView,ua:" + ua);
        if (webView != null)
        {
            webView.handleDestroy();
            webView = null;
            if (webView == null)
            {
                init(ua);
            }
        } else
        {
            init(ua);
        }
        return webView.getView();
    }

    private void initMenuController()
    {
        menuController = MenuController.getInstance(mContext);
        menuController.setBrowserSettingsListener(this);
    }

    private String getLocalUA()
    {
        String ua = BrowserDefine.IPAD_USERAGENT;
        if (menuController != null)
        {
            ua = menuController.getUA();
            if (ua == null || "".equals(ua))
            {
                menuController.setUA(BrowserDefine.IPAD_USERAGENT);
            } else if (!ua.toLowerCase().contains("windows") && !ua.toLowerCase().contains("ipad"))
            {
                menuController.setUA(BrowserDefine.IPAD_USERAGENT);
            }
            SkyLogger.d(TAG, "getLocalUA,ua:" + ua);
        }
        return ua;
    }

    @SuppressLint("NewApi")
    private void initView()
    {
        // 获取主布局
        // browserMainLayout = (RelativeLayout) this.findViewById(R.id.browser_main_layout);
        browserMainLayout = new FrameLayout(mContext);
        browserMainLayout.setLayoutParams(new FrameLayout.LayoutParams(UIUtil
                .getResolutionValue(1920), UIUtil.getResolutionValue(1080)));
        // add 背景
        mBgLayout = new BlurBgLayout(mContext);
        mBgLayout.setPageType(BlurBgLayout.PAGETYPE.FIRST_PAGE);
        browserMainLayout.addView(
                mBgLayout,
                new FrameLayout.LayoutParams(UIUtil.getResolutionValue(1920), UIUtil
                        .getResolutionValue(1080)));

        // add 主webview
        browserMainLayout.addView(reConstructWebView(getLocalUA()), new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(1920), UIUtil.getResolutionValue(1080)));

        final int PROGRESS_HEIGHT = UIUtil.getResolutionValue(33); // 34;
        progressView = new ProgressBarLayout(mContext);
        FrameLayout.LayoutParams progressLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, PROGRESS_HEIGHT);
        progressLayoutParams.gravity = Gravity.TOP;
        // progressLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        progressView.setLayoutParams(progressLayoutParams);
        browserMainLayout.addView(progressView);

        // homepage
        homepage = new HomePage(mContext);
        homepage.setHomepageListener(homepageListener);
        browserMainLayout.addView(homepage, new FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
        // 菜单提示图标
        menuIcon = new MenuIcon(mContext);
        menuIcon.setFocusable(false);
        // menuIcon.setVisibility(View.GONE);
        menuIcon.bringToFront();
        FrameLayout.LayoutParams menuIconLayoutParams = new FrameLayout.LayoutParams(
                UIUtil.getResolutionValue(200), UIUtil.getResolutionValue(105));
        menuIconLayoutParams.leftMargin = UIUtil.getResolutionValue(1820);
        menuIconLayoutParams.topMargin = UIUtil.getResolutionValue(385);
        browserMainLayout.addView(menuIcon, menuIconLayoutParams);

        // 菜单
        menuToolbarLayout = new MenuToolbarLayout(mContext);
        menuToolbarLayout.setVisibility(View.GONE);
        menuToolbarLayout.setControlItemListener(menuToolControlItemLisentner);
        FrameLayout.LayoutParams menuToolbarLayoutParams = new FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                UIUtil.getResolutionValue(265));
        menuToolbarLayoutParams.gravity = Gravity.BOTTOM;
        browserMainLayout.addView(menuToolbarLayout, menuToolbarLayoutParams);

        setContentView(browserMainLayout);
    }

    private boolean isWebCollected(String url)
    {
        boolean collected = false;
        for (BookmarkSupport bookmarkSupport : collectBookmarks)
        {
            if (bookmarkSupport.getUrl().equals(url))
            {
                return true;
            }
        }
        return collected;
    }

    private Bitmap captureScreen()
    {
        View cv = BrowserActivity.this.getWindow().getDecorView();
        Bitmap bmp = null;
        if (UIUtil.getResolutionDiv() == 0.5)
        {
            bmp = Bitmap.createBitmap(2 * cv.getWidth() / 3, 2 * cv.getHeight() / 3,
                    Bitmap.Config.RGB_565);
        } else
        {
            bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Bitmap.Config.RGB_565);
        }
        // Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        cv.draw(canvas);
        return zoomImg(bmp, UIUtil.getResolutionValue(500), UIUtil.getResolutionValue(280));

    }

    /**
     * 处理图片
     * 
     * @param bm
     *            所要转换的bitmap
     * @param newWidth新的宽
     * @param newHeight新的高
     * @return 指定宽高的bitmap
     */
    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight)
    {
        if (bm == null)
        {
            return null;
        }
        SkyLogger.d(TAG, "bm before scale size:" + bm.getByteCount());
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        SkyLogger.d(TAG, "newbm after scale size:" + newbm.getByteCount());
        return newbm;
    }

    private boolean isErrorpageVisible()
    {
        if (lordErrorPage != null)
        {
            if (lordErrorPage.getVisibility() == View.VISIBLE)
            {
                return true;
            }
        }

        return false;
    }

    private boolean isHomepageVisible()
    {
        if (homepage != null)
        {
            if (homepage.getVisibility() == View.VISIBLE)
            {
                return true;
            }
        }

        return false;
    }

    private void showHomePage(boolean flag)
    {
        if (homepage != null)
        {
            if (flag)
            {
                homepage.setVisibility(View.VISIBLE);
                homepage.getFocus();
            } else
            {
                homepage.setVisibility(View.GONE);
            }
        }
    }

    private boolean isMenuToolBarVisible()
    {
        if (menuToolbarLayout != null)
        {
            if (menuToolbarLayout.getVisibility() == View.VISIBLE)
            {
                return true;
            }
        }
        return false;
    }

    private void showMenuToolBar(boolean flag)
    {
        if (menuToolbarLayout != null)
        {
            if (flag)
            {
                SkyLogger.d(TAG, "isTheWebCollected:" + isTheWebCollected + ",iscanback:"
                        + isCanBack + ",iscanfoword：" + isCanforward + ",isCanRefrush:"
                        + isCanRefrush + ",isHomepageVisible:" + isHomepageVisible());
                menuToolbarLayout.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                menuToolbarLayout.show(isTheWebCollected, isCanBack, isCanforward, isCanRefrush,
                        isHomepageVisible());
                menuToolbarLayout.getfocus();
            } else
            {
                menuToolbarLayout.setVisibility(View.INVISIBLE);
                menuIcon.setVisibility(View.VISIBLE);
                homepage.setFocusVisible(true);
            }

        } else
        {

        }
    }

    private static boolean isBackKeyPressed = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        SkyLogger.d(TAG,
                "browserActivity onKeyDown,keycode:" + keyCode + ",keyevent:" + event.toString()
                        + ",isNoMenu:" + isNoMenu);
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_MENU:
                if (!isNoMenu)
                {
                    if (isMenuToolBarVisible())
                    {
                        showMenuToolBar(false);
                        return true;
                    }
                    if (menuController != null)
                    {
                        SkyLogger.d(TAG, "getCapture windowPosition:" + windowPosition);
                        Message msg = mHandler.obtainMessage();
                        msg.what = SHOW_CAPTURE_CHANAGE;
                        msg.arg1 = windowPosition;
                        // msg.obj = captureScreen();
                        mHandler.sendMessage(msg);
                        // for (int i = 0; i < 5; i++)
                        // {
                        // menuController.getCapture(windowPosition, webView);
                        // }
                    }
                    // menuToolbarLayout.updateToolBarStatus();
                    if (isHomepageVisible())
                    {
                        isCanBack = false;
                        isCanforward = false;
                        isCanRefrush = false;
                        if (webView.getUrl() != null)
                        {
                            isCanforward = true;
                        }
                        homepage.setFocusVisible(false);
                    } else
                    {
                        isCanBack = webView.canGoBack();
                        if (!isCanBack && !isHomepageVisible())
                        {
                            isCanBack = true;
                        }
                        isCanforward = webView.canGoForward();
                    }
                    showMenuToolBar(true);
                    return true;
                }

                break;
            case KeyEvent.KEYCODE_BACK:
                SkyLogger.d(TAG, "isBackKeyPressed:" + isBackKeyPressed + ",keyCode:" + keyCode);

                if (isMenuToolBarVisible())
                {
                    showMenuToolBar(false);
                    return true;
                }

                if (!webView.canGoBack() && !isHomepageVisible() && !isNoMenu)
                {
                    showHomePage(true);
                    return true;
                } else
                {
                    if (!isBackKeyPressed && !isNoMenu)
                    {
                        isBackKeyPressed = true;
                        SkyToastView skyToastView = new SkyToastView(mContext);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                        skyToastView.setTostString(mContext.getString(R.string.exit));
                        skyToastView.showToast(ShowTime.SHOTTIME, params);
                        myExitBroserThread = new Thread(new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                try
                                {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                isBackKeyPressed = false;
                            }
                        });
                        myExitBroserThread.start();
                        return true;
                    }
                }

                break;
            case KeyEvent.KEYCODE_1:
                // CollectionLayout mCollectLayout = new CollectionLayout(this);
                // setContentView(mCollectLayout);
                showLordErrorPage(true);
                break;

            case KeyEvent.KEYCODE_2:
                // HistoryLayout mHistoryLayout = new HistoryLayout(this);
                // setContentView(mHistoryLayout);
                hideErrorPage();
                break;

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        SkyLogger.d(TAG, "dispatchKeyEvent, keyevent = " + event);
        final int keyCode = event.getKeyCode();
        if (!isHomepageVisible() && !isErrorpageVisible() && !isMenuToolBarVisible())
        {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        return BrowserVirtualInput.getInstance().clickCenter();
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        // SkyLogger.i(TAG, "flay_y = " + flag_y + ", down_y = " + down_y);

                        if (flag_y > down_y)
                        {
                            if (getTopView().canScrollVertically(SCROLL_DEFAULT_V))
                            {
                                SkyLogger.d(TAG, "wv.canScrollVertically(100)---");
                                getTopView().scrollBy(0, SCROLL_DEFAULT_V);
                            } else
                            {
                                SkyLogger.e(TAG, "not canScrollVertically, not deal");
                                // wv.loadUrl("javascript:window.scrollBy(" + 0 +
                                // "," + 30 + ")");
                                // TODO 仿照水平滚动，使用js接口？
                                // wv.pageDown(true);
                            }
                        }

                        return BrowserVirtualInput.getInstance().moveDown(keyCode);
                    case KeyEvent.KEYCODE_DPAD_LEFT:

                        if (flag_X < left_x)
                        {
                            scrollWebView(-SCROLL_DEFAULT_H);
                        }

                        return BrowserVirtualInput.getInstance().moveLeft(keyCode);
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if (flag_X > right_x)
                        {
                            scrollWebView(SCROLL_DEFAULT_H);
                        }
                        return BrowserVirtualInput.getInstance().moveRight(keyCode);
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (flag_y < up_y)
                        {
                            if (getTopView().canScrollVertically(-SCROLL_DEFAULT_V))
                            {
                                getTopView().scrollBy(0, -SCROLL_DEFAULT_V);
                            } else
                            {
                                SkyLogger.e(TAG, "not canScrollVertically, not deal");
                                // TODO
                                // wv.pageUp(true);
                            }
                        }

                        return BrowserVirtualInput.getInstance().moveUp(keyCode);
                        // TODO 长按确认键
                    case KeyEvent.KEYCODE_BACK:
                    case KeyEvent.KEYCODE_MENU:
                    default:
                        break;
                }
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev)
    {
        SkyLogger.i(TAG, "dispatchGenericMotionEvent(), x= " + ev.getX() + " y= " + ev.getY());
        if (!isHomepageVisible() || !isErrorpageVisible())
        {
            flag_X = ev.getX();
            flag_y = ev.getY();
        }
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        SkyLogger.d(TAG, "onTouchEvent,event:" + event);
        if (isMenuToolBarVisible())
        {
            showMenuToolBar(false);
        }
        return super.onTouchEvent(event);
    }

    /**
     * @Description 利用JS方法水平滚动网页，TODO 此方法从原来浏览器复制，未验证<br/>
     * @param value
     *            滚动的像素值，可为负
     * @date 2016年5月10日
     */
    private void scrollWebView(int value)
    {
        if (webView != null)
        {
            webView.loadUrl("javascript:window.scrollBy(" + (value) + "," + 0 + ")");
            // wv.loadUrl("javascript:window.scrollBy(" + 50 + "," + 0 + ")");
        }
    }

    private final int SCROLL_DEFAULT_H = 50;
    private final int SCROLL_DEFAULT_V = 100;

    private float flag_X = 0.0f;
    private float flag_y = 0.0f;

    // 根据不同分辨率电视，选择不同的上，下，左，右标准边界
    private static float up_y = 0.0f;
    private static float down_y = 0.0f;
    private static float right_x = 0.0f;
    private static float left_x = 0.0f;

    /**
     * @Description 初始化上、下、左、右滚动时的边界，需要根据不同分辨率调整边界值<br/>
     * @date 2016年5月4日
     */
    private void initScrollEdge()
    {
        left_x = UIUtil.getResolutionValue(150);
        right_x = UIUtil.getResolutionValue(1770);
        up_y = UIUtil.getResolutionValue(150);
        down_y = UIUtil.getResolutionValue(930);
    }

    // public OnHoverListener myOnHoverListener = new OnHoverListener()
    // {
    // @Override
    // public boolean onHover(View v, MotionEvent event)
    // {
    // SkyLogger.e(TAG, "onHover(), x,y--" + event.getX() + "  " + event.getY());
    // flag_X = event.getX();
    // flag_y = event.getY();
    // return false;
    // }
    // };

    @Override
    public void onSaveBookmarkFinish(boolean success, String errorMsg)
    {
        Message msg = mHandler.obtainMessage();
        msg.what = SHOW_TOAST;
        msg.obj = errorMsg;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onDelBookmarkFinish(boolean success, String errorMsg)
    {
        Message msg = mHandler.obtainMessage();
        msg.what = SHOW_TOAST;
        msg.obj = errorMsg;
        mHandler.sendMessage(msg);

    }

    @Override
    public void onClearCacheFinish()
    {
        Message msg = mHandler.obtainMessage();
        msg.what = SHOW_TOAST;
        msg.obj = mContext.getString(R.string.clear_cache_ok);
        mHandler.sendMessage(msg);

    }

    @Override
    public void onClearHistoryFinish()
    {
        Message msg = mHandler.obtainMessage();
        msg.what = SHOW_TOAST;
        msg.obj = mContext.getString(R.string.clear_history_ok);
        mHandler.sendMessage(msg);

    }

    @Override
    public void onUAChanged(String ua)
    {
        Message msg = mHandler.obtainMessage();
        msg.what = SHOW_TOAST;
        msg.obj = ua;
        mHandler.sendMessage(msg);

    }

    @Override
    public void onCaptureChanged(int location, Bitmap bm)
    {
        SkyLogger.d(TAG, "onCaptureChanged location:" + location);
        Message msg = mHandler.obtainMessage();
        msg.what = SHOW_CAPTURE_CHANAGE;
        msg.arg1 = location;
        msg.obj = bm;
        if (bm != null)
        {
            SkyLogger.d(TAG, "onCaptureChanged no null:");
            // msg.obj = bm;
        } else
        {
            // msg.obj = captureScreen();
            SkyLogger.d(TAG, "onCaptureChanged bm null:");
        }

        mHandler.sendMessage(msg);
    }

    @Override
    public void onExitBrowser()
    {
        SkyLogger.d(TAG, "webview on");
        BrowserActivity.this.finish();

    }

    private HomepageListener homepageListener = new HomepageListener()
    {

        @Override
        public void onSearchTouch(String content)
        {
            DEFAULT_URL = getBrowserHomepage();
            String search = "";
            if (content != null && !"".equals(content) && content.startsWith("http://"))
            {
                search = content;
            } else
            {
                search = DEFAULT_URL + "/search?q=" + content;

            }

            SkyLogger.d(TAG, "onSearchTouch, content:" + content + ",search:" + search);
            loadUrl(search);
        }
    };

    private MenuIconClickLisenter menuIconClickLisenter = new MenuIconClickLisenter()
    {

        @Override
        public void onclick()
        {
            if (!isNoMenu)
            {
                // menuToolbarLayout.updateToolBarStatus();
                SkyLogger.d(TAG, "isTheWebCollected:" + isTheWebCollected + ",iscanback:"
                        + isCanBack + ",iscanfoword：" + isCanforward + ",isCanRefrush:"
                        + isCanRefrush);
                homepage.setFocusVisible(false);
                showMenuToolBar(true);
            }

        }
    };

    private void updateMenuToolBarStatus()
    {
        if (menuToolbarLayout != null)
        {
            isCanBack = true;
            isCanforward = webView.canGoForward();
            isCanRefrush = true;
            menuToolbarLayout.updateBarState(isCanBack, isCanforward, isCanRefrush);

        }

    }

    private MenuToolControlItemListener menuToolControlItemLisentner = new MenuToolControlItemListener()
    {

        @Override
        public void setUA(String UAName)
        {
            SkyLogger.d(TAG, "webview setUA，UAName：" + UAName);
            webView.setUserAgentString(UAName);
            menuController.setUA(UAName);
        }

        @Override
        public void saveBookMark()
        {
            SkyLogger.d(TAG, "webview saveBookMark");
            // menuController.saveBookMark(webView);
            mHandler.sendEmptyMessage(SAVE_BOOK_MARK);

        }

        @Override
        public void removeBookmark()
        {
            SkyLogger.d(TAG, "webview removeBookMark, web url:" + webView.getUrl());
            menuController.removeBookmark(webView.getUrl());

        }

        @Override
        public void refresh(boolean isLoading)
        {
            SkyLogger.d(TAG, "webview isloading");
            menuController.refresh(webView, isLoading);
            isCanRefrush = false;
            // loadUrl("http://www.sina.com.cn/");

        }

        @Override
        public String getUA()
        {
            SkyLogger.d(TAG, "webview getUA,:" + menuController.getUA());
            return menuController.getUA();
        }

        @Override
        public List<HistorySupport> getHistoryList()
        {
            List<HistorySupport> historyList = menuController.getHistory();
            return historyList;
        }

        @Override
        public List<BookmarkSupport> getCollectList()
        {
            List<BookmarkSupport> bookmarkDataList = menuController.getBookMarks();
            return bookmarkDataList;
        }

        @Override
        public boolean removeCollect(String url)
        {
            menuController.removeBookmark(url);
            return true;
        }

        @Override
        public void jumpToWebsite(String url)
        {
            if (url == null || "".equals(url))
            {
                showHomePage(true);
            } else
            {
                showHomePage(false);
                loadUrl(url);
            }
        }

        @Override
        public void forward()
        {
            SkyLogger.d(TAG, "webview forward");
            if (isHomepageVisible())
            {
                showHomePage(false);
                isCanBack = true;
                isCanRefrush = true;
                isCanforward = true;
            } else
            {
                menuController.forward(webView);
            }

        }

        @Override
        public void exit()
        {
            SkyLogger.d(TAG, "loadUrl");
            menuController.exit();

        }

        @Override
        public void clearHistory()
        {
            SkyLogger.d(TAG, "clearHistory");
            menuController.clearHistory();

        }

        @Override
        public void clearCache()
        {
            SkyLogger.d(TAG, " clearCache");
            menuController.clearCache(webView);

        }

        @Override
        public void back()
        {
            SkyLogger.d(TAG, "webview back");
            if (!isHomepageVisible() && !webView.canGoBack())
            {
                showHomePage(true);
                isCanBack = false;
                isCanforward = true;
                isCanRefrush = false;
            } else
            {
                menuController.back(webView);
            }

        }

        @Override
        public void removeWindow(int index)
        {
            SkyLogger.d(TAG, "removeWindow,index:" + index);
            menuController.removeWindow(index);
            if (index > 0)
            {
                windowPosition = index - 1;
                if (menuController.getWindows().get(index - 1).getUrl() != null)
                {
                    showHomePage(false);
                    loadUrl(menuController.getWindows().get(index - 1).getUrl());
                } else
                {
                    showHomePage(true);
                }
            } else
            {
                // webView.loadUrl(DEFAULT_URL);
                showHomePage(true);
                windowPosition = 0;
            }

        }

        @Override
        public void addWindow()
        {
            SkyLogger.d(TAG, "addWindow,DEFAULT_URL:" + DEFAULT_URL);
            // webView.loadUrl(DEFAULT_URL);
            showHomePage(true);
            windowPosition++;
        }

        @Override
        public ArrayList<WindowData> getWindows()
        {
            SkyLogger.d(TAG, "getwindows");
            return menuController.getWindows();
        }

        @Override
        public void jumpToWindow(String url, int location)
        {
            SkyLogger.d(TAG, "jumpToWindow location：" + location + ",url:" + url);
            if (url == null || "".equals(url))
            {
                showHomePage(true);
            } else
            {
                showHomePage(false);
                loadUrl(url);
            }
            windowPosition = location;

        }

        @Override
        public void showMenuIcon()
        {
            Message msg = mHandler.obtainMessage();
            msg.what = SHOW_MENU_ICON;
            mHandler.sendMessage(msg);

        }

        @Override
        public int getWindowPosition()
        {
            return (windowPosition + 1);
        }

        @Override
        public void deleteHistory(String url)
        {
            SkyLogger.d(TAG, "deleteHistory,url:"+url);
            menuController.deleteHistory(url);
            
        }
    };

    /**
     * Load the url into the webview.
     */
    public void loadUrl(String url)
    {
        SkyLogger.d(TAG, "loadUrl,url:"+url);
        // If keepRunning
        if (webView != null)
        {
            webView.loadUrlIntoView(url, true);
        }

        showHomePage(false);
    }

    private void init(String ua)
    {
        SkyLogger.d(TAG, "init");
        webView = makeWebView();
        webView.setUserAgentString(ua);
        if (!webView.isInitialized())
        {
            webView.init(cordovaImpl, Config.getPluginEntries(), Config.getPreferences(), 0);
        }
        cordovaImpl.onCordovaInit(webView.getPluginManager());
        // Wire the hardware volume controls to control media if desired.
        String volumePref = Config.getPreferences().getString("DefaultVolumeStream", "");
        if ("media".equals(volumePref.toLowerCase(Locale.ENGLISH)))
        {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }

    }

    private View getTopView()
    {
        return webView.getView();
    }

    private CordovaWebView makeWebView()
    {
        SkyLogger.d(TAG, "makeWebView");
        return new CordovaWebViewImpl(makeWebViewEngine());
    }

    protected CordovaWebViewEngine makeWebViewEngine()
    {
        SkyLogger.d(TAG, "makeWebViewEngine");
        return CordovaWebViewImpl.createEngine(this, Config.getPreferences());
    }

    @Override
    public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode)
    {
        SkyLogger.d(TAG, "startActivityForResult");
        cordovaImpl.startActivityForResult(command, intent, requestCode);
    }

    @SuppressLint("NewApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options)
    {
        SkyLogger.d(TAG, "startActivityForResult");
        // Capture requestCode here so that it is captured in the setActivityResultCallback() case.
        cordovaImpl.setActivityResultRequestCode(requestCode);
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void setActivityResultCallback(CordovaPlugin plugin)
    {
        SkyLogger.d(TAG, "setActivityResultCallback");
        cordovaImpl.setActivityResultCallback(plugin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        SkyLogger.d(TAG, "Incoming Result. Request code = " + requestCode);
        super.onActivityResult(requestCode, resultCode, intent);
        cordovaImpl.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public Activity getActivity()
    {
        SkyLogger.d(TAG, "getActivity");
        return cordovaImpl.getActivity();
    }

    @Override
    public Object onMessage(String id, Object data)
    {
        SkyLogger.d(TAG, "onMessage");
        return cordovaImpl.onMessage(id, data);
    }

    @Override
    public ExecutorService getThreadPool()
    {
        SkyLogger.d(TAG, "getThreadPool");
        return cordovaImpl.getThreadPool();
    }

    @Override
    public void requestPermission(CordovaPlugin plugin, int requestCode, String permission)
    {
        SkyLogger.d(TAG, "requestPermission");
        cordovaImpl.requestPermission(plugin, requestCode, permission);
    }

    @Override
    public void requestPermissions(CordovaPlugin plugin, int requestCode, String[] permissions)
    {
        SkyLogger.d(TAG, "requestPermissions");
        cordovaImpl.requestPermissions(plugin, requestCode, permissions);
    }

    @Override
    public boolean hasPermission(String permission)
    {
        SkyLogger.d(TAG, "hasPermission");
        return cordovaImpl.hasPermission(permission);
    }

    @Override
    public void setCoocaaOSConnecter(CoocaaOSConnecter connecter) {

    }

    @Override
    public CoocaaOSConnecter getCoocaaOSConnecter() {
        return null;
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        SkyLogger.d(TAG, "Paused the activity.");

        if (this.webView != null)
        {
            // CB-9382 If there is an activity that started for result and main activity is waiting
            // for callback
            // result, we shoudn't stop WebView Javascript timers, as activity for result might be
            // using them
            boolean keepRunning = this.keepRunning
                    || this.cordovaImpl.activityResultCallback != null;
            this.webView.handlePause(keepRunning);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        SkyLogger.d(TAG, "onWindowFocusChanged--hasFocus--" + hasFocus);

    }

    protected String getIntentInfo(Intent intent)
    {
        String res = null;
        if (intent != null)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                String soundStr = bundle.getString("keyword");
                String secondUrl = bundle.getString("url");
                SkyLogger.d(TAG, "getIntentInfo----keyword:" + soundStr + ",url ");
                if (soundStr != null && !soundStr.equals(""))
                {
                    isNoMenu = true;
                    res = formatCommand(soundStr);
                    return res;
                }
                if (secondUrl != null)
                {
                    return secondUrl;
                }
            }
        }
        return res;
    }

    public boolean isFromBundle(Intent intent)
    {
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            return true;
        }
        return false;
    }

    public boolean isFromData(Intent intent)
    {
        Uri uri = intent.getData();
        if (uri != null)
        {
            return true;
        }
        return false;
    }

    protected void handleIntent(Intent intent)
    {
        SkyLogger.d(TAG, "handleIntent---------is from bundle  or  data----" + intent.getAction()
                + "  " + isFromBundle(intent) + "  " + isFromData(intent));
        if (isFromBundle(intent))
        {
            String info = getIntentInfo(intent);
            if (info != null)
            {
                SkyLogger.d(TAG, "handleIntent----" + info);
                if (info.startsWith("about:blank"))
                {
                    info = "";
                } else
                {
                    if (webView == null)
                    { // 操作太快，浏览器没有被释放，导致云平台启动浏览器会走这里
                      // showPage(false, info);
                      // getToast("打开网页失败！").show();
                      // showToast("打开网页失败！");
                    } else
                    {
                        loadUrl(info);
                    }
                }
            }
        }
        if (isFromData(intent))
        {
            SkyLogger.d(TAG, "isFromData--error---error--");
            // Uri uri = intent.getData();
            // if (uri != null)
            // {
            // String intent_url = uri.toString();
            // Log.d(tag, "handleIntent--22--" + intent_url);
            // if (intent_url.startsWith("about:blank"))
            // {
            // intent_url = "";
            // showHomePage(true);
            // }
            // else{
            // wv.loadUrl(intent_url);
            // showHomePage(false);
            // }
            // String log1= CollecteData(DataFlag.ACTIVITYPATH_LINK,
            // intent_url);
            // String log2= CollecteData(DataFlag.USERACTIVITY, intent_url);
            // submitLog(new String[]{log1,log2});
            // }
        }
    }

    /**
     * Called when the activity receives a new intent
     */
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        SkyLogger.d(TAG, "onNewIntent-");
        // Forward to plugins
        if (this.webView != null)
            this.webView.onNewIntent(intent);

     // 获取第三方调用浏览器的启动方式
        boolean showMenu = true;
        String showMenuStr = this.getIntent().getStringExtra("showMenu");
        if (showMenuStr != null && "false".equals(showMenuStr))
        {
            showMenu = false;
        }else {
            showMenu = this.getIntent().getBooleanExtra("showMenu", true);
        }
        if (!showMenu)
        {
            String url = this.getIntent().getStringExtra("url");
            isNoMenu = true;
            if (url == null || url.equals(""))
                url = "http://www.baidu.com";
            if (webView == null)
            { // 操作太快，浏览器没有被释放，导致云平台启动浏览器会走这里
              // showToast("打开网页失败！");
            } else
            {
                loadUrl(url);
            }
        }
        handleIntent(intent);

    }

    /**
     * Called when the activity will start interacting with the user.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        SkyLogger.d(TAG, "Resumed the activity.");

        if (menuIcon != null && !isNoMenu)
        {
            menuIcon.setMenuIconClickLisenter(menuIconClickLisenter);
            menuIcon.setVisibility(View.VISIBLE);
        } else
        {
            menuIcon.setVisibility(View.GONE);
        }
        if (this.webView == null)
        {
            return;
        } else
        {
            SkyLogger.d(TAG, "Resumed the isNetWorkActive:" + isNetWorkActive());
            if (isNetWorkActive())
            {
                
                if (isHomepageVisible())
                {
                    hideErrorPage();
                    showHomePage(true);
                } else
                {
                    if (isErrorpageVisible())
                    {
                        hideErrorPage();
                        webView.reload();
                    }
                    
                }

            } else
            {
                showLordErrorPage(true);
            }
        }
        // Force window to have focus, so application always
        // receive user input. Workaround for some devices (Samsung Galaxy Note 3 at least)
        // this.getWindow().getDecorView().requestFocus();

        this.webView.handleResume(this.keepRunning);
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        SkyLogger.d(TAG, "Stopped the activity.");

        if (mWakeLock != null)
        {
            mWakeLock.release();
            mWakeLock = null;
        }

        if (this.webView == null)
        {
            return;
        }
        this.webView.handleStop();
    }

    @Override
    public void onTrimMemory(int level)
    {
        SkyLogger.d(TAG, "onTrimMemory the activity.");
        super.onTrimMemory(level);
    }

    private PowerManager.WakeLock mWakeLock = null;

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        SkyLogger.d(TAG, "Started the activity.");

        if (null == mWakeLock)
        {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, TAG); // TAG为 Your class name
            if (null != mWakeLock)
            {
                mWakeLock.acquire();
            }
        }

        if (this.webView == null)
        {
            return;
        }
        this.webView.handleStart();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        SkyLogger
                .d(TAG,
                        "Activity.onLowMemory(),LOW MEMORY#############LOW MEMORY###############LOW MEMORY##########");
        if (this.webView != null)
        {
            // webView.;
        }
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    @Override
    public void onDestroy()
    {
        SkyLogger.d(TAG, "Activity.onDestroy()");
        super.onDestroy();

        if (menuToolbarLayout != null)
        {
            menuToolbarLayout = null;
        }
        if (this.webView != null)
        {
            webView.handleDestroy();
        }
        System.exit(0);
    }

    // /**
    // * Android 6.0 SDK Called by the system when the user grants permissions
    // *
    // * @param requestCode
    // * @param permissions
    // * @param grantResults
    // */
    // @Override
    // public void onRequestPermissionsResult(int requestCode, String permissions[],
    // int[] grantResults) {
    // try
    // {
    // cordovaImpl.onRequestPermissionResult(requestCode, permissions, grantResults);
    // }
    // catch (JSONException e)
    // {
    // SkyLogger.d(TAG, "JSONException: Parameters fed into the method are not valid");
    // e.printStackTrace();
    // }
    //
    // }

    private static class MyHandler extends Handler
    {
        private WeakReference<BrowserActivity> mActivityReference = null;

        public MyHandler(BrowserActivity activity)
        {
            mActivityReference = new WeakReference<BrowserActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            if (mActivityReference != null && mActivityReference.get() != null)
            {
                SkyLogger.d(TAG, "msg,msg.what:" + msg.what);
                switch (msg.what)
                {
                    case SAVE_BOOK_MARK: // 保存书签
                        mActivityReference.get().menuController.saveBookMark(mActivityReference
                                .get().webView);
                        break;
                    case SHOW_TOAST: // show 提示
                        SkyToastView skyToastView = new SkyToastView(mActivityReference.get());
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                        skyToastView.setTostString(msg.obj.toString());
                        skyToastView.showToast(ShowTime.SHOTTIME, params);
                        mActivityReference.get().collectBookmarks = mActivityReference.get().menuController
                                .getBookMarks();
                        mActivityReference.get().isTheWebCollected = mActivityReference.get()
                                .isWebCollected(mActivityReference.get().webView.getUrl());
                        break;
                    case SHOW_CAPTURE_CHANAGE: // 截屏完成
                    {
                        CordovaWebView wv = mActivityReference.get().webView;
                        Bitmap bmBitmap = (Bitmap) msg.obj;
                        if (bmBitmap == null)
                        {
                            bmBitmap = mActivityReference.get().captureScreen();
                        }
                        mActivityReference.get().menuController.addWindow(wv.getTitle(), bmBitmap,
                                wv.getUrl(), msg.arg1);
                    }

                        break;
                    case UPDATE_PROGRESS:
                        int processValue = msg.arg1;
                        if (processValue <= 10)
                        {
                            processValue = 10;
                        }
                        mActivityReference.get().progressView.updatePercent(processValue);
                        if (processValue > 99)
                        {
                            mActivityReference.get().isCanRefrush = true;
                            mActivityReference.get().updateMenuToolBarStatus();
                        }
                        // 进度条测试代码
                        // if (processValue > 120)
                        // {
                        // processValue = 0;
                        // }
                        // sendMessageDelayed(obtainMessage(UPDATE_PROGRESS, ++processValue, 0),
                        // 1000);
                        break;
                    case ADD_HISTORY:
                        mActivityReference.get().menuController
                                .addHistory(mActivityReference.get().webView);
                        // 更新收藏列表
                        mActivityReference.get().collectBookmarks = mActivityReference.get().menuController
                                .getBookMarks();
                        mActivityReference.get().isTheWebCollected = mActivityReference.get()
                                .isWebCollected(mActivityReference.get().webView.getUrl());
                        break;
                    case UPDATE_COLLECT_STATUS:
                        // 更新收藏列表
                        mActivityReference.get().collectBookmarks = mActivityReference.get().menuController
                                .getBookMarks();
                        mActivityReference.get().isTheWebCollected = mActivityReference.get()
                                .isWebCollected(mActivityReference.get().webView.getUrl());
                        break;
                    case SHOW_MENU_ICON:
                        mActivityReference.get().showMenuToolBar(false);
                        break;
                    case UPDATE_ERROR_PAGE:
                        mActivityReference.get().hideErrorPage();
                    default:
                        break;
                }
            }

            super.handleMessage(msg);
        }
    }

    private LordErrorListener mLordErrorListener = new LordErrorListener()
    {

        @Override
        public void onclick(boolean isNetError)
        {
            if (isNetError)
            {
                Intent mIntent = new Intent(
                        android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                // hideErrorPage();
            } else
            {
                webView.reload();
                Message msg = mHandler.obtainMessage();
                msg.what = UPDATE_ERROR_PAGE;
                mHandler.sendMessage(msg);
            }
        }
    };

    private void showLordErrorPage(boolean isNetError)
    {
        SkyLogger.d(TAG, "showLordErrorPage----");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        if (lordErrorPage == null)
        {
            lordErrorPage = new LordErrorPage(mContext);
            lordErrorPage.setLordErrorListener(mLordErrorListener);
            browserMainLayout.addView(lordErrorPage, lp);

        }
        lordErrorPage.updateView(isNetError);

        lordErrorPage.bringToFront();
        lordErrorPage.setVisibility(View.VISIBLE);

    }

    /****
     * 把系统自身请求失败时的网页隐藏
     */
    protected void hideErrorPage()
    {
        SkyLogger.d(TAG, "hideErrorPage----");
        if (lordErrorPage != null)
        {
            lordErrorPage.setVisibility(View.GONE);
            // browserMainLayout.removeView(lordErrorPage);
        }
    }

    public boolean isNetWorkActive()
    {
        NetworkInfo activeNetWork = null;
        if (connectManager == null)
        {
            connectManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        activeNetWork = connectManager.getActiveNetworkInfo();
        if (activeNetWork == null)
        {
            return false;
        }
        SkyLogger.d(TAG, "isNetWorkActive----" + activeNetWork.isConnected());
        return activeNetWork.isConnected();
    }

    private class CordovaInterfaceListenerImpl implements CordovaInterfaceListener
    {

        @Override
        public void onPageStarted(String url)
        {
            SkyLogger.d(TAG, "CordovaInterfaceListenerImpl, onPageStarted()");
            SkyLogger.d(TAG, "url = " + url);
            if (voiceControllBrowser != null)
            {
                voiceControllBrowser.clearIsJSCodeLoad();
            }
        }

        @Override
        public void onPageLoadingFinished(String url)
        {
            SkyLogger.d(TAG, "CordovaInterfaceListenerImpl, onPageLoadingFinished()");
            SkyLogger.d(TAG, "url = " + url);
            if (voiceControllBrowser != null)
            {
                if (!voiceControllBrowser.getJSCodeLoadState())
                {
                    voiceControllBrowser.insertJSCodeAfter(voiceControllBrowser.JSCodeAddress);
                }
            }
            Message msg = mHandler.obtainMessage();
            msg.what = UPDATE_COLLECT_STATUS;
            msg.obj = url;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReceivedError(int errorCode, String description, String failingUrl)
        {
            SkyLogger.d(TAG, "CordovaInterfaceListenerImpl, onReceivedError()");
            SkyLogger.d(TAG, "errorCode = " + errorCode);
            SkyLogger.d(TAG, "description = " + description);
            SkyLogger.d(TAG, "failingUrl = " + failingUrl);
            if (!isNetWorkActive())
            {

                runOnUiThread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        showLordErrorPage(true);
                    }
                });
            } else
            {
                runOnUiThread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        showLordErrorPage(false);
                    }
                });
            }
        }

        @Override
        public void doUpdateVisitedHistory(String url, boolean isReload)
        {
            SkyLogger.d(TAG, "CordovaInterfaceListenerImpl, doUpdateVisitedHistory()");
            SkyLogger.d(TAG, "url = " + url);
            SkyLogger.d(TAG, "isReload = " + isReload);
        }

        @Override
        public boolean shouldOverrideUrlLoading(String url)
        {
            SkyLogger.d(TAG, "CordovaInterfaceListenerImpl, shouldOverrideUrlLoading()");
            SkyLogger.d(TAG, "url = " + url);
            return false;
        }

        @Override
        public void onReceivedTitle(String title)
        {
            SkyLogger.d(TAG, "CordovaInterfaceListenerImpl, onReceivedTitle()");
            SkyLogger.d(TAG, "title = " + title);

            // 保存
            mHandler.sendEmptyMessage(ADD_HISTORY);
        }

        @Override
        public void onReceivedIcon(Bitmap icon)
        {
            SkyLogger.d(TAG, "CordovaInterfaceListenerImpl, onReceivedIcon()");
        }

        @Override
        public void onProgressChanged(int process)
        {
            SkyLogger.d(TAG, "CordovaInterfaceListenerImpl, onProgressChanged()");
            SkyLogger.d(TAG, "process = " + process);
            mHandler.sendMessage(mHandler.obtainMessage(UPDATE_PROGRESS, process, 0));
        }

        @Override
        public void onReceivedSslError(int errorCode, String failingUrl) {

        }
    }
}
