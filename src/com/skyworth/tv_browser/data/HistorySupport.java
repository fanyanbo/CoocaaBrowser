package com.skyworth.tv_browser.data;

import org.litepal.crud.DataSupport;

public class HistorySupport extends DataSupport{
    private String title;
    private String url;
    private long time;
    
    public HistorySupport(){}
    
	public HistorySupport(String title, String url, long time) {
		super();
		this.title = title;
		this.url = url;
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

    @Override
    public String toString()
    {
        return "HistorySupport [title=" + title + ", url=" + url + ", time=" + time + "]";
    }
    
    
    
}
