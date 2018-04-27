//package com.skyworth.tv_browser.data;
//
//import com.skyworth.tv_browser.ui.data.CollectionData;
//
//public class BookmarkData1 {
//	private String title = null;
//	private String url = null;
//	private String id = null;
//	private String data = null;
//	private int isBookmark = 0;
//	private int isHistory = 0;
//	private String thumb = null;
//	private String favcion = null;
//
//	// public Bookmark(String id, String title, String url, String thumb)
//	// {
//	// this.id = id;
//	// this.title = title;
//	// this.url = url;
//	// this.thumb = thumb;
//	// }
//
//	public BookmarkData(String id, String title, String url, String data,
//			int isBookmark, int isHistory, String thumb, String favcion) {
//		this.id = id;
//		this.title = title;
//		this.url = url;
//		this.data = data;
//		this.isBookmark = isBookmark;
//		this.isHistory = isHistory;
//		this.thumb = thumb;
//		this.favcion = favcion;
//	}
//
//	public BookmarkData(CollectionData collectionData)
//	{
//		id = collectionData.id;
//		title = collectionData.collectionTitle;
//		url = collectionData.url;
//		favcion = collectionData.iconPath;
//		isHistory = 0;
//	}
//	
//	
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getUrl() {
//		return url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getData() {
//		return data;
//	}
//
//	public void setData(String data) {
//		this.data = data;
//	}
//
//	public int getIsBookmark() {
//		return isBookmark;
//	}
//
//	public void setIsBookmark(int isBookmark) {
//		this.isBookmark = isBookmark;
//	}
//
//	public int getIsHistory() {
//		return isHistory;
//	}
//
//	public void setIsHistory(int isHistory) {
//		this.isHistory = isHistory;
//	}
//
//	public String getThumb() {
//		return thumb;
//	}
//
//	public void setThumb(String thumb) {
//		this.thumb = thumb;
//	}
//
//	public String getFavcion() {
//		return favcion;
//	}
//
//	public void setFavcion(String favcion) {
//		this.favcion = favcion;
//	}
//
//	@Override
//	public String toString() {
//		return "Bookmark [id=" + id + ", title=" + title + ", url=" + url
//				+ ", data=" + data + ", isBookmark=" + isBookmark
//				+ ", isHistory=" + isHistory + ", thumb=" + thumb
//				+ ", favcion=" + favcion + "]";
//	}
//}
