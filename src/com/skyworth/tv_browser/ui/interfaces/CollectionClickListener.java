/*
 * Copyright (C) 2016 The SkyTvOS Project
 *
 * Version     Date              Author
 * ─────────────────────────────────────
 *   V1.0     16-4-26 下午5:55     mikan
 *
 */

package com.skyworth.tv_browser.ui.interfaces;

import com.skyworth.tv_browser.ui.data.CollectionData;

import java.util.List;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author mikan
 * @version V1.0.0
 * @className CollectionClickListener
 * @date 16/4/26
 */
public interface CollectionClickListener
{
	/**
	 * 概述：获取收藏列表<br/>
	 *
	 * @return 收藏列表
	 * date 16/4/26
	 */
	public List<CollectionData> getCollectList();

	/**
	 * 概述：移除某个收藏网页<br/>
	 *
	 * @return 移除是否成功
	 * date 16/4/26
	 */
	public boolean removeCollect(CollectionData data);

	/**
	 * 概述：跳转到某个界面<br/>
	 * 适用条件：<br/>
	 * 执行流程：<br/>
	 * 使用方法：<br/>
	 * 注意事项：<br/>
	 *
	 * date 16/5/5
	 */
	public void jumpToWebsite(String url);
}
