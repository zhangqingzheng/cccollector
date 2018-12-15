/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.Department;
import com.cccollector.app.model.Job;
import com.cccollector.app.model.User;
import com.cccollector.universal.dao.QueryField;

/**
 * 岗位Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class JobBean extends BaseEditBean<Job> implements Serializable {

	private static final long serialVersionUID = 2008369727252496736L;

	public JobBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Job>() {
			
			@Override
			public Job loadModel(Integer modelId) {
				Job job = null;
				if (modelId == null) {
					job = new Job();
					job.setDepartment(getDepartment() != null ? getDepartment() : new Department());
					job.setUser(getUser() != null ? getUser() : new User());
				} else {
					job = jobService.loadById(modelId);
				}
				return job;
			}
			
			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return departmentService.loadById(relatedModelId);
				} else if (index == 2) {
					return userService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}
	
	/**
	 * 岗位
	 */
	public Job getJob() {
		return getModel();
	}

	/**
	 * 所属部门
	 */
	public Department getDepartment() {
		return (Department) relatedModel(1);
	}

	/**
	 * 对应用户
	 */
	public User getUser() {
		return (User) relatedModel(2);
	}
	
	/**
	 * 处理部门自动完成
	 */
	public List<Department> handleDepartmentIdComplete(String query) {
		return departmentService.findDepartmentsBySearchWord(query);
	}
	
	/**
	 * 处理用户自动完成
	 */
	public List<User> handleUserIdComplete(String query) {
		return userService.findUsersBySearchWord(query);
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		Job job = getJob();
		if (job.getJobId() == null) {
			// 处理岗位重复
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("user", new QueryField("userId", job.getUser().getUserId())));
			predicateQueryFieldList.add(new QueryField("department", new QueryField("departmentId", job.getDepartment().getDepartmentId())));
			long count = jobService.count(predicateQueryFieldList);
			if (count > 0) {
				addValidatingMessage("岗位已存在，请勿重复添加");
				return;
			}

			// 添加岗位
			jobService.addJob(job);
			addInfoMessageToFlash("添加岗位成功！");
		} else {
			// 编辑岗位
			jobService.update(job);
			addInfoMessageToFlash("编辑岗位成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("jobList.xhtml?multiSelect=true");
		}
	}
}
