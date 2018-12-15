/**
 * 
 */
package com.cccollector.universal.user.service;

import java.util.List;

import com.cccollector.universal.service.GenericService;
import com.cccollector.universal.user.model.UserApp;

/**
 * 用户应用服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface UserAppService extends GenericService<Integer, UserApp> {
	
	/**
	 * 根据用户应用ID加载用户应用
	 * 
	 * @return 用户应用
	 */
	public UserApp loadUserAppByUserAppId(int userAppId);
    
	/**
	 * 根据搜索词加载用户应用列表
	 * 
	 * @param searchWord 搜索词
	 * @return 用户应用列表
	 */
    public List<UserApp> loadUserAppsBySearchWord(String searchWord);
}
