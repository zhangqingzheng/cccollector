/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 模板映射类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "site_templateMappings")
public class TemplateMapping implements Serializable {
	
	private static final long serialVersionUID = 1087988739708758736L;
	
	private Integer _templateMappingId;

	/**
	 * 模板映射ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTemplateMappingId() {
		return _templateMappingId;
	}

	public void setTemplateMappingId(Integer templateMappingId) {
		_templateMappingId = templateMappingId;
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
	
	private Integer _sourceTemplateType;

	/**
	 * 源模版类别
	 */
	@Column(nullable = false)
	public Integer getSourceTemplateType() {
		return _sourceTemplateType;
	}

	public void setSourceTemplateType(Integer sourceTemplateType) {
		_sourceTemplateType = sourceTemplateType;
	}

	/**
	 * 栏目源模版类别枚举
	 */
	public static enum ColumnSourceTemplateTypeEnum {
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
	 * 栏目源模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ColumnSourceTemplateTypeEnum[] getColumnSourceTemplateTypeEnums() {
		return ColumnSourceTemplateTypeEnum.values();
	}

	/**
	 * 栏目源模版类别枚举
	 */
	@JsonIgnore
	@Transient
	public ColumnSourceTemplateTypeEnum getColumnSourceTemplateTypeEnum() {
		return ColumnSourceTemplateTypeEnum.values()[_sourceTemplateType];
	}

	/**
	 * 文章源模版类别枚举
	 */
	public static enum ArticleSourceTemplateTypeEnum {
		图文混排_分类标签_赞and不喜欢_相关阅读,
		图文分离,
		图集,
		杂志,
		图书,
	    顶部悬浮音频,
		顶部悬浮视频
	}

	/**
	 * 文章源模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ArticleSourceTemplateTypeEnum[] getArticleSourceTemplateTypeEnums() {
		return ArticleSourceTemplateTypeEnum.values();
	}

	/**
	 * 文章源模版类别枚举
	 */
	@JsonIgnore
	@Transient
	public ArticleSourceTemplateTypeEnum getArticleSourceTemplateTypeEnum() {
		return ArticleSourceTemplateTypeEnum.values()[_sourceTemplateType];
	}
	
	/**
	 * 其它源模版类别枚举
	 */
	public static enum OtherSourceTemplateTypeEnum {
		分类标签
	}
	
	/**
	 * 其它源模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public OtherSourceTemplateTypeEnum[] getOtherSourceTemplateTypeEnums() {
		return OtherSourceTemplateTypeEnum.values();
	}
	
	/**
	 * 其它源模版类别枚举
	 */
	@JsonIgnore
	@Transient
	public OtherSourceTemplateTypeEnum getOtherSourceTemplateTypeEnum() {
		return OtherSourceTemplateTypeEnum.values()[_sourceTemplateType];
	}

	/**
	 * 获取源模版类别类别枚举名称
	 */
	@JsonIgnore
	@Transient
	public String getSourceTemplateTypeEnumName() {
		return getTypeEnum() == TypeEnum.栏目 ? getColumnSourceTemplateTypeEnum().name().replace('_', '+') : (getTypeEnum() == TypeEnum.文章 ? getArticleSourceTemplateTypeEnum().name().replace('_', '+') : getOtherSourceTemplateTypeEnum().name().replace('_', '+'));
	}
	
	private Integer _targetTemplateType;

	/**
	 * 目标模版类别
	 */
	@Column(nullable = false)
	public Integer getTargetTemplateType() {
		return _targetTemplateType;
	}

	public void setTargetTemplateType(Integer targetTemplateType) {
		_targetTemplateType = targetTemplateType;
	}

	/**
	 * 栏目目标模版类别枚举
	 */
	public static enum ColumnTargetTemplateTypeEnum {
		子栏目列表,
		文章列表,
		子栏目_文章列表
	}

	/**
	 * 栏目目标模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ColumnTargetTemplateTypeEnum[] getColumnTargetTemplateTypeEnums() {
		return ColumnTargetTemplateTypeEnum.values();
	}

	/**
	 * 栏目目标模版类别枚举
	 */
	@JsonIgnore
	@Transient
	public ColumnTargetTemplateTypeEnum getColumnTargetTemplateTypeEnum() {
		return ColumnTargetTemplateTypeEnum.values()[_targetTemplateType];
	}

	/**
	 * 文章目标模版类别枚举
	 */
	public static enum ArticleTargetTemplateTypeEnum {
		图文混排
	}

	/**
	 * 文章目标模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ArticleTargetTemplateTypeEnum[] getArticleTargetTemplateTypeEnums() {
		return ArticleTargetTemplateTypeEnum.values();
	}

	/**
	 * 文章目标模版类别枚举
	 */
	@JsonIgnore
	@Transient
	public ArticleTargetTemplateTypeEnum getArticleTargetTemplateTypeEnum() {
		return ArticleTargetTemplateTypeEnum.values()[_targetTemplateType];
	}
	
	/**
	 * 其它目标模版类别枚举
	 */
	public static enum OtherTargetTemplateTypeEnum {
		分类标签
	}
	
	/**
	 * 其它目标模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public OtherTargetTemplateTypeEnum[] getOtherTargetTemplateTypeEnums() {
		return OtherTargetTemplateTypeEnum.values();
	}
	
	/**
	 * 其它目标模版类别枚举
	 */
	@JsonIgnore
	@Transient
	public OtherTargetTemplateTypeEnum getOtherTargetTemplateTypeEnum() {
		return OtherTargetTemplateTypeEnum.values()[_targetTemplateType];
	}
	
	/**
	 * 获取目标模版类别类别枚举名称
	 */
	@JsonIgnore
	@Transient
	public String getTargetTemplateTypeEnumName() {
		return getTypeEnum() == TypeEnum.栏目 ? getColumnTargetTemplateTypeEnum().name().replace('_', '+') : (getTypeEnum() == TypeEnum.文章 ? getArticleTargetTemplateTypeEnum().name().replace('_', '+') : getOtherTargetTemplateTypeEnum().name().replace('_', '+'));
	}
	
	private Integer _contentId;
	
	/**
	 * 内容ID
	 */
	@Column(nullable = true)
	public Integer getContentId() {
		return _contentId;
	}
	
	public void setContentId(Integer contentId) {
		_contentId = contentId;
	}

	private Object _content;
	
	/**
	 * 内容
	 */
	@JsonIgnore
	@Transient
	public Object getContent() {
		return _content;
	}
	
	@Transient
	public void setContent(Object content) {
		_content = content;
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
	
	private Template _template;

	/**
	 * 关联的模板
	 */
	@ManyToOne
	@JoinColumn(name = "templateId", nullable = false)
	public Template getTemplate() {
		return _template;
	}

	public void setTemplate(Template template) {
		_template = template;
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
}
