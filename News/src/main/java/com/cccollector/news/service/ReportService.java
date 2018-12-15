/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.Report;
import com.cccollector.universal.service.GenericService;

/**
 * 举报服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface ReportService extends GenericService<Integer, Report> {
	
	/**
	 * 审核通过举报
	 * 
	 * @param report 待审核的举报
	 * @return 是否成功
	 */
	public boolean passReport(Report report);
	
	/**
	 * 审核未通过举报
	 * 
	 * @param report 待审核的举报
	 * @return 是否成功
	 */
	public boolean notPassReport(Report report);
	
	/**
	 * 恢复举报
	 * 
	 * @param report 待恢复的举报
	 * @return 是否成功
	 */
	public boolean recoverReport(Report report);
}
