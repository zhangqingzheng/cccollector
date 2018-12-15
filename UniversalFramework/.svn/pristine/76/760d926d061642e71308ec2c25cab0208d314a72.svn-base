/**
 * 
 */
package com.cccollector.universal.subscription.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * 期刊类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Issue implements Serializable {
	
	private static final long serialVersionUID = 1746184904049051818L;
	
	private Integer _issueId;

	/**
	 * 期刊ID
	 */
	public Integer getIssueId() {
		return _issueId;
	}

	public void setIssueId(Integer issueId) {
		_issueId = issueId;
	}

	/**
	 * 期刊名称
	 */
	public String getIssueName() {
		if (_title != null) {
			return _year + "年" + _title;
		}
		return _year + "年" + getTypeEnum() + "第" + _issueNumber + "期";
	}

	/**
	 * 期刊URL路径
	 */
	public String getIssueUrlPath() {
		return getMagazine().getMagazineUrlPath() + _issueId + "/";
	}

	/**
	 * 期刊文件路径
	 */
	public String getIssueFilePath() {
		return getMagazine().getMagazineUrlPath() + _issueId + File.separator;
	}

	private String _issueIdentifier;
	
	/**
	 * 期刊标识符
	 */
	public String getIssueIdentifier() {
		return _issueIdentifier;
	}

	public void setIssueIdentifier(String issueIdentifier) {
		_issueIdentifier = issueIdentifier;
	}
	
	private Integer _type;

	/**
	 * 类别
	 */
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
		正刊,
		增刊,
		预留一,
		预留二,
		预留三
	}

	/**
	 * 类别枚举数组
	 */
	public TypeEnum[] getTypeEnums() {
		return TypeEnum.values();
	}

	/**
	 * 类别的枚举
	 */
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
	}

	private Integer _year;
	
	/**
	 * 年份
	 */
	public Integer getYear() {
		return _year;
	}

	public void setYear(Integer year) {
		_year = year;
	}
	
    private Integer _issueNumber;
	
	/**
	 * 期号
	 */
	public Integer getIssueNumber() {
		return _issueNumber;
	}

	public void setIssueNumber(Integer issueNumber) {
		_issueNumber = issueNumber;
	}
	
    private Integer _totalIssueNumber;
	
	/**
	 * 总期号
	 */
	public Integer getTotalIssueNumber() {
		return _totalIssueNumber;
	}

	public void setTotalIssueNumber(Integer totalIssueNumber) {
		_totalIssueNumber = totalIssueNumber;
	}

	private Date _issueDate;
	
	/**
	 * 发行日期
	 */
	public Date getIssueDate() {
		return _issueDate;
	}

	public void setIssueDate(Date issueDate) {
		_issueDate = issueDate;
	}
	
	private Date _saleTime;
	
	/**
	 * 发售时间
	 */
	public Date getSaleTime() {
		return _saleTime;
	}

	public void setSaleTime(Date saleTime) {
		_saleTime = saleTime;
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
	
	private String _summary;
	
	/**
	 * 摘要
	 */
	public String getSummary() {
		return _summary;
	}

	public void setSummary(String summary) {
		_summary = summary;
	}
	
	private Magazine _magazine;
	
	/**
	 * 所属的杂志
	 */
	public Magazine getMagazine() {
		return _magazine;
	}

	public void setMagazine(Magazine magazine) {
		_magazine = magazine;
	}
}
