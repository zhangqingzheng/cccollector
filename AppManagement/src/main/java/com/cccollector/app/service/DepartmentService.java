/**
 * 
 */
package com.cccollector.app.service;

import java.util.List;

import com.cccollector.app.model.Department;
import com.cccollector.universal.service.GenericService;

/**
 * 部门服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface DepartmentService extends GenericService<Integer, Department> {
	
	/**
	 * 获取数据目录
	 */
	public String getDataPath();
	
	/**
	 * 获取部门徽章缩略图URL
	 */
	public String getDepartmentBadgeThumbnailUrl(Department department);
    
    /**
	 * 根据搜索词查询部门列表
	 * 
	 * @param searchWord 搜索词
	 * @return 部门列表
	 */
    public List<Department> findDepartmentsBySearchWord(String searchWord);
	
	/**
	 * 添加部门
	 * 
	 * @param department 待添加的部门
	 * @param badgeFilePath 徽章文件路径
	 */
	public void addDepartment(Department department, String badgeFilePath);
	
	/**
	 * 更新部门
	 * 
	 * @param department 待更新的部门
	 * @param badgeFilePath 徽章文件路径
	 */
	public void updateDepartment(Department department, String badgeFilePath);
	
	/**
	 * 删除部门
	 * 
	 * @param department 待删除的部门
	 */
	public void deleteDepartment(Department department);
}
