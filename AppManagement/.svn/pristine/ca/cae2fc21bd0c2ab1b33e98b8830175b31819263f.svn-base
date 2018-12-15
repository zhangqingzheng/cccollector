/**
 * 
 */
package com.cccollector.app.service.impl;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.UserDao;
import com.cccollector.app.model.User;
import com.cccollector.app.service.UserService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 用户服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("userService")
public class UserServiceImpl extends GenericServiceHibernateImpl<Integer, User> implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;
	
	/**
	 * ImageMagick命令路径
	 */
	@Value("${imageMagickPath}")
	private String imageMagickPath;
	
	/**
	 * Tomcat数据目录
	 */
	@Value("${paths.tomcatDataPath}")
	private String tomcatDataPath;
	
	/**
	 * nginx数据目录
	 */
	@Value("${paths.nginxDataPath}")
	private String nginxDataPath;
	
	/**
	 * 应用根路径键
	 */
	@Value("${paths.webAppRootKey}")
	private String webAppRootKey;
	
	/**
	 * 临时路径
	 */
	@Value("${paths.webTemp}")
	private String webTemp;
	
	/**
	 * 图像路径
	 */
	@Value("${paths.image}")
	private String image;
	
	/**
	 * 缩略图URL
	 */
	@Value("${paths.thumbnailUrl}")
	private String thumbnailUrl;
	
	@Override
	public String getImageMagickPath() {
		return imageMagickPath;
	}

	public String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}

	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}

	public String getImagePath() {
		return nginxDataPath + platformBundleId + File.separator + image + File.separator;
	}

	@Override
	public String getUserAvatarThumbnailUrl(User user) {
		return thumbnailUrl + "w0_h100/" + user.avatarUrlPath();
	}
	
	@Override
	public List<User> findUsersBySearchWord(String searchWord) {
		// 根据搜索词查询用户列表
		int id = 0;
		try {
			id = Integer.valueOf(searchWord);
		} catch (Exception e) {}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		if (id > 0) {
			predicateQueryFieldList.add(new QueryField("userId", id));
		}
		predicateQueryFieldList.add(new QueryField("username", searchWord, PredicateEnum.LIKE, QueryField.BooleanOperatorEnum.OR));
		predicateQueryFieldList.add(new QueryField("realName", searchWord, PredicateEnum.LIKE, QueryField.BooleanOperatorEnum.OR));
		predicateQueryFieldList.add(new QueryField("email", searchWord, PredicateEnum.LIKE, QueryField.BooleanOperatorEnum.OR));
		predicateQueryFieldList.add(new QueryField("cellphone", searchWord, PredicateEnum.LIKE, QueryField.BooleanOperatorEnum.OR));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("username", true));
		List<User> users = userDao.loadPage(predicateQueryFieldList, orderQueryFieldList, 0, 10);
		if (users.size() == 0) {
			return null;
		}
		return users;
	}

	@Override
	public void addUser(User user, String avatarFilePath) {
		// 设置密码盐
		user.setSalt(createRandomSalt(40));
		
		// 设置密码
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			String salt = platformBundleId + user.getSalt();
			messageDigest.update(salt.getBytes("UTF-8"));
			byte[] hashed = messageDigest.digest(user.getPassword().getBytes("UTF-8"));
			String passwordMD5 = new BigInteger(1, hashed).toString(16);
			user.setPassword(passwordMD5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 设置是否有头像
		user.setHasAvatar(false);
		
		// 设置创建时间
		user.setCreateTimeDate(new Date());
		
		// 保存用户
		userDao.save(user);
		
		// 处理头像文件
		File avatarFile = new File(getDataPath() + user.avatarFilePath());
		if (avatarFilePath != null) {
			File tempFile = new File(avatarFilePath);
			avatarFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), avatarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
				
				// 设置是否有头像
				user.setHasAvatar(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateUser(User user, String avatarFilePath) {
		// 获取用户最新数据
		User userNew = userDao.loadById(user.getUserId());
		
		if (user.getPassword() != null) {
			try {
				String salt = createRandomSalt(40);
				String saltFinal = platformBundleId + salt;
				MessageDigest messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(saltFinal.getBytes("UTF-8"));
				byte[] hashed = messageDigest.digest(user.getPassword().getBytes("UTF-8"));
				String passwordMD5 = new BigInteger(1, hashed).toString(16);
				// 更新密码和密码盐
				userNew.setPassword(passwordMD5);
				userNew.setSalt(salt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 更新用户
		userNew.setEmail(user.getEmail());
		userNew.setCellphone(user.getCellphone());
		userNew.setRemarks(user.getRemarks());
		userNew.setType(user.getType());
		userNew.setEnabled(user.getEnabled());
		userNew.setExpiresDate(user.getExpiresDate());
		
		// 处理头像文件
		File avatarFile = new File(getDataPath() + userNew.avatarFilePath());
		if (avatarFilePath != null) {
			if (avatarFilePath.startsWith(getTempPath())) {
				File tempFile = new File(avatarFilePath);
				avatarFile.getParentFile().mkdirs();
				try {
					Files.copy(tempFile.toPath(), avatarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();
					
					// 设置是否有头像
					userNew.setHasAvatar(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			if (avatarFile.exists()) {
				avatarFile.delete();
			}
			
			// 设置是否有头像
			userNew.setHasAvatar(false);
		}
		
		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File avatarImageFile = new File(sizeFile, userNew.avatarFilePath());
				avatarImageFile.delete();
			}
		}
	}

	@Override
	public void deleteUser(User user) {
		// 删除头像文件
		File avatarFile = new File(getDataPath() + user.avatarFilePath());
		if (avatarFile.exists()) {
			avatarFile.delete();
		}
		
		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File avatarImageFile = new File(sizeFile, user.avatarFilePath());
				avatarImageFile.delete();
			}
		}
		
		// 删除用户
		userDao.delete(user);
	}
	
	/**
	 * 随机生成40位密码盐
	 */
	public static String createRandomSalt(int length) {
		// 密码盐
        String value = "";  
        Random random = new Random();  
           
        for (int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if ( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出大写字母或小写字母 
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                value += (char)(random.nextInt(26) + temp);  
            } else if ( "num".equalsIgnoreCase(charOrNum) ) {  
                value += String.valueOf(random.nextInt(10));  
            }  
        }  
        return value;
	}
}
