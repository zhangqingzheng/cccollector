/**
 * 
 */
package com.cccollector.universal.cxf;

import javax.ws.rs.core.Response;

/**
 * API异常
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@SuppressWarnings("serial")
public class ApiException extends Exception {
	
	public ApiException(Response response) {
		_response = response;
	}
	
	/**
	 * 响应
	 */
	private Response _response;

	public Response getResponse() {
		return _response;
	}
}
