/**
 * 
 */
package com.cccollector.news.templatemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模板文章类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class TemplateArticle implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2287124778928372621L;

	private int _distributionId;

	/**
	 * 分发ID
	 */
	public int getDistributionId() {
		return _distributionId;
	}

	public void setDistributionId(int distributionId) {
		_distributionId = distributionId;
	}

	private String _title;

	/**
	 * 标题
	 */
	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	private String _source;

	/**
	 * 来源
	 */
	public String getSource() {
		return _source;
	}

	public void setSource(String source) {
		_source = source;
	}

	private String _content;

	/**
	 * 内容
	 */
	public String getContent() {
		return _content;
	}

	public void setContent(String content) {
		_content = content;
	}

	private Date _updateTimeDate;

	/**
	 * Date类型更新时间
	 */
	public Date getUpdateTimeDate() {
		return _updateTimeDate;
	}

	public void setUpdateTimeDate(Date updateTimeDate) {
		_updateTimeDate = updateTimeDate;
	}

	private int _listThumbnailRatio;

	/**
	 * 列表缩略图比例
	 */
	public int getListThumbnailRatio() {
		return _listThumbnailRatio;
	}

	public void setListThumbnailRatio(int listThumbnailRatio) {
		_listThumbnailRatio = listThumbnailRatio;
	}

	private List<TemplateCategoryTag> _categoryTags = new ArrayList<TemplateCategoryTag>();

	/**
	 * 关联的分类标签
	 */
	public List<TemplateCategoryTag> getTemplateCategoryTags() {
		return _categoryTags;
	}

	public void setTemplateCategoryTags(List<TemplateCategoryTag> categoryTags) {
		_categoryTags = categoryTags;
	}

	private List<TemplateArticle> _relativeArticles;

	/**
	 * 相关阅读
	 */
	public List<TemplateArticle> getRelativeArticles() {
		return _relativeArticles;
	}

	public void setRelativeArticles(List<TemplateArticle> relativeArticles) {
		this._relativeArticles = relativeArticles;
	}

	private String _pictureUrl;

	/**
	 * 图片
	 */
	public String getPictureUrl() {
		return _pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		_pictureUrl = pictureUrl;
	}

	private String _articleUrl;

	/**
	 * 文章正文
	 */
	public String getArticleUrl() {
		return _articleUrl;
	}

	public void setArticleUrl(String articleUrl) {
		this._articleUrl = articleUrl;
	}

	private boolean _isTop = false;

	/**
	 * 是否置顶
	 */
	public boolean getIsTop() {
		return _isTop;
	}

	public void setIsTop(boolean isTop) {
		this._isTop = isTop;
	}
	

	private String _shareSource;

	/**
	 * 分享来源
	 */
	public String getShareSource() {
		return _shareSource;
	}
	
	public void setShareSource(String shareSource) {
		_shareSource = shareSource;
	}
	
	private String _appkey;	

	/**
	 * 应用键
	 */
	public String getAppkey() {
		return _appkey;
	}
	
	public void setAppkey(String appkey) {
		_appkey = appkey;
	}
	
	private String _code;

	/**
	 * 编码格式
	 */
	public String getCode() {
		return _code;
	}
	
	public void setCode(String code) {
		_code = code;
	}
}
