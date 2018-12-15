/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.sym.Name;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 栏目类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "news_columns")
public class Column implements Serializable {

	private static final long serialVersionUID = 2729188894289157323L;

	private Integer _columnId;

	/**
	 * 栏目ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getColumnId() {
		return _columnId;
	}

	public void setColumnId(Integer columnId) {
		_columnId = columnId;
	}

	/**
	 * 栏目URL路径
	 */
	@JsonIgnore
	@Transient
	public String getColumnUrlPath() {
		return getNewsSource().getNewsSourceUrlPath() + "columns/";
	}

	/**
	 * 栏目文件路径
	 */
	@JsonIgnore
	@Transient
	public String getColumnFilePath() {
		return getNewsSource().getNewsSourceFilePath() + "columns" + File.separator;
	}
	
	/**
	 * 静态化栏目Url
	 */
	@JsonIgnore
	@Transient
	public String staticColumnUrl() {
		if (_parentColumn == null) {
			return "";				
		} else {
			return "column/" + _columnId +"/index.html";			
		}
	}

	private String _name;

	/**
	 * 名称
	 */
	@javax.persistence.Column(nullable = false, length = 100)
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	/**
	 * 带级别名称
	 */
	@JsonIgnore
	@Transient
	public String getNameWithLevel() {
		String nameWithLevel = _name;
		if (_code != null) {
			int level = _code.split(":").length;
			for (int i = 0; i < level - 1; i++) {
				nameWithLevel = " " + nameWithLevel;
			}
		}
		return nameWithLevel;
	}

	/**
	 * 带父栏目名称
	 */
	@JsonIgnore
	@Transient
	public String getNameWithParents() {
		String nameWithParents = _name;
		Column parentColumn = _parentColumn;
		while (parentColumn != null) {
			nameWithParents = parentColumn.getName() + ":" + nameWithParents;
			parentColumn = parentColumn.getParentColumn();
		}
		return nameWithParents;
	}

	private String _intro;

	/**
	 * 简介
	 */
	@javax.persistence.Column(nullable = true, length = 200)
	public String getIntro() {
		return _intro;
	}

	public void setIntro(String intro) {
		_intro = intro;
	}

	private Integer _iconRatio;

	/**
	 * 图标比例
	 */
	@javax.persistence.Column(nullable = false)
	public Integer getIconRatio() {
		return _iconRatio;
	}

	public void setIconRatio(Integer iconRatio) {
		_iconRatio = iconRatio;
	}

	/**
	 * 图标比例枚举
	 */
	public static enum IconRatioEnum {
		方图1_1,
		横图3_2,
		竖图2_3,
		横图4_3,
		竖图3_4,
		横图16_9,
		竖图9_16
	}

	/**
	 * 图标比例枚举数组
	 */
	@JsonIgnore
	@Transient
	public IconRatioEnum[] getIconRatioEnums() {
		return IconRatioEnum.values();
	}

	/**
	 * 图标比例的枚举
	 */
	@JsonIgnore
	@Transient
	public IconRatioEnum getIconRatioEnum() {
		return IconRatioEnum.values()[_iconRatio];
	}

	/**
	 * 图标URL路径
	 */
	public String iconUrlPath() {
		return getColumnUrlPath() + _columnId + "_icon.png";
	}

	/**
	 * 图标文件路径
	 */
	public String iconFilePath() {
		return getColumnFilePath() + _columnId + "_icon.png";
	}

	private Date _iconUpdateTime;

	/**
	 * 图标更新时间
	 */
	@javax.persistence.Column(nullable = true)
	public Date getIconUpdateTime() {
		return _iconUpdateTime;
	}

	public void setIconUpdateTime(Date iconUpdateTime) {
		_iconUpdateTime = iconUpdateTime;
	}

	private Integer _templateType;

	/**
	 * 模版类别
	 */
	@javax.persistence.Column(nullable = false)
	public Integer getTemplateType() {
		return _templateType;
	}

	public void setTemplateType(Integer templateType) {
		_templateType = templateType;
	}

	/**
	 * 模版类别枚举
	 */
	public static enum TemplateTypeEnum {
		子栏目列表,
		顶部栏目选择_子栏目列表,
		顶部栏目选择_顶部轮播推荐_子栏目列表,
		文章列表,
		顶部栏目选择_文章列表,
		顶部栏目选择_顶部轮播推荐_文章列表,
		顶部栏目选择_顶部轮播推荐_子栏目_文章列表,
		杂志_文章列表,
		书城_文章列表
	}

	/**
	 * 模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public TemplateTypeEnum[] getTemplateTypeEnums() {
		return TemplateTypeEnum.values();
	}

	/**
	 * 模版类别的枚举
	 */
	@JsonIgnore
	@Transient
	public TemplateTypeEnum getTemplateTypeEnum() {
		return TemplateTypeEnum.values()[_templateType];
	}

	private Integer _thumbnailRatio;

	/**
	 * 缩略图默认比例
	 */
	@JsonIgnore	
	@javax.persistence.Column(nullable = false)
	public Integer getThumbnailRatio() {
		return _thumbnailRatio;
	}

	public void setThumbnailRatio(Integer thumbnailRatio) {
		_thumbnailRatio = thumbnailRatio;
	}

	/**
	 * 缩略图默认比例枚举
	 */
	public static enum ThumbnailRatioEnum {
		方图1_1,
		横图3_2,
		竖图2_3,
		横图4_3,
		竖图3_4,
		横图16_9,
		竖图9_16
	}

	/**
	 * 缩略图默认比例枚举数组
	 */
	@JsonIgnore
	@Transient
	public ThumbnailRatioEnum[] getThumbnailRatioEnums() {
		return ThumbnailRatioEnum.values();
	}

	/**
	 * 缩略图默认比例的枚举
	 */
	@JsonIgnore
	@Transient
	public ThumbnailRatioEnum getThumbnailRatioEnum() {
		return ThumbnailRatioEnum.values()[_thumbnailRatio];
	}

	private Integer _businessType;

	/**
	 * 业务类别
	 */
	@javax.persistence.Column(nullable = false)
	public Integer getBusinessType() {
		return _businessType;
	}

	public void setBusinessType(Integer businessType) {
		_businessType = businessType;
	}

	/**
	 * 业务类别枚举
	 */
	public static enum BusinessTypeEnum {
		无,
		杂志,
		书城
	}

	/**
	 * 业务类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public BusinessTypeEnum[] getBusinessTypeEnums() {
		return BusinessTypeEnum.values();
	}

	/**
	 * 业务类别的枚举
	 */
	@JsonIgnore
	@Transient
	public BusinessTypeEnum getBusinessTypeEnum() {
		return BusinessTypeEnum.values()[_businessType];
	}

	private String _businessIdentifiers;

	/**
	 * 业务标识符
	 */
	@javax.persistence.Column(nullable = true, length = 1000)
	public String getBusinessIdentifiers() {
		return _businessIdentifiers;
	}

	public void setBusinessIdentifiers(String businessIdentifiers) {
		_businessIdentifiers = businessIdentifiers;
	}

	/**
	 * 带格式的业务标识符
	 */
	@SuppressWarnings("unchecked")
	@JsonIgnore
	@Transient
	public String getBusinessIdentifiersWithFormat() {
		if (_businessIdentifiers != null) {
			try {
				String businessIdentifiersWithFormat = "";
				ObjectMapper objectMapper = new ObjectMapper();
				HashMap<String, String> businessIdentifierMap = objectMapper.readValue(_businessIdentifiers, HashMap.class);
				for (Map.Entry<String, String> entry : businessIdentifierMap.entrySet()) {
					businessIdentifiersWithFormat += entry.getKey() + ": " + entry.getValue() + ", ";
				}
				return businessIdentifiersWithFormat;
			} catch (IOException e) {}
		}
		return null;
	}

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = false)
	public Boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(Boolean enabled) {
		_enabled = enabled;
	}

	/**
	 * 是否可用枚举
	 */
	public static enum EnabledEnum {
		否,
		是
	}

	/**
	 * 是否可用的枚举
	 */
	@JsonIgnore
	@Transient
	public EnabledEnum getEnabledEnum() {
		int index = _enabled ? 1 : 0;
		return EnabledEnum.values()[index];
	}

	private Integer _position;

	/**
	 * 排序位置
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = false)
	public Integer getPosition() {
		return _position;
	}

	public void setPosition(Integer position) {
		_position = position;
	}

	private String _code;

	/**
	 * 排序代码
	 */
	@javax.persistence.Column(nullable = false, length = 100)
	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		_code = code;
	}

	private Column _parentColumn;

	/**
	 * 所属的父栏目
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "parentColumnId", nullable = true)
	public Column getParentColumn() {
		return _parentColumn;
	}

	public void setParentColumn(Column parentColumn) {
		_parentColumn = parentColumn;
	}

	private Integer _parentColumnId;

	/**
	 * 所属的父栏目ID
	 */	
	@Transient
	public Integer getParentColumnId() {
		return _parentColumnId;
	}

	public void setParentColumnId(Integer parentColumnId) {
		_parentColumnId = parentColumnId;
	}

	private List<Column> _childColumns = new ArrayList<Column>();

	/**
	 * 包含的子栏目
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentColumn")
	@OrderBy("position")
	public List<Column> getChildColumns() {
		return _childColumns;
	}

	public void setChildColumns(List<Column> childColumns) {
		_childColumns = childColumns;
	}
	
	private List<ColumnSubstitute> _sourceColumnSubstitutes = new ArrayList<ColumnSubstitute>();
	
	/**
	 * 包含的源栏目替身
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceColumn")
	public List<ColumnSubstitute> getSourceColumnSubstitutes() {
		return _sourceColumnSubstitutes;
	}

	public void setSourceColumnSubstitutes(List<ColumnSubstitute> sourceColumnSubstitutes) {
		_sourceColumnSubstitutes = sourceColumnSubstitutes;
	}

	private List<ColumnSubstitute> _targetColumnSubstitutes = new ArrayList<ColumnSubstitute>();
	
	/**
	 * 包含的目标栏目替身
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "targetColumn")
	public List<ColumnSubstitute> getTargetColumnSubstitutes() {
		return _targetColumnSubstitutes;
	}

	public void setTargetColumnSubstitutes(List<ColumnSubstitute> targetColumnSubstitutes) {
		_targetColumnSubstitutes = targetColumnSubstitutes;
	}
	
	private List<ColumnReplacement> _sourceColumnReplacements = new ArrayList<ColumnReplacement>();
	
	/**
	 * 包含的源栏目替代
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceColumn")
	public List<ColumnReplacement> getSourceColumnReplacements() {
		return _sourceColumnReplacements;
	}
	
	public void setSourceColumnReplacements(List<ColumnReplacement> sourceColumnReplacements) {
		_sourceColumnReplacements = sourceColumnReplacements;
	}
	
	private List<ColumnReplacement> _targetColumnReplacements = new ArrayList<ColumnReplacement>();
	
	/**
	 * 包含的目标栏目替代
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "targetColumn")
	public List<ColumnReplacement> getTargetColumnReplacements() {
		return _targetColumnReplacements;
	}
	
	public void setTargetColumnReplacements(List<ColumnReplacement> targetColumnReplacements) {
		_targetColumnReplacements = targetColumnReplacements;
	}

	private NewsSource _newsSource;

	/**
	 * 所属的新闻源
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "newsSourceId", nullable = false)
	public NewsSource getNewsSource() {
		return _newsSource;
	}

	public void setNewsSource(NewsSource newsSource) {
		_newsSource = newsSource;
	}

	private List<Distribution> _distributions = new ArrayList<Distribution>();
	
	/**
	 * 包含的分发
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "column")
	public List<Distribution> getDistributions() {
		return _distributions;
	}

	public void setDistributions(List<Distribution> distributions) {
		_distributions = distributions;
	}

	private List<CategoryTag> _categoryTags = new ArrayList<CategoryTag>();

	/**
	 * 关联的分类标签
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tag_columnCategoryTags", joinColumns = @JoinColumn(name = "columnId", nullable = false), inverseJoinColumns = @JoinColumn(name = "categoryTagId", nullable = false))
	@OrderColumn(name = "position")
	public List<CategoryTag> getCategoryTags() {
		return _categoryTags;
	}
	
	public void setCategoryTags(List<CategoryTag> categoryTags) {
		_categoryTags = categoryTags;
	}	

	private List<Integer> _categoryTagIds = new ArrayList<Integer>();

	/**
	 * 关联的分类标签ID
	 */
	@Transient
	public List<Integer> getCategoryTagIds() {
		return _categoryTagIds;
	}
	
	public void setCategoryTagIds(List<Integer> categoryTagIds) {
		_categoryTagIds = categoryTagIds;
	}	
}
