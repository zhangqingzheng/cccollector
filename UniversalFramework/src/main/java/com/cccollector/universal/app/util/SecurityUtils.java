/**
 * 
 */
package com.cccollector.universal.app.util;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cccollector.universal.app.model.UserDetail;

/**
 * 安全工具类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class SecurityUtils {

	/**
	 * 获取认证
	 */
	public static Object getAuthentication() {
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
	public static UserDetail getLoginUserDetail() {
		Object authentication = getAuthentication();
		if (authentication == null || !(authentication instanceof UserDetail)) {
			return null;
		}
		return (UserDetail) authentication;
	}
	
	/**
	 * 是否匿名认证
	 */
	public static boolean isAnonymousAuthentication() {
		Object authentication = getAuthentication();
		return (authentication instanceof String) && ((String) authentication).equals("anonymousUser");
	}
	
	/**
	 * 是否登录认证
	 */
	public static boolean isLoginAuthentication() {
		UserDetail userDetail = getLoginUserDetail();
		return userDetail != null;
	}
	
	/**
	 * 是否登录或匿名认证
	 */
	public static boolean isLoginOrAnonymousAuthentication() {
		return isLoginAuthentication() || isAnonymousAuthentication();
	}
	
	/**
	 * 是否审核中
	 */
	public static boolean inReview() {
		UserDetail userDetail = getLoginUserDetail();
		return userDetail != null && userDetail.getUserId() != null && userDetail.getCertificateEnabled() != null && userDetail.getCertificateEnabled();
	}
}
