package com.skyworth.tv_browser.provider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.skyworth.framework.skysdk.util.MD5;
import com.skyworth.tv_browser.data.BookmarkSupport;
import com.skyworth.tv_browser.data.HistorySupport;

public class BrowserDBSupport
{
    private static final String tag = "dbsupport";
    private static BrowserDBSupport instance = null;
    private Context mContext = null;
    private String SKYWEB_PATH;
    private String BOOKMARK_FAVCION;
    private static int MAX_HISTORY = 50;
    private static final int BOOKMARK_THUMB_WIDTH = 60;
    private static final int BOOKMARK_THUMB_HEIGHT = 60;

    public static BrowserDBSupport getInstance(Context c)
    {
        if (instance == null)
        {
            instance = new BrowserDBSupport(c);
        }
        return instance;
    }

    private BrowserDBSupport(Context c)
    {
        mContext = c;
        initString(c);
        if (!(new File(SKYWEB_PATH).exists()))
            new File(SKYWEB_PATH).mkdir();
        File dirFile = new File(BOOKMARK_FAVCION);
        if (!dirFile.exists())
            dirFile.mkdir();
    }

    private void initString(Context c)
    {
        SKYWEB_PATH = c.getFilesDir() + "/browser_cache/";
        BOOKMARK_FAVCION = SKYWEB_PATH + "favcion/";
    }

    public void addHistory(String title, String url, long time)
    {
        // SkyLogger.d(tag, "addHistory,title:"+title+",url:"+url+",time:"+time);
        List<HistorySupport> list = DataSupport.findAll(HistorySupport.class);
        if (list != null && list.size() >= MAX_HISTORY)
        {
            HistorySupport hs = DataSupport.findLast(HistorySupport.class);
            hs.delete();
        }

        HistorySupport history = DataSupport.findLast(HistorySupport.class);
        // SkyLogger.d(tag, "history,title:"+history.toString());
        if (history != null && !"".equals(history.getTitle()) && history.getTitle().equals(title))
        {
            int length = DataSupport.count(HistorySupport.class);
            // SkyLogger.d(tag, "history,update, length:"+length);
            history.setTime(time);
            history.update(length);
        } else
        {
            HistorySupport support = new HistorySupport(title, url, time);
            support.save();
        }

    }

    public void deleteHistory(String url)
    {
        DataSupport.deleteAll(HistorySupport.class, "url = ?", url);
    }

    public void delHistory()
    {
        DataSupport.deleteAll(HistorySupport.class);
    }

    public List<HistorySupport> getHistory()
    {
        List<HistorySupport> list = DataSupport.order("time desc").find(HistorySupport.class);
        return list;
    }

    public boolean addBookmark(String title, String url, long time, Bitmap fav)
    {
        String fav_path = "";
        if (fav != null && !fav.isRecycled())
        {
            fav_path = BOOKMARK_FAVCION + MD5.md5s(title + url);
            File myCaptureFile2 = new File(fav_path);
            boolean flag_b = resizedBitmap(fav, myCaptureFile2);
            Log.d(tag, "addBookmark----flag_b:" + flag_b);
            if (!flag_b)
                return false;
        }
        BookmarkSupport bs = new BookmarkSupport(title, url, time, fav_path);
        bs.save();
        return true;
    }

    public void deleteBookmark(String url)
    {
        DataSupport.deleteAll(BookmarkSupport.class, "url = ?", url);
    }

    public List<BookmarkSupport> getBookmark()
    {
        List<BookmarkSupport> list = DataSupport.order("time desc").find(BookmarkSupport.class);
        // List<BookmarkSupport> list = DataSupport.findAll(
        // BookmarkSupport.class);
        return list;
    }

    private boolean resizedBitmap(Bitmap bm, File f)
    {

        try
        {
            int width = bm.getWidth();
            int height = bm.getHeight();
            int newWidth = BOOKMARK_THUMB_WIDTH;
            int newHeight = BOOKMARK_THUMB_HEIGHT;
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            resizedBitmap.recycle();
            return true;
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
