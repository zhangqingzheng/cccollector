/**
 * 
 */
package com.cccollector.app.service;

 import java.util.List;

import com.cccollector.app.model.User;
import com.cccollector.universal.service.GenericService;

/**
 * 用户服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface UserService extends GenericService<Integer, User> {
	
	/**
	 * 获取ImageMagick命令路径
	 */
	public String getImageMagickPath();

	/**
	 * 获取数据目录
	 */
	public String getDataPath();

	/**
	 * 获取用户头像缩略图URL
	 */
	public String getUserAvatarThumbnailUrl(User user);
    
    /**
	 * 根据搜索词查询用户列表
	 * 
	 * @param searchWord 搜索词
	 * @return 用户列表
	 */
    public List<User> findUsersBySearchWord(String searchWord);
	
	/**
	 * 添加用户
	 * 
	 * @param user 待添加的用户
	 * @param avatarFilePath 头像文件路径
	 */
	public void addUser(User user, String avatarFilePath);
	
	/**
	 * 更新用户
	 * 
	 * @param user 待更新的用户
	 * @param avatarFilePath 头像文件路径
	 */
	public void updateUser(User user, String avatarFilePath);
	
	/**
	 * 删除用户
	 * 
	 * @param user 待删除的用户
	 */
	public void deleteUser(User user);
}
