/**
 * 
 */
package com.cccollector.universal.user.model;

import java.io.Serializable;

/**
 * 交易类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Transaction implements Serializable {

	private static final long serialVersionUID = 8887826350012588091L;

	/**
	 * 交易订单类别枚举
	 */
	public static enum OrderTypeEnum {
		用户平台充值,
		书城平台购书
	}
}
