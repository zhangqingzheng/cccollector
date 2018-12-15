/**
 * 
 */
package com.cccollector.app.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.DepartmentDao;
import com.cccollector.app.model.Department;
import com.cccollector.app.service.DepartmentService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 部门服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("departmentService")
public class DepartmentServiceImpl extends GenericServiceHibernateImpl<Integer, Department> implements DepartmentService {
	
	@Autowired
	private DepartmentDao departmentDao;
	
	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;
	
	/**
	 * Tomcat数据目录
	 */
	@Value("${paths.tomcatDataPath}")
	private String tomcatDataPath;
	
	/**
	 * nginx数据目录
	 */
	@Value("${paths.nginxDataPath}")
	private String nginxDataPath;
	
	/**
	 * 应用根路径键
	 */
	@Value("${paths.webAppRootKey}")
	private String webAppRootKey;
	
	/**
	 * 临时路径
	 */
	@Value("${paths.webTemp}")
	private String webTemp;
	
	/**
	 * 图像路径
	 */
	@Value("${paths.image}")
	private String image;
	
	/**
	 * 缩略图URL
	 */
	@Value("${paths.thumbnailUrl}")
	private String thumbnailUrl;
	
	private String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}
	
	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}
	
	private String getImagePath() {
		return nginxDataPath + platformBundleId + File.separator + image + File.separator;
	}
	
	@Override
	public String getDepartmentBadgeThumbnailUrl(Department department) {
		return thumbnailUrl + "w0_h100/" + department.badgeUrlPath();
	}

	@Override
	public List<Department> findDepartmentsBySearchWord(String searchWord) {
		// 根据搜索词查询部门列表
		int id = 0;
		try {
			id = Integer.valueOf(searchWord);
		} catch (Exception e) {}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		if (id > 0) {
			predicateQueryFieldList.add(new QueryField("departmentId", id));
		}
		predicateQueryFieldList.add(new QueryField("name", searchWord, PredicateEnum.LIKE, QueryField.BooleanOperatorEnum.OR));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("code", true));
		List<Department> departments = departmentDao.loadPage(predicateQueryFieldList, orderQueryFieldList, 0, 10);
		if (departments.size() == 0) {
			return null;
		}
		return departments;
	}

	@Override
	public void addDepartment(Department department, String badgeFilePath) {
		// 设置是否有徽章
		department.setHasBadge(false);

		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(department.getParentDepartment() == null ? new QueryField("parentDepartment", null, PredicateEnum.IS_NULL) : new QueryField("parentDepartment", new QueryField("departmentId", department.getParentDepartment().getDepartmentId())));
		Integer position = departmentDao.max("position", predicateQueryFieldList);
		department.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 设置排序代码
		String code = String.format("%02d", department.getPosition());
		if (department.getParentDepartment() != null) {
			code = department.getParentDepartment().getCode() + ":" + code;
		}
		department.setCode(code);
		
		// 保存部门
		departmentDao.save(department);
		
		// 处理徽章文件
		File badgeFile = new File(getDataPath() + department.badgeFilePath());
		if (badgeFilePath != null) {
			File tempFile = new File(badgeFilePath);
			badgeFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), badgeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
				
				// 设置是否有徽章
				department.setHasBadge(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void updateDepartment(Department department, String badgeFilePath) {
		// 获取部门最新数据
		Department departmentNew = departmentDao.loadById(department.getDepartmentId());
		
		// 更新部门
		departmentNew.setName(department.getName());
		departmentNew.setIsIndependent(department.getIsIndependent());
		departmentNew.setRemarks(department.getRemarks());
		
		// 处理徽章文件
		File badgeFile = new File(getDataPath() + departmentNew.badgeFilePath());
		if (badgeFilePath != null) {
			if (badgeFilePath.startsWith(getTempPath())) {
				File tempFile = new File(badgeFilePath);
				badgeFile.getParentFile().mkdirs();
				try {
					Files.copy(tempFile.toPath(), badgeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();
					
					// 设置是否有徽章
					departmentNew.setHasBadge(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			if (badgeFile.exists()) {
				badgeFile.delete();
			}
			
			// 设置是否有徽章
			departmentNew.setHasBadge(false);
		}
		
		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File badgeImageFile = new File(sizeFile, departmentNew.badgeFilePath());
				badgeImageFile.delete();
			}
		}
	}

	@Override
	public void deleteDepartment(Department department) {
		// 删除徽章文件
		File badgeFile = new File(getDataPath() + department.badgeFilePath());
		if (badgeFile.exists()) {
			badgeFile.delete();
		}
		
		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File badgeImageFile = new File(sizeFile, department.badgeFilePath());
				badgeImageFile.delete();
			}
		}
		
		// 删除部门
		departmentDao.delete(department);
	}
}
