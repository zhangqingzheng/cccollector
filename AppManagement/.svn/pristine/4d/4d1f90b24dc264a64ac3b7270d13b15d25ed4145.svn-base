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

import com.cccollector.app.model.Department;
import com.cccollector.app.model.User;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;

/**
 * 人员信息列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class PersonnelInfoListBean extends BaseListBean<Department> implements Serializable {

	private static final long serialVersionUID = -2567466001030383430L;
	
	/**
	 * 获取人员信息表
	 */
	private List<User> personnelInfo;
	
	public List<User> getPersonnelInfo() {
		Map<Integer, Department> userIndependentDepartments = userDetailService.getUserIndependentDepartments1(getLoginUserDetail().getUserId());
		List<Integer> departmentIds = new ArrayList<Integer>();
		for (Integer key : userIndependentDepartments.keySet()) {
			List<Department> departments = userDetailService.loadDepartments1(userIndependentDepartments.get(key).getCode());
			 for (Department department : departments) {
				departmentIds.add(department.getDepartmentId());
			}
		}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("jobs", new QueryField("department", new QueryField("departmentId", departmentIds, PredicateEnum.IN))));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("username", true));
		personnelInfo = userService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		return personnelInfo;
    }
}
