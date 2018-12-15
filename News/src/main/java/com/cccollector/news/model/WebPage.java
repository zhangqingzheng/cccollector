/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

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
 * 网络页面类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "site_webPages")
public class WebPage implements Serializable {
	
	private static final long serialVersionUID = 7955449511917173408L;

	private Integer _webPageId;

	/**
	 * 网络页面ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getWebPageId() {
		return _webPageId;
	}

	public void setWebPageId(Integer webPageId) {
		_webPageId = webPageId;
	}
	
	/**
	 * 网络页面URL路径
	 */
	@JsonIgnore
	@Transient
	public String webPageUrlPath() {
		return getWebFolder().getWebFolderUrlPath() + _webName + "/";
	}

	/**
	 * 网络页面文件路径
	 */
	@JsonIgnore
	@Transient
	public String webPageFilePath() {
		return getWebFolder().getWebFolderFilePath() + _webName + File.separator;
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

	private String _webName;

	/**
	 * 网络名称
	 */
	@Column(nullable = false, length = 100)
	public String getWebName() {
		return _webName;
	}

	public void setWebName(String webName) {
		_webName = webName;
	}

	private Date _updateTime;

	/**
	 * 更新时间
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Date getUpdateTime() {
		return _updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		_updateTime = updateTime;
	}
	
	private Date _publishTime;

	/**
	 * 发布时间
	 */
	@JsonIgnore
	@Column(nullable = true)
	public Date getPublishTime() {
		return _publishTime;
	}

	public void setPublishTime(Date publishTime) {
		_publishTime = publishTime;
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

	private WebFolder _webFolder;

	/**
	 * 所属的网络文件夹
	 */
	@ManyToOne
	@JoinColumn(name = "webFolderId", nullable = false)
	public WebFolder getWebFolder() {
		return _webFolder;
	}

	public void setWebFolder(WebFolder webFolder) {
		_webFolder = webFolder;
	}
}
