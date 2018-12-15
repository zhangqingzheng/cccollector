/**
 * 
 */
package com.cccollector.universal.user.api;

import com.cccollector.universal.service.GenericService;
import com.cccollector.universal.user.model.UserApp;

/**
 * 用户应用API接口
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface UserAppApi extends GenericService<Integer, UserApp> {

	/**
	 * 验证用户应用访问令牌
	 * 
	 * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌
	 * @return 是否验证成功
	 */
    public boolean verifyUserAppAccessToken(int userAppId, String accessToken);

	/**
	 * 获取用户应用
	 * 
	 * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌
	 * @return 用户应用对象
	 */
    public UserApp loadUserApp(int userAppId, String accessToken);

	/**
	 * 根据用户ID获取用户应用
	 * 
	 * @param appId 应用ID
	 * @param userId 用户ID
	 * @param accessToken 访问令牌
	 * @return 用户应用对象
	 */
	@Deprecated
    public UserApp loadUserAppByUserId(int appId, int userId, String accessToken);
}
