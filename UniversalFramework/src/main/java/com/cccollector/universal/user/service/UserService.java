/**
 * 
 */
package com.cccollector.universal.user.service;

import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;

import com.cccollector.universal.service.GenericService;
import com.cccollector.universal.user.model.User;

import io.swagger.annotations.ApiParam;

/**
 * 用户服务接口
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface UserService extends GenericService<Integer, User> {

	/**
	 * 账号是否存在
	 * 
	 * @param account 账号
	 * @return 返回成功的时候账号存在，其他情况账号不存在
	 */
	public Boolean isAccountExist(@ApiParam(value = "账号", required = true) @FormParam("account") String account);

	/**
	 * 尝试添加用户及用户应用
	 * 
	 * @param appId 应用ID
	 * @param editionId 版本ID
	 * @param releaseId 发行ID
	 * @param account 账号
	 * @param password 密码
	 * @return 用户应用ID
	 */	
	public Integer addUserAndUserApp(@ApiParam(value = "应用ID", required = true) @HeaderParam("appId") int appId, @ApiParam(value = "版本ID", required = true) @HeaderParam("editionId") int editionId, @ApiParam(value = "发行ID", required = true) @HeaderParam("releaseId") int releaseId, @ApiParam(value = "账号", required = true) @FormParam("account") String account, @ApiParam(value = "密码", required = true) @FormParam("password") String password);

}
