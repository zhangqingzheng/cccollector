/**
 * 
 */
package com.cccollector.universal.subscription.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Order implements Serializable {
	
	private static final long serialVersionUID = -2270603766181499902L;
	
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
		苹果内购,
		订阅平台,
		兑换码兑换
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

	private Product _product;
	
	/**
	 * 对应的商品
	 */
	public Product getProduct() {
		return _product;
	}

	public void setProduct(Product product) {
		_product = product;
	}
	
	private Integer _productId;

	/**
	 * 对应的商品ID
	 */
	public Integer getProductId() {
		return _productId;
	}

	public void setProductId(Integer productId) {
		_productId = productId;
	}
	
	private BigDecimal _price;

	/**
	 * 价格
	 */
	public BigDecimal getPrice() {
		return _price;
	}

	public void setPrice(BigDecimal price) {
		_price = price;
	}

	private Integer _paymentMethod;

	/**
	 * 付款方式
	 */
	public Integer getPaymentMethod() {
		return _paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		_paymentMethod = paymentMethod;
	}

	/**
	 * 付款方式枚举
	 */
	public static enum PaymentMethodEnum {
		苹果,
		支付宝,
		微信,
		线下,
		移动积分
	}

	/**
	 * 付款方式枚举数组
	 */
	public PaymentMethodEnum[] getPaymentMethodEnums() {
		return PaymentMethodEnum.values();
	}

	/**
	 * 付款方式的枚举
	 */
	public PaymentMethodEnum getPaymentMethodEnum() {
		return PaymentMethodEnum.values()[_paymentMethod];
	}
	
	private Integer _purchaseTime;

	/**
	 * 购买时间
	 */
	public Integer getPurchaseTime() {
		return _purchaseTime;
	}

	public void setPurchaseTime(Integer purchaseTime) {
		_purchaseTime = purchaseTime;
	}
	
	/**
	 * Date类型购买时间
	 */
	public Date getPurchaseTimeDate() {
		if (_purchaseTime != null) {
			return new Date((long) _purchaseTime * 1000);
		}
		return null;
	}
	
	public void setPurchaseTimeDate(Date purchaseTimeDate) {
		if (purchaseTimeDate != null) {
			_purchaseTime = (int) (purchaseTimeDate.getTime() / 1000);
		} else {
			_purchaseTime = (int) (new Date().getTime() / 1000);
		}
	}	

	private Integer _startTime;

	/**
	 * 订阅起始时间
	 */
	public Integer getStartTime() {
		return _startTime;
	}

	public void setStartTime(Integer startTime) {
		_startTime = startTime;
	}

	/**
	 * Date类型订阅起始时间
	 */
	public Date getStartTimeDate() {
		if (_startTime != null) {
			return new Date((long) _startTime * 1000);
		}
		return null;
	}
	
	public void setStartTimeDate(Date startTimeDate) {
		if (startTimeDate != null) {
			_startTime = (int) (startTimeDate.getTime() / 1000);
		} else {
			_startTime = (int) (new Date().getTime() / 1000);
		}
	}

	private String _payInfo;

	/**
	 * 支付信息
	 */
	public String getPayInfo() {
		return _payInfo;
	}

	public void setPayInfo(String payInfo) {
		_payInfo = payInfo;
	}
}
