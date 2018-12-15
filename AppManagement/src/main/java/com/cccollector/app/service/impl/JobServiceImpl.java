/**
 * 
 */
package com.cccollector.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.JobDao;
import com.cccollector.app.model.Job;
import com.cccollector.app.service.JobService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 岗位服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("jobService")
public class JobServiceImpl extends GenericServiceHibernateImpl<Integer, Job> implements JobService {
	
	@Autowired
	private JobDao jobDao;

	@Override
	public void addJob(Job job) {
		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("department", new QueryField("departmentId", job.getDepartment().getDepartmentId())));
		Integer position = jobDao.max("position", predicateQueryFieldList);
		job.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 保存岗位
		jobDao.save(job);
	}

	@Override
	public void moveJobs(List<Job> jobs) {
		int position = 0;
		for (Job job : jobs) {
			job.setPosition(position);
			jobDao.update(job, "position");
			position++;
		}
	}
}
