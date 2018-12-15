/**
 * 
 */
package com.cccollector.universal.subscription.model;

import java.io.Serializable;

/**
 * 订阅类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
public class Subscription implements Serializable {

	private static final long serialVersionUID = -2270603766181499902L;

	private Integer _subscriptionId;

	/**
	 * 订阅ID
	 */
	public Integer getSubscriptionId() {
		return _subscriptionId;
	}

	public void setSubscriptionId(Integer subscriptionId) {
		_subscriptionId = subscriptionId;
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

	private Integer _period;

	/**
	 * 周期
	 */
	public Integer getPeriod() {
		return _period;
	}

	public void setPeriod(Integer period) {
		_period = period;
	}

	/**
	 * 周期枚举
	 */
	public static enum PeriodEnum {
		日, 周, 月, 年
	}

	/**
	 * 周期枚举数组
	 */
	public PeriodEnum[] getPeriodEnums() {
		return PeriodEnum.values();
	}

	/**
	 * 周期的枚举
	 */
	public PeriodEnum getPeriodEnum() {
		return PeriodEnum.values()[_period];
	}

	private Integer _quantity;

	/**
	 * 数量
	 */
	public Integer getQuantity() {
		return _quantity;
	}

	public void setQuantity(Integer quantity) {
		_quantity = quantity;
	}

	private Integer _periodExtended;

	/**
	 * 延长周期
	 */
	public Integer getPeriodExtended() {
		return _periodExtended;
	}

	public void setPeriodExtended(Integer periodExtended) {
		_periodExtended = periodExtended;
	}

	/**
	 * 延长周期枚举数组
	 */
	public PeriodEnum[] getPeriodExtendedEnums() {
		return PeriodEnum.values();
	}

	/**
	 * 延长周期的枚举
	 */
	public PeriodEnum getPeriodExtendedEnum() {
		return PeriodEnum.values()[_periodExtended];
	}

	private Integer _quantityExtended;

	/**
	 * 延长数量
	 */
	public Integer getQuantityExtended() {
		return _quantityExtended;
	}

	public void setQuantityExtended(Integer quantityExtended) {
		_quantityExtended = quantityExtended;
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
