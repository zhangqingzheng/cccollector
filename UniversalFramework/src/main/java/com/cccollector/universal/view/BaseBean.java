/**
 * 
 */
package com.cccollector.universal.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cccollector.universal.app.model.Module;
import com.cccollector.universal.app.model.Permission;
import com.cccollector.universal.app.model.UserDetail;
import com.cccollector.universal.app.service.UserDetailService;

/**
 * 基础Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public abstract class BaseBean {

	/**
	 * 应用管理平台应用服务
	 */
	@ManagedProperty(value = "#{app_appService}")
	protected com.cccollector.universal.app.service.AppService app_appService;

	public void setApp_appService(com.cccollector.universal.app.service.AppService _app_appService) {
		app_appService = _app_appService;
	}

	/**
	 * 应用管理平台用户详情服务
	 */
	@ManagedProperty(value = "#{app_userDetailService}")
	protected UserDetailService app_userDetailService;

	public void setApp_userDetailService(UserDetailService _app_userDetailService) {
		app_userDetailService = _app_userDetailService;
	}

	/**
	 * 用户平台用户应用服务
	 */
	@ManagedProperty(value = "#{user_userAppService}")
	protected com.cccollector.universal.user.service.UserAppService user_userAppService;

	public void setUser_userAppService(com.cccollector.universal.user.service.UserAppService _user_userAppService) {
		user_userAppService = _user_userAppService;
	}

	/**
	 * 用户平台交易服务
	 */
	@ManagedProperty(value = "#{user_transactionService}")
	protected com.cccollector.universal.user.service.TransactionService user_transactionService;

	public void setUser_transactionService(com.cccollector.universal.user.service.TransactionService _user_transactionService) {
		user_transactionService = _user_transactionService;
	}

	/**
	 * 获取认证
	 */
	public Object getAuthentication() {
		try {
			SecurityContext securityContext = SecurityContextHolder.getContext();
			return securityContext.getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取登录用户详情
	 */
	public UserDetail getLoginUserDetail() {
		Object authentication = getAuthentication();
		if (authentication == null || !(authentication instanceof UserDetail)) {
			return null;
		}
		return (UserDetail) authentication;
	}
	
	/**
	 * 是否匿名认证
	 */
	public boolean isAnonymousAuthentication() {
		Object authentication = getAuthentication();
		return (authentication instanceof String) && ((String) authentication).equals("anonymousUser");
	}
	
	/**
	 * 是否登录认证
	 */
	public boolean isLoginAuthentication() {
		UserDetail userDetail = getLoginUserDetail();
		return userDetail != null;
	}
	
	/**
	 * 是否登录或匿名认证
	 */
	public boolean isLoginOrAnonymousAuthentication() {
		return isLoginAuthentication() || isAnonymousAuthentication();
	}
	
	/**
	 * 获取模块名称
	 * 
	 * @param moduleClass 模块类
	 * @return 模块名称
	 */
	public String getModuleName(Class<?> moduleClass) {
		String className = moduleClass.getName();
		return className.substring(className.lastIndexOf(".") + 1).replace("ListBean", "").replace("Bean", "");
	}
	
	/**
	 * 获取模块
	 * 
	 * @param moduleName 模块名称
	 * @return 模块
	 */
	public Module getModule(String moduleName) {
		UserDetail userDetail = getLoginUserDetail();
		if (userDetail.getModulePermissions() == null) {
			Map<String, Module> modulePermissions = app_userDetailService.getLoggedUserModulePermissions();
			if (modulePermissions == null) {
				modulePermissions = new HashMap<String, Module>();
			}
			userDetail.setModulePermissions(modulePermissions);
		}
		return userDetail.getModulePermissions().get(moduleName);
	}
	
	/**
	 * 是否拥有当前模块
	 */
	public boolean hasModule() {
		return hasModule(getModuleName(getClass()));
	}
	
	/**
	 * 是否拥有模块
	 * 
	 * @param moduleName 模块名称
	 */
	public boolean hasModule(String moduleName) {
		return hasModulePermission(moduleName, null, null);
	}
	
	/**
	 * 是否拥有任一模块
	 * 
	 * @param moduleNames 模块名称
	 */
	public boolean hasModulesAny(String... moduleNames) {
		for (String moduleName : moduleNames) {
			if (hasModule(moduleName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否拥有全部模块
	 * 
	 * @param moduleNames 模块名称
	 */
	public boolean hasModulesAll(String... moduleNames) {
		for (String moduleName : moduleNames) {
			if (!hasModule(moduleName)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 是否拥有特定ID的模块
	 * 
	 * @param moduleName 模块名称
	 * @param specificId 特定ID
	 */
	public boolean hasModule(String moduleName, int specificId) {
		return hasModulePermission(moduleName, specificId, null);
	}
	
	/**
	 * 是否拥有当前模块权限
	 * 
	 * @param type 权限类别
	 */
	public boolean hasModulePermission(int type) {
		return hasModulePermission(getModuleName(getClass()), type);
	}
	
	/**
	 * 是否拥有当前模块任一权限
	 * 
	 * @param types 权限类别
	 */
	public boolean hasModulePermissionsAny(int... types) {
		for (int type : types) {
			if (hasModulePermission(getModuleName(getClass()), type)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否拥有模块权限
	 * 
	 * @param moduleName 模块名称
	 * @param type 权限类别
	 */
	public boolean hasModulePermission(String moduleName, int type) {
		return hasModulePermission(moduleName, null, type);
	}
	
	/**
	 * 是否拥有模块任一权限
	 * 
	 * @param moduleName 模块名称
	 * @param types 权限类别
	 */
	public boolean hasModulePermissionsAny(String moduleName, int... types) {
		for (int type : types) {
			if (hasModulePermission(moduleName, type)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否拥有特定ID的模块权限
	 * 
	 * @param moduleName 模块名称
	 * @param specificId 特定ID
	 * @param type 权限类别
	 */
	public boolean hasModulePermission(String moduleName, Integer specificId, Integer type) {
		// 未登录
		if (!isLoginOrAnonymousAuthentication()) {
			return false;
		}
		// 匿名登录
		if (isAnonymousAuthentication()) {
			return true;
		}
		Module module = getModule(moduleName);
		// 模块不存在
		if (module == null) {
			return false;
		}
		// 不处理权限
		if (specificId == null && type == null) {
			// 如果模块包含的权限有不指定特定ID的，则返回真
			for (Integer key : module.getPermissionMap().keySet()) {
				Permission permission = module.getPermissionMap().get(key);
				if (permission.getSpecificIds() == null) {
					return true;
				}
			}
			return false;
		}
		// 处理特定ID
		if (specificId != null) {
			boolean hasPermission = false;
			for (Integer key : module.getPermissionMap().keySet()) {
				Permission permission = module.getPermissionMap().get(key);
				if (type == null || type.equals(permission.getType())) {
					if (permission.getSpecificIds() == null || ("," + permission.getSpecificIds() + ",").indexOf("," + specificId + ",") >= 0) {
						hasPermission = true;
					}
				}
				if (hasPermission) {
					break;
				}
			}
			return hasPermission;
		}
		// 处理只有权限类别
		for (Integer key : module.getPermissionMap().keySet()) {
			Permission permission = module.getPermissionMap().get(key);
			if (type.equals(permission.getType())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证当前模块
	 */
	public String validateModuleAction() {
		if (hasModule()) {
			return null;
		}
		return "/auth/noPermission";
	}
	
	/**
	 * 验证模块
	 * 
	 * @param moduleName 模块名称
	 */
	public String validateModuleAction(String moduleName) {
		if (hasModule(moduleName)) {
			return null;
		}
		return "/auth/noPermission";
	}
	
	/**
	 * 验证当前模块以及特定ID模块
	 * 
	 * @param specificIdModuleName 特定ID模块名称
	 * @param specificId 特定ID
	 */
	public String validateModuleWithSpecificIdAction(String specificIdModuleName, int specificId) {
		if (hasModule() && hasModule(specificIdModuleName, specificId)) {
			return null;
		}
		return "/auth/noPermission";
	}
	
	/**
	 * 验证模块以及特定ID模块
	 * 
	 * @param moduleName 模块名称
	 * @param specificIdModuleName 特定ID模块名称
	 * @param specificId 特定ID
	 */
	public String validateModuleWithSpecificIdAction(String moduleName, String specificIdModuleName, int specificId) {
		if (hasModule(moduleName) && hasModule(specificIdModuleName, specificId)) {
			return null;
		}
		return "/auth/noPermission";
	}
	
	/**
	 * 是否拥有当前模块管理权限
	 */
	public boolean hasModuleManage() {
		return hasModulePermissionsAny(Permission.TypeEnum.管理.ordinal(), Permission.TypeEnum.添加.ordinal(), Permission.TypeEnum.修改.ordinal(), Permission.TypeEnum.删除.ordinal());
	}
	
	/**
	 * 是否拥有模块管理权限
	 * 
	 * @param moduleName 模块名称
	 */
	public boolean hasModuleManage(String moduleName) {
		return hasModulePermissionsAny(moduleName, Permission.TypeEnum.管理.ordinal(), Permission.TypeEnum.添加.ordinal(), Permission.TypeEnum.修改.ordinal(), Permission.TypeEnum.删除.ordinal());
	}
	
	/**
	 * 获取基础Bean处理器
	 */
	public abstract BaseBeanHandler getBaseBeanHandler();
	
	/**
	 * 获取根路径的相对路径
	 */
	public String getRelativeRootPath() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		int count = Math.max(StringUtils.countMatches(request.getServletPath(), "/") - 1, 0);
		return StringUtils.repeat("../", count);
	}

	/**
	 * 通过键获取配置字符串
	 * 
	 * @param key 键
	 * @return 值字符串
	 */
	public String stringFromConfig(String key) {
		ResourceBundle resourceBundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "configBundle");
		return resourceBundle.getString(key);
	}

	/**
	 * 通过键获取应用管理平台配置字符串
	 * 
	 * @param key 键
	 * @return 值字符串
	 */
	public String stringFromConfigApp(String key) {
		ResourceBundle resourceBundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "configAppBundle");
		return resourceBundle.getString(key);
	}
	
	/**
	 * 加载关联模型
	 */
	private void loadRelatedModel(int index, String relatedModelId) {
		if (relatedModelId == null || relatedModel(index) != null) {
			return;
		}
		Integer id = -1;
		try {
			id = Integer.valueOf(relatedModelId);
		} catch (Exception e) {}
		if (id == -1) {
			return;
		}
		Object relatedModel = getBaseBeanHandler().loadRelatedModel(index, id);
		if (relatedModel != null) {
			relatedModelMap.put(index, relatedModel);
		}
	}

	/**
	 * 模型ID1
	 */
	private String modelId1;

	public String getModelId1() {
		return modelId1;
	}

	public void setModelId1(String _modelId1) {
		modelId1 = _modelId1;
		loadRelatedModel(1, modelId1);
	}

	/**
	 * 模型ID2
	 */
	private String modelId2;

	public String getModelId2() {
		return modelId2;
	}

	public void setModelId2(String _modelId2) {
		modelId2 = _modelId2;
		loadRelatedModel(2, modelId2);
	}

	/**
	 * 模型ID3
	 */
	private String modelId3;

	public String getModelId3() {
		return modelId3;
	}

	public void setModelId3(String _modelId3) {
		modelId3 = _modelId3;
		loadRelatedModel(3, modelId3);
	}

	/**
	 * 模型ID4
	 */
	private String modelId4;
	
	public String getModelId4() {
		return modelId4;
	}
	
	public void setModelId4(String _modelId4) {
		modelId4 = _modelId4;
		loadRelatedModel(4, modelId4);
	}

	/**
	 * 模型ID5
	 */
	private String modelId5;
	
	public String getModelId5() {
		return modelId5;
	}
	
	public void setModelId5(String _modelId5) {
		modelId5 = _modelId5;
		loadRelatedModel(5, modelId5);
	}

	/**
	 * 模型ID6
	 */
	private String modelId6;
	
	public String getModelId6() {
		return modelId6;
	}
	
	public void setModelId6(String _modelId6) {
		modelId6 = _modelId6;
		loadRelatedModel(6, modelId6);
	}

	
	/**
	 * 模型ID7
	 */
	private String modelId7;
	
	public String getModelId7() {
		return modelId7;
	}
	
	public void setModelId7(String _modelId7) {
		modelId7 = _modelId7;
		loadRelatedModel(7, modelId7);
	}
	
	/**
	 * 关联模型映射
	 */
	private Map<Integer, Object> relatedModelMap = new HashMap<>();

	/**
	 * 通过序号获取关联模型
	 * 
	 * @param index 序号
	 * @return 关联模型
	 */
	public Object relatedModel(int index) {
		return relatedModelMap.get(index);
	}

	/**
	 * 处理导航
	 * 
	 * @param navigation 导航
	 */
	protected void handleNavigation(String navigation) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
	    facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, navigation + "&faces-redirect=true&includeViewParams=true");
	}

	/**
	 * 添加提示消息
	 * 
	 * @param message 消息
	 */
	protected void addInfoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "提示信息", message));
	}

	/**
	 * 添加错误消息
	 * 
	 * @param message 消息
	 */
	protected void addErrorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误提示", message));
	}

	/**
	 * 添加验证消息
	 * 
	 * @param message 消息
	 */
	protected void addValidatingMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	/**
	 * 向Flash添加提示消息
	 * 
	 * @param message 消息
	 */
	protected void addInfoMessageToFlash(String message) {
		addInfoMessage(message);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);;
	}

	/**
	 * 向Flash添加错误消息
	 * 
	 * @param message 消息
	 */
	protected void addErrorMessageToFlash(String message) {
		addErrorMessage(message);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);;
	}

	/**
	 * 图片信息处理
	 */	
	public static class SimpleImageInfo {
	    private int height;
	    private int width;
	    private String mimeType;

	    public SimpleImageInfo(File file) throws IOException {
	        InputStream is = new FileInputStream(file);
	        try {
	            processStream(is);
	        } finally {
	            is.close();
	        }
	    }

	    public SimpleImageInfo(InputStream is) throws IOException {
	        processStream(is);
	    }

	    public SimpleImageInfo(byte[] bytes) throws IOException {
	        InputStream is = new ByteArrayInputStream(bytes);
	        try {
	            processStream(is);
	        } finally {
	            is.close();
	        }
	    }

	    private void processStream(InputStream is) throws IOException {
	        int c1 = is.read();
	        int c2 = is.read();
	        int c3 = is.read();

	        mimeType = null;
	        width = height = -1;

	        if (c1 == 'G' && c2 == 'I' && c3 == 'F') { // GIF
	            is.skip(3);
	            width = readInt(is,2,false);
	            height = readInt(is,2,false);
	            mimeType = "image/gif";
	        } else if (c1 == 0xFF && c2 == 0xD8) { // JPG
	            while (c3 == 255) {
	                int marker = is.read();
	                int len = readInt(is,2,true);
	                if (marker == 192 || marker == 193 || marker == 194) {
	                    is.skip(1);
	                    height = readInt(is,2,true);
	                    width = readInt(is,2,true);
	                    mimeType = "image/jpeg";
	                    break;
	                }
	                is.skip(len - 2);
	                c3 = is.read();
	            }
	        } else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
	            is.skip(15);
	            width = readInt(is,2,true);
	            is.skip(2);
	            height = readInt(is,2,true);
	            mimeType = "image/png";
	        } else if (c1 == 66 && c2 == 77) { // BMP
	            is.skip(15);
	            width = readInt(is,2,false);
	            is.skip(2);
	            height = readInt(is,2,false);
	            mimeType = "image/bmp";
	        } else {
	            int c4 = is.read();
	            if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
	                    || (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) { //TIFF
	                boolean bigEndian = c1 == 'M';
	                int ifd = 0;
	                int entries;
	                ifd = readInt(is,4,bigEndian);
	                is.skip(ifd - 8);
	                entries = readInt(is,2,bigEndian);
	                for (int i = 1; i <= entries; i++) {
	                    int tag = readInt(is,2,bigEndian);
	                    int fieldType = readInt(is,2,bigEndian);
	                    int valOffset;
	                    if ((fieldType == 3 || fieldType == 8)) {
	                        valOffset = readInt(is,2,bigEndian);
	                        is.skip(2);
	                    } else {
	                        valOffset = readInt(is,4,bigEndian);
	                    }
	                    if (tag == 256) {
	                        width = valOffset;
	                    } else if (tag == 257) {
	                        height = valOffset;
	                    }
	                    if (width != -1 && height != -1) {
	                        mimeType = "image/tiff";
	                        break;
	                    }
	                }
	            }
	        }
	        if (mimeType == null) {
	            throw new IOException("Unsupported image type");
	        }
	    }
	    
	    private int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
	        int ret = 0;
	        int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
	        int cnt = bigEndian ? -8 : 8;
	        for(int i=0;i<noOfBytes;i++) {
	            ret |= is.read() << sv;
	            sv += cnt;
	        }
	        return ret;
	    }

	    public int getHeight() {
	        return height;
	    }

	    public void setHeight(int height) {
	        this.height = height;
	    }

	    public int getWidth() {
	        return width;
	    }

	    public void setWidth(int width) {
	        this.width = width;
	    }

	    public String getMimeType() {
	        return mimeType;
	    }

	    public void setMimeType(String mimeType) {
	        this.mimeType = mimeType;
	    }

	    @Override
	    public String toString() {
	        return "MIME Type : " + mimeType + "\t Width : " + width + "\t Height : " + height;
	    }
	}
}
