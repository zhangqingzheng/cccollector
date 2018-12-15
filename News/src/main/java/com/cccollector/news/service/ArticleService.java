/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.Article;
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.service.GenericService;

/**
 * 文章服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface ArticleService extends GenericService<Integer, Article> {

	/**
	 * 获取发布目录
	 */
	public String getPublishPath();
	
	/**
	 * 获取文章目录
	 */
	public String getArticlesPath(NewsSource newsSource);

	/**
	 * 获取文章发布目录
	 */
	public String getPublishArticlesPath(NewsSource newsSource);

	/**
	 * 获取文章JSON发布URL
	 */
	public String getJsonPublishArticlesUrl(NewsSource newsSource);	
	
	/**
	 * 添加文章
	 * 
	 * @param article 待添加的文章
	 */
	public void addArticle(Article article);
	
	/**
	 * 更新文章
	 * 
	 * @param article 待更新的文章
	 */
	public void updateArticle(Article article);
	
	/**
	 * 更新文章多媒体数量
	 * 
	 * @param article 待更新的文章
	 */
	public void updateArticleMediaCount(Article article);
	
	/**
	 * 更新文章JSON文件
	 * 
	 * @param article 待更新的文章
	 */
	public void updateArticleJson(Article article);
	
	/**
	 * 删除文章JSON文件
	 * 
	 * @param article 待删除的文章
	 */
	public void deleteArticleJson(Article article);
}
