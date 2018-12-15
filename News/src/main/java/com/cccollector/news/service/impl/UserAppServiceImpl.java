/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.UserDao;
import com.cccollector.news.model.App;
import com.cccollector.news.model.User;
import com.cccollector.news.dao.UserAppDao;
import com.cccollector.news.model.UserApp;
import com.cccollector.news.service.UserAppService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 用户应用服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("userAppService")
public class UserAppServiceImpl extends GenericServiceHibernateImpl<Integer, UserApp> implements UserAppService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserAppDao userAppDao;
	
	@Autowired
	private com.cccollector.universal.user.service.UserAppService user_userAppService;;

	@Override
	public boolean syncUserApp(UserApp userApp) {
		// 加载用户平台用户应用
		com.cccollector.universal.user.model.UserApp userUserApp = user_userAppService.loadUserAppByUserAppId(userApp.getUserAppId());
		if (userUserApp == null) {
			return false;
		}

		// 加载用户应用
		UserApp userAppCurrent = userAppDao.loadById(userApp.getUserAppId());

		// 更新用户
		userAppCurrent.getUser().setNickName(userUserApp.getUser().getNickName());
		userAppCurrent.getUser().setCellphone(userUserApp.getUser().getCellphone());
		userAppCurrent.getUser().setEmail(userUserApp.getUser().getEmail());
		userAppCurrent.getUser().setRegisterTime(userUserApp.getUser().getRegisterTime());
		
        // 更新用户应用
		userAppCurrent.setCreateTime(userUserApp.getCreateTime());
		userAppCurrent.setSyncTime(new Date());

		return true;
	}

	@Override
	public void addUserApp(com.cccollector.universal.user.model.UserApp userUserApp, UserApp userApp, User user, App app) {
		// 添加用户
		if (user == null) {
			user = new User();
			user.setUserId(userUserApp.getUser().getUserId());
			user.setNickName(userUserApp.getUser().getNickName());
			user.setCellphone(userUserApp.getUser().getCellphone());
			user.setEmail(userUserApp.getUser().getEmail());
			user.setRegisterTime(userUserApp.getUser().getRegisterTime());

			userDao.save(user);
		}

		// 设置用户应用
		userApp.setUserAppId(userUserApp.getUserAppId());
		userApp.setApp(app);
		userApp.setCreateTime(userUserApp.getCreateTime());
		userApp.setUser(user);
		userApp.setSyncTime(new Date());
		
		// 添加用户应用
		userAppDao.save(userApp);
	}
}
