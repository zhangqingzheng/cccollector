/**
 * 
 */
package com.cccollector.universal.subscription.model;

import java.io.Serializable;

/**
 * 明细类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Detail implements Serializable {
	
	private static final long serialVersionUID = -2270603766181499902L;
	
	private Integer _detailId;

	/**
	 * 明细ID
	 */
	public Integer getDetailId() {
		return _detailId;
	}

	public void setDetailId(Integer detailId) {
		_detailId = detailId;
	}

	private Integer _contentType;

	/**
	 * 内容类别
	 */
	public Integer getContentType() {
		return _contentType;
	}

	public void setContentType(Integer contentType) {
		_contentType = contentType;
	}

	/**
	 * 内容类别枚举
	 */
	public static enum ContentTypeEnum {
		期刊,
		订阅	
	}

	/**
	 * 内容类别枚举数组
	 */
	public ContentTypeEnum[] getContentTypeEnums() {
		return ContentTypeEnum.values();
	}

	/**
	 * 内容类别的枚举
	 */
	public ContentTypeEnum getContentTypeEnum() {
		return ContentTypeEnum.values()[_contentType];
	}
	
	private Integer _contentId;

	/**
	 * 内容ID
	 */
	public Integer getContentId() {
		return _contentId;
	}

	public void setContentId(Integer contentId) {
		_contentId = contentId;
	}

	private Integer _format;

	/**
	 * 版式
	 */
	public Integer getFormat() {
		return _format;
	}

	public void setFormat(Integer format) {
		_format = format;
	}

	/**
	 * 版式枚举
	 */
	public static enum FormatEnum {
		纸质,
		照排,
		网页,
		前端
	}

	/**
	 * 版式枚举数组
	 */
	public FormatEnum[] getFormatEnums() {
		return FormatEnum.values();
	}

	/**
	 * 版式的枚举
	 */
	public FormatEnum getFormatEnum() {
		return FormatEnum.values()[_format];
	}

	private Magazine _magazine;
	
	/**
	 * 关联的杂志
	 */
	public Magazine getMagazine() {
		return _magazine;
	}

	public void setMagazine(Magazine magazine) {
		_magazine = magazine;
	}
	
	private Integer _magazineId;

	/**
	 * 关联的杂志ID
	 */
	public Integer getMagazineId() {
		return _magazineId;
	}

	public void setMagazineId(Integer magazineId) {
		_magazineId = magazineId;
	}
	
	private Product _product;
	
	/**
	 * 所属的商品
	 */
	public Product getProduct() {
		return _product;
	}

	public void setProduct(Product product) {
		_product = product;
	}
}
