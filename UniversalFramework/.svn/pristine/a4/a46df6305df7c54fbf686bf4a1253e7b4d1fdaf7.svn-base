/**
 * 
 */
package com.cccollector.universal.component.service;

import com.cccollector.universal.component.model.RedeemCode;
import com.cccollector.universal.service.GenericService;

/**
 * 兑换码服务接口类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface RedeemCodeService extends GenericService<Integer, RedeemCode> {

	/**
	 * 判断兑换码是否可用
	 * 
	 * @param code 兑换码
	 * @param userAppId 用户应用ID
	 * @return true可用，false不可用
	 */
	public boolean isRedeemCodeCanUse(String code, int userAppId);
}
