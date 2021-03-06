/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;

import com.cccollector.app.model.Certificate;
import com.cccollector.app.model.User;
import com.cccollector.app.model.RootCertificate;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 用户列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class UserListBean extends BaseListBean<User> implements Serializable {
	
	private static final long serialVersionUID = 3860220876863330093L;

	public UserListBean() {
		modelDisplayName = "用户";
		modelAttributeName = "user";
		modelIdAttributeName = "userId";
		submodelAttributeName = "job";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerPage<User>() {

			@Override
			public List<User> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 过滤条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "username":
						predicateQueryFieldList.add(new QueryField("username", value, PredicateEnum.LIKE));
						break;
					case "realName":
						predicateQueryFieldList.add(new QueryField("realName", value, PredicateEnum.LIKE));
						break;
					case "email":
						predicateQueryFieldList.add(new QueryField("email", value, PredicateEnum.LIKE));
						break;
					case "cellphone":
						predicateQueryFieldList.add(new QueryField("cellphone", value, PredicateEnum.LIKE));
						break;
					case "type":
						predicateQueryFieldList.add(new QueryField("type", value));
						break;
					}
				}
				
				predicateQueryFieldList = predicateQueryFieldList.size() == 0 ? null : predicateQueryFieldList;

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("username", true));
				
				// 总行数
				totalRowCount.append(userService.count(predicateQueryFieldList).toString());
				
				return userService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
			}
		};
	}
	
	/**
	 * 加载用户头像缩略图
	 */
	public String loadAvatarThumbnail(User user) {
		if (user.getHasAvatar()) {
			return userService.getUserAvatarThumbnailUrl(user);
		} else {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			int count = Math.max(StringUtils.countMatches(request.getServletPath(), "/") - 1, 0);
			return StringUtils.repeat("../", count) + "main/images/noAvatar.png";
		}
	}

	/**
	 * 用户类别枚举数组
	 */
	public User.TypeEnum[] getTypeEnums() {
		return User.TypeEnum.values();
	}

	/**
	 * 管理证书
	 */
	public void manageCertificatesAction(User user) {
		// 获取可用的根证书
		RootCertificate rootCertificate = rootCertificateService.getEnabledRootCertificate();
		handleNavigation("certificateList.xhtml?rootCertificateId=" + rootCertificate.getRootCertificateId() + "&ownerType=" + Certificate.OwnerTypeEnum.用户.ordinal() + "&ownerId=" + user.getUserId());
	}	
	
	/**
	 * 批量修改用户是否可用
	 */
	public void modifyUsersEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个用户进行修改！");
			return;
		}
		
		// 修改用户是否可用
		for (User user : getSelectedModels()) {
			user.setEnabled(enabled);
			userService.update(user, "enabled");
		}
		addInfoMessage("修改用户是否可用成功！");
		
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 删除用户
	 */
	public void deleteUserAction(User user) {
		// 用户如果否包含子对象，则不能被删除
		if (userService.hasChildren(user)) {
			addErrorMessage("要删除的用户中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除用户
		userService.deleteUser(user);
		addInfoMessage("删除用户成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 批量删除用户
	 */
	public void deleteUsersAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个用户进行删除！");
			return;
		}

		// 用户如果否包含子对象，则不能被删除
		for (User user : getSelectedModels()) {
			if (userService.hasChildren(user)) {
				addErrorMessage("要删除的用户中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除用户
		for (User user : getSelectedModels()) {
			userService.deleteUser(user);
		}
		addInfoMessage("删除用户成功！");

		// 清空选中的模型
		setSelectedModels(null);		
		// 刷新分页模型
		pageModel = null;
	}
}
