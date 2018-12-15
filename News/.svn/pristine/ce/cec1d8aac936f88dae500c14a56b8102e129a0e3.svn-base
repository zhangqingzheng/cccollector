/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
 * 模板版本类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "site_templateVersions")
public class TemplateVersion implements Serializable {
	
	private static final long serialVersionUID = 2097627673836472770L;
	
	private Integer _templateVersionId;

	/**
	 * 模板版本ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTemplateVersionId() {
		return _templateVersionId;
	}

	public void setTemplateVersionId(Integer templateVersionId) {
		_templateVersionId = templateVersionId;
	}

	/**
	 * 模版版本文件路径
	 */
	@JsonIgnore
	@Transient
	public String templateVersionFilePath() {
		return getTemplate().getTemplateFilePath() + _template.getTemplateId() + "_template_" + _version + ".tpl";
	}
	
	private Integer _version;
	
	/**
	 * 版本号
	 */
	@Column(nullable = false)
	public Integer getVersion() {
		return _version;
	}
	
	public void setVersion(Integer version) {
		_version = version;
	}
	
	/**
	 * 名称
	 */
	@JsonIgnore
	@Transient
	public String getName() {
		return _template.getName() + "_" + _version + "版";
	}
	
	private Date _updateTime;

	/**
	 * 更新时间
	 */
	@Column(nullable = false)
	public Date getUpdateTime() {
		return _updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		_updateTime = updateTime;
	}
	
	private Integer _status;

	/**
	 * 状态
	 */
	@JsonIgnore	
	@Column(nullable = false)
	public Integer getStatus() {
		return _status;
	}
	
	public void setStatus(Integer status) {
		_status = status;
	}

	/**
	 * 状态枚举
	 */
	public static enum StatusEnum {
		制作中,
		测试中,
		已发布,
		已撤销
	}

	/**
	 * 状态枚举数组
	 */
	@JsonIgnore
	@Transient
	public StatusEnum[] getStatusEnums() {
		return StatusEnum.values();
	}

	/**
	 * 状态的模版枚举
	 */
	@JsonIgnore
	@Transient
	public StatusEnum getStatusEnum() {
		return StatusEnum.values()[_status];
	}	

	private Template _template;

	/**
	 * 所属的模板
	 */
	@ManyToOne
	@JoinColumn(name = "templateId", nullable = false)
	public Template getTemplate() {
		return _template;
	}

	public void setTemplate(Template template) {
		_template = template;
	}
	
	private List<StaticPage> _staticPages = new ArrayList<StaticPage>();
	
	/**
	 * 静态页面
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "templateVersion")
	@OrderBy("staticPageId")
	public List<StaticPage> getStaticPages() {
		return _staticPages;
	}
	
	public void setStaticPages(List<StaticPage> staticPages) {
		_staticPages = staticPages;
	}
}
