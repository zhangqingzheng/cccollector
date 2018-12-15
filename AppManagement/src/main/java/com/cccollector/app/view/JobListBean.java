/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;

import com.cccollector.app.model.Department;
import com.cccollector.app.model.Job;
import com.cccollector.app.model.User;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 岗位列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class JobListBean extends BaseListBean<Job> implements Serializable {

	private static final long serialVersionUID = 7011017406601134852L;

	public JobListBean() {
		modelDisplayName = "岗位";
		modelAttributeName = "job";
		modelIdAttributeName = "jobId";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Job>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return departmentService.loadById(relatedModelId);
				} else if (index == 2) {
					return userService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Job> loadAllModelList() {
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				if (getDepartment() != null) {
					predicateQueryFieldList.add(new QueryField("department", new QueryField("departmentId", getDepartment().getDepartmentId())));
					orderQueryFieldList.add(new QueryField("position", true));
				}
				if (getUser() != null) {
					predicateQueryFieldList.add(new QueryField("user", new QueryField("userId", getUser().getUserId())));
					orderQueryFieldList.add(new QueryField("department", new QueryField("code", true)));
				}
				return jobService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};
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
	 * 管理角色与权限
	 */
	public void manageRolesAndPermissionsAction(SelectEvent event) {
		Job job = (Job) event.getObject();
		handleNavigation("roleAndPermissionList.xhtml?" + modelIdAttributeName + "=" + job.getJobId());
	}
	
	/**
	 * 移动岗位
	 */
	public void moveJobAction(ReorderEvent event) {
		// 移动岗位
		jobService.moveJobs(allModels);
		addInfoMessage( "移动岗位成功！");
		
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除岗位
	 */
	public void deleteJobAction(Job job) {
		// 岗位如果否包含子对象，则不能被删除
		if (jobService.hasChildren(job)) {
			addErrorMessage("要删除的岗位中包含子对象，请清空子对象再进行删除！");
			return;
		}
		// 删除岗位
		jobService.delete(job);
		addInfoMessage("删除岗位成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除岗位
	 */
	public void deleteJobsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个岗位进行删除！");
			return;
		}
		// 岗位如果否包含子对象，则不能被删除
		for (Job job : getSelectedModels()) {
			if (jobService.hasChildren(job)) {
				addErrorMessage("要删除的岗位中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除岗位
		jobService.deleteAll(getSelectedModels());
		addInfoMessage("删除岗位成功！");
		
		// 清空选中模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
