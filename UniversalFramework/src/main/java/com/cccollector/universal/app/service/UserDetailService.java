/**
 * 
 */
package com.cccollector.universal.app.service;

import java.util.List;
import java.util.Map;

import com.cccollector.universal.app.model.Department;
import com.cccollector.universal.app.model.Module;
import com.cccollector.universal.app.model.UserDetail;
import com.cccollector.universal.service.GenericService;

/**
 * 用户详情服务接口
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface UserDetailService extends GenericService<Integer, UserDetail> {

	/**
	 * 获取用户头像缩略图URL
	 */
	public String getAvatarThumbnailUsersUrl();
	
	/**
	 * 修改用户资料
     * 
     * @param userId 用户ID
	 * @param email 电子邮箱
	 * @param cellphone 手机号
	 * @return 是否成功 
	 */
	public boolean modifyUserProfile(int userId, String email, String cellphone);
	
	/**
	 * 修改用户密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
	 * @return 是否成功 
	 */
	public boolean modifyUserPassword(int userId, String oldPassword, String newPassword);
	
	/**
	 * 获取登录用户在当前平台下所有的模块权限
	 * 
	 * @return 所有的模块权限
	 */
	public Map<String, Module> getLoggedUserModulePermissions();
	
	/**
	 * 获取用户所属独立部门
	 * 
	 * @param userId 用户ID
	 * @return 部门
	 */
	public Map<Integer, Department> getUserIndependentDepartments(int userId);
	
	/**
	 * 查询部门
	 * 
	 * @param code 部门排序代码
	 * @return 部门
	 */
	public List<Department> loadDepartments(String code);
}
