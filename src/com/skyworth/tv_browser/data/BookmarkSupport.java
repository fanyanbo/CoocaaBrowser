package com.skyworth.tv_browser.data;

import org.litepal.crud.DataSupport;

public class BookmarkSupport extends DataSupport{
    private String title;
    private String url;
    private long time;
    private String fav;
    
    public BookmarkSupport(){}
    
	public BookmarkSupport(String title, String url, long time, String fav) {
		super();
		this.title = title;
		this.url = url;
		this.time = time;
		this.fav = fav;
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

	public String getFav() {
		return fav;
	}

	public void setFav(String fav) {
		this.fav = fav;
	}
    
    
	
}
