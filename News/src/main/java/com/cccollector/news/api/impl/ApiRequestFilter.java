/**
 * 
 */
package com.cccollector.news.api.impl;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.cccollector.news.model.UserApp;
import com.cccollector.news.service.UserAppService;

/**
 * API请求过滤器
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Provider
public class ApiRequestFilter implements ContainerRequestFilter {

	/**
	 * 用户应用服务
	 */
	private UserAppService _userAppService;

	public void setUserAppService(UserAppService userAppService) {
		_userAppService = userAppService;
	}

	/**
	 * 用户应用API
	 */
	private com.cccollector.universal.user.api.UserAppApi _user_userAppApi;

	public void setUser_userAppApi(com.cccollector.universal.user.api.UserAppApi user_userAppApi) {
		_user_userAppApi = user_userAppApi;
	}	
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String path = requestContext.getUriInfo().getPath();
		if (path.startsWith("articles")) {
			if (path.endsWith("/dynamic") || path.endsWith("/comments") || path.endsWith("/comments/top")) {
				// 获取已登录用户应用ID
				String loggedUserAppIdString = requestContext.getHeaderString("loggedUserAppId");
				int loggedUserAppId = 0;
				try {
					loggedUserAppId = Integer.valueOf(loggedUserAppIdString);
				} catch (Exception e) {}
				// 获取已登录访问令牌
				String loggedAccessToken = requestContext.getHeaderString("loggedAccessToken");

				if (loggedUserAppId > 0 || !(loggedAccessToken == null || loggedAccessToken.isEmpty())) {
					// 获取用户应用对象
					UserApp loggedUserApp = _userAppService.loadById(loggedUserAppId);
					// 如果没有获取到
					if (loggedUserApp == null) {
						requestContext.abortWith(Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).type(MediaType.APPLICATION_JSON).build());
						return;
					}
					// 如果应用不可用
					if (!loggedUserApp.getApp().getAppId().equals(loggedUserApp.getApp().getAppId())) {
						requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.应用不可用.toJson()).type(MediaType.APPLICATION_JSON).build());
						return;
					}
					// 验证访问令牌
					if (!_user_userAppApi.verifyUserAppAccessToken(loggedUserAppId, loggedAccessToken)) {
						requestContext.abortWith(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.验证访问令牌失败.toJson()).type(MediaType.APPLICATION_JSON).build());
						return;
					}
				}
			}
		}
		if (path.startsWith("userApps")) {
			if (path.endsWith("/findBySearchWord")) {
				return;
			}

			// 获取用户应用ID
			String userAppIdString = requestContext.getUriInfo().getPathParameters().getFirst("userAppId");
			int userAppId = 0;
			try {
				userAppId = Integer.valueOf(userAppIdString);
			} catch (Exception e) {}
			// 验证必填项
			if (userAppId == 0) {
				requestContext.abortWith(Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).type(MediaType.APPLICATION_JSON).build());
				return;
			}

			// 获取用户应用对象
			UserApp userApp = _userAppService.loadById(userAppId);
			// 如果没有获取到
			if (userApp == null && !path.endsWith("/sync")) {
				requestContext.abortWith(Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).type(MediaType.APPLICATION_JSON).build());
				return;
			}
			// 如果应用不可用
			if (!(userApp == null && path.endsWith("/sync")) && !userApp.getApp().getEnabled()) {
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.应用不可用.toJson()).type(MediaType.APPLICATION_JSON).build());
				return;
			}

			if (path.endsWith("/" + userAppIdString) || path.endsWith("/followUserApps") || path.endsWith("/followedUserApps")) {
				return;
			} else if (path.endsWith("/comments") || path.endsWith("/replies") || path.endsWith("/likes")) {
				// 获取已登录用户应用ID
				String loggedUserAppIdString = requestContext.getHeaderString("loggedUserAppId");
				int loggedUserAppId = 0;
				try {
					loggedUserAppId = Integer.valueOf(loggedUserAppIdString);
				} catch (Exception e) {}
				// 获取已登录访问令牌
				String loggedAccessToken = requestContext.getHeaderString("loggedAccessToken");

				if (loggedUserAppId > 0 || !(loggedAccessToken == null || loggedAccessToken.isEmpty())) {
					// 获取用户应用对象
					UserApp loggedUserApp = _userAppService.loadById(loggedUserAppId);
					// 如果没有获取到
					if (loggedUserApp == null) {
						requestContext.abortWith(Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).type(MediaType.APPLICATION_JSON).build());
						return;
					}
					// 如果应用不可用
					if (!loggedUserApp.getApp().getAppId().equals(userApp.getApp().getAppId())) {
						requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.应用不可用.toJson()).type(MediaType.APPLICATION_JSON).build());
						return;
					}
					// 验证访问令牌
					if (!_user_userAppApi.verifyUserAppAccessToken(loggedUserAppId, loggedAccessToken)) {
						requestContext.abortWith(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.验证访问令牌失败.toJson()).type(MediaType.APPLICATION_JSON).build());
						return;
					}
				}
			} else {
				// 获取访问令牌
				String accessToken = requestContext.getHeaderString("accessToken");
				// 验证必填项
				if (accessToken.isEmpty()) {
					requestContext.abortWith(Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).type(MediaType.APPLICATION_JSON).build());
					return;
				}

				// 验证访问令牌
				if (!_user_userAppApi.verifyUserAppAccessToken(userAppId, accessToken)) {
					requestContext.abortWith(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.验证访问令牌失败.toJson()).type(MediaType.APPLICATION_JSON).build());
					return;
				}
			}
		}
	}

	/**
	 * 错误消息枚举
	 */
	public enum ErrorMessage {
		缺少必填项(1, "缺少必填项"),
		应用不存在(2, "应用不存在"),
		应用不可用(3, "应用不可用"),
		用户应用不存在(4, "用户应用不存在"),
		验证访问令牌失败(5, "验证访问令牌失败");

		private ErrorMessage(int code, String message) {
			_code = code;
			_message = message;
		}

		/**
		 * 代码
		 */
		private int _code;

		public int getCode() {
			return _code;
		}

		/**
		 * 消息
		 */
		private String _message;

		public String getMessage() {
			return _message;
		}

		/**
		 * 转换为JSON
		 */
		public String toJson() {
			return "{\"code\" : \"" + _code + "\", \"message\" : \"" + _message + "\"}";
		}
	}
}
