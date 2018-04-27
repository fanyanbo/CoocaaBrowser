package com.skyworth.tv_browser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewEngine.WebViewCaptureCallBack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.skyworth.framework.skysdk.logger.SkyLogger;
import com.skyworth.tv_browser.data.BookmarkSupport;
import com.skyworth.tv_browser.data.HistorySupport;
import com.skyworth.tv_browser.data.WindowData;
import com.skyworth.tv_browser.provider.BrowserDBSupport;
import com.skyworth.tv_browser.provider.SPManager;

//import android.webkit.WebView;

public class MenuController
{
    private static final int HISTORY_SIZE = 50;
    private final String TAG = "MenuController";
    private static final int CAPTURE_WIDTH = 500;
    private static final int CAPTURE_HEIGHT = 280;

    interface BrowserControllerListener
    {
        /**
         * @Description 保存书签完成后回调给ui处理<br/>
         */
        void onSaveBookmarkFinish(boolean success, String errorMsg);

        /**
         * @Description 删除书签完成时候的回调<br/>
         */
        void onDelBookmarkFinish(boolean success, String errorMsg);

        /**
         * @Description 清楚浏览器cache时候的回调<br/>
         */
        void onClearCacheFinish();

        /**
         * @Description 清楚历史数据时候的回调<br/>
         */
        void onClearHistoryFinish();

        /**
         * @Description UA发生变化的时候的回调<br/>
         */
        void onUAChanged(String ua);

        /**
         * @Description 截屏完成时候的回调<br/>
         */
        void onCaptureChanged(int location, Bitmap bm);

        /**
         * @Description 退出浏览器时候的回调<br/>
         */
        void onExitBrowser();
    }

    private static final String tag = "bs";
    private Context mContext = null;
    private BrowserControllerListener mBrowserControllerListener = null;
    private static MenuController mController = null;
    private ArrayList<WindowData> winList = new ArrayList<WindowData>();

    public static MenuController getInstance(Context c)
    {
        if (null == mController)
        {
            mController = new MenuController(c);
        }
        return mController;
    }

    private MenuController(Context c)
    {
        mContext = c;
    }

    public void setBrowserSettingsListener(BrowserControllerListener listener)
    {
        mBrowserControllerListener = listener;
    }

    public void exit()
    {
        // 退出浏览器的实现
        if (mBrowserControllerListener != null)
        {
            mBrowserControllerListener.onExitBrowser();
        }
    }

    ;

    public void back(CordovaWebView wv)
    {
        wv.backHistory();
    }

    ;

    public void forward(CordovaWebView wv)
    {
        wv.goForward();
    }

    ;

    public void refresh(CordovaWebView wv, boolean isLoading)
    {
        if (isLoading)
        {
            wv.stopLoading();
        } else
        {
            wv.reload();
        }
    }

    ;

    public List<BookmarkSupport> getBookMarks()
    {
        // return SkyWebDB.getInstance(mContext).getBookmark();
        return BrowserDBSupport.getInstance(mContext).getBookmark();
    }

    public void saveBookMark(final CordovaWebView wv)
    {
        Log.d(tag, "insertBookmark---------");
        final String title = wv.getTitle();
        final String url = wv.getUrl();
        final long time = System.currentTimeMillis();
        // final Bitmap captureBitmap = wv.getFavicon();
        BrowserApplication.post(new Runnable()
        {

            @Override
            public void run()
            {
                SkyLogger.i(TAG, "run: start url:" + url);
                String favUrl = "";
                Bitmap fav = null;
                if (url != null && !url.equals(""))
                {
                    favUrl = "http://" + Uri.parse(url).getHost() + "/favicon.ico";
                    fav = getBitmapByUrl(favUrl);

                    SkyLogger.i(TAG, "11run: Uri.parse(url).getHost() = "
                            + Uri.parse(url).getHost());
                    SkyLogger.i(TAG, "11run: url = " + url);
                    SkyLogger.i(TAG, "11run: favUrl = " + favUrl);
                    SkyLogger.i(TAG, "11run: fav = " + (fav == null));
                }

                if ((url != null) && url.startsWith("about:blank"))
                {
                    if (mBrowserControllerListener != null)
                    {
                        mBrowserControllerListener.onSaveBookmarkFinish(false, "空网页");
                    }
                } else if (title == null || title.equals("") || url == null || url.equals(""))
                {
                    if (mBrowserControllerListener != null)
                    {
                        mBrowserControllerListener.onSaveBookmarkFinish(false, "空网页");
                    }
                } else
                {
                    Log.d(tag, "insertBookmark---title---" + title + "  " + wv.toString());
                    if (isCollected(url))
                    {
                        if (mBrowserControllerListener != null)
                        {
                            mBrowserControllerListener.onSaveBookmarkFinish(false, "已收藏");
                        }
                        // if (fav != null)
                        // {
                        // SkyWebDB.getInstance(mContext).addBookmark(title, url, time, 1, 0,
                        // fav, null);
                        // }
                    } else
                    {
                        // SkyWebDB.getInstance(mContext).addBookmark(title, url, time, 1, 0,
                        // fav, null);
                        SkyLogger.i(TAG, "run: url = " + url);
                        SkyLogger.i(TAG, "run: favUrl = " + favUrl);
                        SkyLogger.i(TAG, "run: fav = " + (fav == null));
                        BrowserDBSupport.getInstance(mContext).addBookmark(title, url, time, fav);
                        if (mBrowserControllerListener != null)
                        {
                            mBrowserControllerListener.onSaveBookmarkFinish(true, "收藏成功");
                        }
                    }
                }
            }
        });

    }

    ;

    public void removeBookmark(String url)
    {
        // SkyWebDB.getInstance(mContext).deleteBookmark(BookmarkData);
        BrowserDBSupport.getInstance(mContext).deleteBookmark(url);
        if (mBrowserControllerListener != null)
        {
            mBrowserControllerListener.onDelBookmarkFinish(true, "取消收藏成功");
        }
    }

    public List<HistorySupport> getHistory()
    {
        // return SkyWebDB.getInstance(mContext).getPageHistory();
        return BrowserDBSupport.getInstance(mContext).getHistory();
    }

    public void deleteHistory(String url)
    {
        BrowserDBSupport.getInstance(mContext).deleteHistory(url);
    }

    public void addHistory(final CordovaWebView wv)
    {
        String title = wv.getTitle();
        String url = wv.getUrl();
        long time = System.currentTimeMillis();

        SkyLogger.i(TAG, "addHistory: title = " + title);
        SkyLogger.i(TAG, "addHistory: url = " + url);
        // int
        // historyCount=SkyWebDB.getInstance(mContext).getHistoryCount(SkyWebDB.PAGEHISTORY_TABLE);
        // Log.d(tag, "addHistory-----historyCount---"+historyCount);
        // if(historyCount > HISTORY_SIZE){
        // SkyWebDB.getInstance(mContext).deleteLastPageHistory();
        // }
        // SkyWebDB.getInstance(mContext).addPageHistory(title, url,
        // time, fav);
        BrowserDBSupport.getInstance(mContext).addHistory(title, url, time);
    }

    public void clearHistory()
    {
        // SkyWebDB.getInstance(mContext).deletePageHistory();
        BrowserDBSupport.getInstance(mContext).delHistory();
        if (mBrowserControllerListener != null)
        {
            mBrowserControllerListener.onClearHistoryFinish();
        }
    }

    public void clearCache(CordovaWebView wv)
    {
        wv.clearCache(true);
        if (mBrowserControllerListener != null)
        {
            mBrowserControllerListener.onClearCacheFinish();
        }
    }

    ;

    public void setUA(String ua)
    {
        SPManager.getInstance(mContext).saveString("ua", ua);
        if (mBrowserControllerListener != null && !ua.endsWith(""))
        {
            mBrowserControllerListener.onUAChanged(ua);
        }
    }

    public String getUA()
    {
        return SPManager.getInstance(mContext).getStringValue("ua");
    }

    public void getCapture(final int location, CordovaWebView wv)
    {
        SkyLogger.i(TAG, "getCapture: location = " + location);
        try
        {
            wv.getEngine().capturePicture(new WebViewCaptureCallBack()
            {

                @Override
                public void onCaptureBitmap(Bitmap bitmap)
                {
                    SkyLogger.i(TAG, "getCapture: onCaptureBitmap");
                    if (mBrowserControllerListener != null)
                    {
                        mBrowserControllerListener.onCaptureChanged(location, bitmap);
                    }
                }
            }, CAPTURE_WIDTH, CAPTURE_HEIGHT);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        // Bitmap currentScreen = null;
        // View cv = ((Activity) mContext).getWindow().getDecorView();
        // float resolution = UIUtil.getResolutionDiv();
        // Log.d(tag, "captureHandler----resolution---" + resolution);
        // if (resolution == 0.5)
        // {
        // currentScreen = Bitmap.createBitmap(2 * cv.getWidth() / 3, 2 *
        // cv.getHeight() / 3,
        // Config.RGB_565);
        // }
        // else
        // {
        // currentScreen = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(),
        // Config.RGB_565);
        // }
        // Canvas canvas = new Canvas(currentScreen);
        // cv.draw(canvas);
        // if (mBrowserControllerListener != null)
        // {
        // mBrowserControllerListener.onCaptureChanged(location, currentScreen);
        // }
    }

    public void addWindow(String title, Bitmap bm, String url, int position)
    {
        synchronized (winList)
        {
            WindowData data = new WindowData(title, bm, url);
            if (position >= winList.size())
            {
                winList.add(data);
            } else
            {
                winList.remove(position);
                winList.add(position, data);
            }

        }
    }

    public void removeWindow(int location)
    {
        synchronized (winList)
        {
            try
            {
                winList.remove(location);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<WindowData> getWindows()
    {
        return winList;
    }

    private boolean isCollected(String url)
    {
        List<BookmarkSupport> list = BrowserDBSupport.getInstance(mContext).getBookmark();
        if (list != null)
        {
            for (BookmarkSupport BookmarkData : list)
            {
                if (url.equals(BookmarkData.getUrl()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkURL(String url)
    {
        String regEx = "^(((ht|f)tp(s?))\\://)?(www.|[a-zA-Z].)[a-zA-Z0-9\\-\\.]+\\.(com|edu|gov|mil|net|org|biz|info|name|museum|us|ca|uk)(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\;\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(url);
        boolean rs = mat.find();
        Log.d(tag, "is url-------" + rs);
        return rs;
    }

    private Bitmap getBitmapByUrl(String url)
    {
        Bitmap bitmap = null;
        try
        {
            URL picUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) picUrl.openConnection();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }
}
