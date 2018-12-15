/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.App;
import com.cccollector.news.model.User;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.service.GenericService;

/**
 * 用户应用服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface UserAppService extends GenericService<Integer, UserApp> {
	
	/**
	 * 同步用户应用
	 * 
	 * @param userApp 待同步的用户应用
	 */
	public boolean syncUserApp(UserApp userApp);
	
	/**
	 * 添加用户应用
	 * 
	 * @param userUserApp 用户平台用户应用
	 * @param userApp 待添加的用户应用
	 * @param user 待添加的用户
	 * @param app 待添加的应用
	 */
	public void addUserApp(com.cccollector.universal.user.model.UserApp userUserApp, UserApp userApp, User user, App app);
}
