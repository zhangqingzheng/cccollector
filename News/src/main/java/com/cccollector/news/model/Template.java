/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 模板类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "site_templates")
public class Template implements Serializable {
	
	private static final long serialVersionUID = 8643490027303768242L;
	
	private Integer _templateId;

	/**
	 * 模板ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTemplateId() {
		return _templateId;
	}

	public void setTemplateId(Integer templateId) {
		_templateId = templateId;
	}
	
	/**
	 * 模板URL路径
	 */
	@JsonIgnore
	@Transient
	public String getTemplateUrlPath() {
		return getSiteVersion().getSiteVersionUrlPath() + "templates/";
	}

	/**
	 * 模板文件路径
	 */
	@JsonIgnore
	@Transient
	public String getTemplateFilePath() {
		return getSiteVersion().getSiteVersionFilePathWithSite() + "templates" + File.separator;
	}
	
	private String _name;
	
	/**
	 * 名称
	 */
	@Column(nullable = false, length = 100)
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	private Integer _type;

	/**
	 * 类别
	 */
	@Column(nullable = false)
	public Integer getType() {
		return _type;
	}

	public void setType(Integer type) {
		_type = type;
	}

	/**
	 * 类别枚举
	 */
	public static enum TypeEnum {
		栏目,
		文章,
		其它
	}

	/**
	 * 类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public TypeEnum[] getTypeEnums() {
		return TypeEnum.values();
	}

	/**
	 * 类别的枚举
	 */
	@JsonIgnore
	@Transient
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
	}
	
	private Integer _subtype;

	/**
	 * 子类别
	 */
	@Column(nullable = false)
	public Integer getSubtype() {
		return _subtype;
	}

	public void setSubtype(Integer subtype) {
		_subtype = subtype;
	}

	/**
	 * 栏目子类别枚举
	 */
	public static enum ColumnSubtypeEnum {
		子栏目列表,
		文章列表,
		子栏目_文章列表
	}

	/**
	 * 栏目子类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ColumnSubtypeEnum[] getColumnSubtypeEnums() {
		return ColumnSubtypeEnum.values();
	}

	/**
	 * 栏目子类别枚举
	 */
	@JsonIgnore
	@Transient
	public ColumnSubtypeEnum getColumnSubtypeEnum() {
		return ColumnSubtypeEnum.values()[_subtype];
	}

	/**
	 * 文章子类别枚举
	 */
	public static enum ArticleSubtypeEnum {
		图文混排
	}

	/**
	 * 文章子类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ArticleSubtypeEnum[] getArticleSubtypeEnums() {
		return ArticleSubtypeEnum.values();
	}

	/**
	 * 文章子类别枚举
	 */
	@JsonIgnore
	@Transient
	public ArticleSubtypeEnum getArticleSubtypeEnum() {
		return ArticleSubtypeEnum.values()[_subtype];
	}
	
	/**
	 * 其它子类别枚举
	 */
	public static enum OtherSubtypeEnum {
		分类标签
	}
	
	/**
	 * 其它子类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public OtherSubtypeEnum[] getOtherSubtypeEnums() {
		return OtherSubtypeEnum.values();
	}
	
	/**
	 * 其它子类别枚举
	 */
	@JsonIgnore
	@Transient
	public OtherSubtypeEnum getOtherSubtypeEnum() {
		return OtherSubtypeEnum.values()[_subtype];
	}

	/**
	 * 获取子类别枚举名称
	 */
	@JsonIgnore
	@Transient
	public String getSubtypeEnumName() {
		return getTypeEnum() == TypeEnum.栏目 ? getColumnSubtypeEnum().name().replace('_', '+') : (getTypeEnum() == TypeEnum.文章 ? getArticleSubtypeEnum().name().replace('_', '+') : getOtherSubtypeEnum().name().replace('_', '+'));
	}
	
	private String _validateExpression;
	
	/**
	 * 校验表达式
	 */
	@Column(nullable = true)
	public String getValidateExpression() {
		return _validateExpression;
	}
	
	public void setValidateExpression(String validateExpression) {
		_validateExpression = validateExpression;
	}
	
	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	@JsonIgnore
	@Column(nullable = false)
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
	@Column(nullable = false)
	public Integer getPosition() {
		return _position;
	}
	
	public void setPosition(Integer position) {
		_position = position;
	}

	private SiteVersion _siteVersion;

	/**
	 * 所属的站点版本
	 */
	@ManyToOne
	@JoinColumn(name = "siteVersionId", nullable = false)
	public SiteVersion getSiteVersion() {
		return _siteVersion;
	}

	public void setSiteVersion(SiteVersion siteVersion) {
		_siteVersion = siteVersion;
	}
	
	private List<TemplateVersion> _templateVersions = new ArrayList<TemplateVersion>();
	
	/**
	 * 包含的模板版本
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
	@OrderBy("version")
	public List<TemplateVersion> getTemplateVersions() {
		return _templateVersions;
	}
	
	public void setTemplateVersions(List<TemplateVersion> templateVersions) {
		_templateVersions = templateVersions;
	}
	
	private List<TemplateMapping> _templateMappings = new ArrayList<TemplateMapping>();
	
	/**
	 * 关联的模板映射
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
	@OrderBy("templateMappingId")
	public List<TemplateMapping> getTemplateMappings() {
		return _templateMappings;
	}
	
	public void setTemplateMappings(List<TemplateMapping> templateMappings) {
		_templateMappings = templateMappings;
	}
}
