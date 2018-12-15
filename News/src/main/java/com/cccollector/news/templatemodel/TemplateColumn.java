/**
 * 
 */
package com.cccollector.news.templatemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * 栏目类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
//表示开启二级缓存，并使用read-only策略
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class TemplateColumn implements Serializable {

	private static final long serialVersionUID = 3066808415706436174L;
	
	private Integer _columnId;

	/**
	 * 栏目ID
	 */
	public Integer getColumnId() {
		return _columnId;
	}

	public void setColumnId(Integer columnId) {
		_columnId = columnId;
	}

	
	private String _name;
	
	/**
	 * 名称
	 */
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}

	private String _intro;

	/**
	 * 简介
	 */
	public String getIntro() {
		return _intro;
	}

	public void setIntro(String intro) {
		_intro = intro;
	}
	
	private String _iconUrl;
	
	/**
	 * 图标
	 */
	public String getIconUrl() {
		return _iconUrl;
	}
	
	public void setIconUrl(String iconUrl) {
		_iconUrl = iconUrl;
	}

	private String _link;	

	/**
	 * 链接
	 */	
	public String getLink() {
		return _link;
	}

	public void setLink(String link) {
		_link = link;
	}

	private boolean _style1;
	
	/**
	 * 样式一2级栏目
	 */	
	public boolean getStyle1() {
		return _style1;
	}
	
	public void setStyle1(boolean style1) {
		_style1 = style1;
	}
	
	private boolean _style2;
	
	/**
	 * 样式二3级栏目
	 */	
	public boolean getStyle2() {
		return _style2;
	}

	public void setStyle2(boolean style2) {
		_style2 = style2;
	}

	private TemplateColumn _parentColumn;

	/**
	 * 当前栏目所属的一级栏目
	 */	
	public TemplateColumn getParentColumn() {
		return _parentColumn;
	}

	public void setParentColumn(TemplateColumn parentColumn) {
		_parentColumn = parentColumn;
	}

	private List<TemplateColumn> _childColumns = new ArrayList<TemplateColumn>();

	/**
	 * 包含的子栏目
	 */
	//表示开启二级缓存，并使用read-only策略
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public List<TemplateColumn> getChildColumns() {
		return _childColumns;
	}

	public void setChildColumns(List<TemplateColumn> childColumns) {
		_childColumns = childColumns;
	}
	
	private List<TemplateRecommend> _templateRecommends = new ArrayList<TemplateRecommend>();
	
	/**
	 * 关联的模板推荐列表
	 */
	public List<TemplateRecommend> getTemplateRecommends() {
		return _templateRecommends;
	}
	
	public void setTemplateRecommends(List<TemplateRecommend> templateRecommends) {
		_templateRecommends = templateRecommends;
	}
}
