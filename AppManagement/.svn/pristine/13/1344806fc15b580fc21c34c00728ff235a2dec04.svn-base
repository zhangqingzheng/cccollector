/**
 * 
 */
package com.cccollector.app.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

import com.cccollector.app.model.User;
import com.cccollector.universal.util.ImageUtils;

/**
 * 用户Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class UserBean extends BaseEditBean<User> implements Serializable {

	private static final long serialVersionUID = 4135893484379386882L;

	public UserBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModel<User>() {
			
			@Override
			public User loadModel(Integer modelId) {
				User user = null;
				if (modelId == null) {
					user = new User();
					user.setType(User.TypeEnum.正式.ordinal());				
					user.setEnabled(true);
				} else {
					user = userService.loadById(modelId);

					// 处理头像
					loadPictureFile(1, userService.getDataPath() + user.avatarFilePath());
				}
				return user;
			}
		};
	}

	/**
	 * 用户
	 */
	public User getUser() {
		return getModel();
	}

	/**
	 * 处理头像文件上传
	 */
	public void handleAvatarFileUpload(FileUploadEvent fileUploadEvent) {
		String clientId = fileUploadEvent.getComponent().getClientId();
		int index = Integer.parseInt(clientId.substring(clientId.indexOf("_") + 1));
		try {
			String webAppRootKey = stringFromConfig("paths.webAppRootKey");
			String webTemp = stringFromConfig("paths.webTemp");
			String tempPath = System.getProperty(webAppRootKey) + webTemp + File.separator;
			new File(tempPath).mkdirs();
			String tempFileName = new Date().getTime() + "_" + new Random().nextInt(10000);
			pictureFilePathMap.put(index, tempPath + tempFileName);
			fileUploadEvent.getFile().write(pictureFilePathMap.get(index));
			
			SimpleImageInfo simpleImageInfo = new SimpleImageInfo(new File(pictureFilePathMap.get(index)));
			// 裁切方图
			if (simpleImageInfo.getWidth() != simpleImageInfo.getHeight()) {
				int square = Math.min(simpleImageInfo.getWidth(), simpleImageInfo.getHeight());
				String tempFileNameNew = new Date().getTime() + "_" + new Random().nextInt(10000);
				ImageUtils.cropImage(pictureFilePathMap.get(index), tempPath + tempFileNameNew, userService.getImageMagickPath(), square, square);
				new File(pictureFilePathMap.get(index)).delete();
				pictureFilePathMap.put(index, tempPath + tempFileNameNew);
			}
			
			FileInputStream fileInputStream = new FileInputStream(pictureFilePathMap.get(index));
			pictureImageMap.put(index, new DefaultStreamedContent(fileInputStream));
			pictureInfoMap.put(index, new SimpleImageInfo(new File(pictureFilePathMap.get(index))));
		} catch (Exception e) {
			pictureFilePathMap.remove(index);
			pictureImageMap.remove(index);
			pictureInfoMap.remove(index);
		}
	}
    
	/**
	 * 保存
	 */
	public void saveAction() {
		User user = getUser();
		// 置空
		if (user.getPassword().equals("")) {
			user.setPassword(null);
		}
		if (user.getEmail().equals("")) {
			user.setEmail(null);
		}
		if (user.getCellphone().equals("")) {
			user.setCellphone(null);
		}
		if (user.getRemarks().equals("")) {
			user.setRemarks(null);
		}
		
		// 验证密码
		if (user.getPassword() != null) {
			if (user.getPassword().length() < 8) {
				addValidatingMessage("密码长度必须不少于8位, 请符合规则！");
				return;				
			}
			boolean match = Pattern.matches("^.*[A-Z]+.*$", user.getPassword()) && Pattern.matches("^.*[a-z]+.*$", user.getPassword()) && Pattern.matches("^.*[0-9]+.*$", user.getPassword());			
			if (!match) {
				addValidatingMessage("密码必须包含大小写字母和数字, 请符合规则！");
				return;				
			}
		}
		
		if (user.getUserId() == null) {
			// 添加用户
			userService.addUser(user, pictureFilePath(1));
			addInfoMessageToFlash("添加用户成功！");
		} else {
			// 编辑用户
			userService.updateUser(user, pictureFilePath(1));
			addInfoMessageToFlash("编辑用户成功！");
		}
		
		handleNavigation("userList.xhtml?multiSelect=true");
	}
}
