/**
 * 
 */
package com.cccollector.universal.user.service;

import java.math.BigDecimal;

import com.cccollector.universal.service.GenericService;
import com.cccollector.universal.user.model.Transaction;

/**
 * 交易服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface TransactionService extends GenericService<Integer, Transaction> {
	
    /**
     * 添加交易
     * 
     * @param userAppId 用户应用ID
     * @param productName 商品名称
     * @param price 价格
     * @param orderType 订单类别
     * @param orderId 订单ID
     * @return 是否成功
     */
    public boolean addTransaction(int userAppId, String productName, BigDecimal price, int orderType, int orderId);
	
    /**
     * 取消交易
     * 
     * @param userAppId 用户应用ID
     * @param productName 商品名称
     * @param price 价格
     * @param orderType 订单类别
     * @param orderId 订单ID
     * @return 是否成功
     */
    public boolean cancelTransaction(int userAppId, String productName, BigDecimal price, int orderType, int orderId);
}
