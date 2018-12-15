/**
 * 
 */
package com.cccollector.app.service;

import java.util.List;

import com.cccollector.app.model.Job;
import com.cccollector.universal.service.GenericService;

/**
 * 岗位服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface JobService extends GenericService<Integer, Job> {
	
	/**
	 * 添加岗位
	 * 
	 * @param job 待添加的岗位
	 */
	public void addJob(Job job);
	
	/**
	 * 移动岗位
	 * 
	 * @param jobs 移动后的岗位
	 */
	public void moveJobs(List<Job> jobs);
}
