/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;

import com.cccollector.app.model.Department;

/**
 * 组织结构图Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class OrganizationChartListBean extends BaseListBean<Department> implements Serializable {
	
	private static final long serialVersionUID = 6545245863256373280L;
	
	/**
	 * 组织结构图
	 */
	public void showOrganizationChartAction() {
		handleNavigation("organizationChartList.xhtml");
	}
	
	/**
	 * 获取组织图根节点
	 */
	private OrganigramNode rootDepartmentOrganigramNode;
	
    public OrganigramNode getRootDepartmentOrganigramNode() {
    	if (rootDepartmentOrganigramNode == null) {
    		Map<Integer, Department> userIndependentDepartments = userDetailService.getUserIndependentDepartments1(getLoginUserDetail().getUserId());
        	rootDepartmentOrganigramNode= new DefaultOrganigramNode("root", "组织结构", null);
        	for (Integer key : userIndependentDepartments.keySet()) {
        		List<Department> departments = userDetailService.loadDepartments1(userIndependentDepartments.get(key).getCode());
        		Map<Integer, OrganigramNode> organigramNodeMap = new HashMap<Integer, OrganigramNode>();
    			for (Department department : departments) {
    				OrganigramNode parentOrganigramNode = department.getParentDepartment() == null ? rootDepartmentOrganigramNode : organigramNodeMap.get(department.getParentDepartment().getDepartmentId());
    				OrganigramNode organigramNode = new DefaultOrganigramNode("department", department.getName(), parentOrganigramNode);
    				organigramNodeMap.put(department.getDepartmentId(), organigramNode);
    			}
        	}
    	}
    	return rootDepartmentOrganigramNode;
    }
}
