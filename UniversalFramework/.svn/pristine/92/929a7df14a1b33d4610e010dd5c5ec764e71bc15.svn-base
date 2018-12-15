/**
 * 
 */
package com.cccollector.universal.component.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 兑换码类
 * 
 * @author 杨彪 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class RedeemCode implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8117692285886570884L;

	private Integer _redeemCodeId;

	/**
	 * 兑换码ID
	 */
	public Integer getRedeemCodeId() {
		return _redeemCodeId;
	}

	public void setRedeemCodeId(Integer redeemCodeId) {
		_redeemCodeId = redeemCodeId;
	}

	private String _code;

	/**
	 * 兑换码
	 */
	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		_code = code;
	}

	private Date _generateTime;

	/**
	 * 生成时间
	 */
	public Date getGenerateTime() {
		return _generateTime;
	}

	public void setGenerateTime(Date generateTime) {
		_generateTime = generateTime;
	}

	private Integer _productId;

	/**
	 * 商品ID
	 */
	public Integer getProductId() {
		return _productId;
	}

	public void setProductId(Integer productId) {
		_productId = productId;
	}

	private Integer _orderId;

	/**
	 * 订单ID
	 */
	public Integer getOrderId() {
		return _orderId;
	}

	public void setOrderId(Integer orderId) {
		_orderId = orderId;
	}

	private Date _redeemTime;

	/**
	 * 兑换时间
	 */
	public Date getRedeemTime() {
		return _redeemTime;
	}

	public void setRedeemTime(Date redeemTime) {
		_redeemTime = redeemTime;
	}

	private Integer _status;

	/**
	 * 状态
	 */
	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}

	/**
	 * 状态枚举
	 */
	public static enum StatusEnum {
		未兑换, 已兑换, 已撤单
	}
	
	/**
	 * 状态枚举数组
	 */
	public StatusEnum[] getStatusEnums() {
		return StatusEnum.values();
	}

	
	/**
	 * 状态的枚举
	 */
	public StatusEnum getStatusEnum() {
		return StatusEnum.values()[_status];
	}
}
