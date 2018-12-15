/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.primefaces.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.TemplateVersionDao;
import com.cccollector.news.model.TemplateVersion;
import com.cccollector.news.service.TemplateVersionService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 模板版本服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("templateVersionService")
public class TemplateVersionServiceImpl extends GenericServiceHibernateImpl<Integer, TemplateVersion> implements TemplateVersionService {
	
	@Autowired
	private TemplateVersionDao templateVersionDao;
	
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
	 * 发布路径
	 */
	@Value("${paths.publish}")
	private String publish;
	
	@Override
	public String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}
	
	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}
	
	public String getPublishPath() {
		return nginxDataPath + platformBundleId + File.separator + publish + File.separator;
	}
	
	@Override
	public void addTemplateVersion(TemplateVersion templateVersion, String filePath) {
		// 设置版本号
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("template", new QueryField("templateId", templateVersion.getTemplate().getTemplateId())));
		Integer version = templateVersionDao.max("version", predicateQueryFieldList);
		templateVersion.setVersion(version == null ? 1 : version.intValue() + 1);
		
		// 添加模板版本
		templateVersion.setUpdateTime(new Date());
		templateVersionDao.save(templateVersion);
		
		// 处理文件
		File tempFile = new File(filePath);
		File templateVersionFile = new File(getDataPath() + templateVersion.templateVersionFilePath());
		templateVersionFile.getParentFile().mkdirs();
		try {
			Files.copy(tempFile.toPath(), templateVersionFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			tempFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateTemplateVersion(TemplateVersion templateVersion, String templateContent, String filePath) {
		// 获取模板版本最新数据
		TemplateVersion templateVersionNew = templateVersionDao.loadById(templateVersion.getTemplateVersionId());
		templateVersionNew.setUpdateTime(new Date());
		templateVersionNew.setVersion(templateVersion.getVersion());
		templateVersionNew.setStatus(templateVersion.getStatus());
		
		// 将编辑后的模板内容写入上传的模板文件中
		if (filePath != null) {
			if (filePath.startsWith(getTempPath())) {
				File tempFile = new File(filePath);
				File templateVersionFile = new File(getDataPath() + templateVersionNew.templateVersionFilePath());	
				templateVersionFile.getParentFile().mkdirs();
				try {
					Files.copy(tempFile.toPath(), templateVersionFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				File templateFile = new File(filePath);
				try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(templateFile));) {
					bufferedWriter.write(templateContent);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void deleteTemplateVersion(TemplateVersion templateVersion) {
		// 删除模板版本文件
		File templateVersionFile = new File(getDataPath() + templateVersion.templateVersionFilePath());
		if (templateVersionFile.exists()) {
			templateVersionFile.delete();
		}
		
		// 删除模板版本
		templateVersionDao.delete(templateVersion);
	}
	
	@Override
	public void testTemplateVersion(TemplateVersion templateVersion) {
		// 查询全部测试中的模板
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("template", new QueryField("templateId", templateVersion.getTemplate().getTemplateId())));
		predicateQueryFieldList.add(new QueryField("status", TemplateVersion.StatusEnum.测试中.ordinal()));
		List<TemplateVersion> templateVersionList = templateVersionDao.loadAll(predicateQueryFieldList, null);
		if (templateVersionList != null && templateVersionList.size() > 0) {
			for (TemplateVersion templateVersionTemp : templateVersionList) {
				templateVersionTemp.setStatus(TemplateVersion.StatusEnum.制作中.ordinal());
				templateVersionTemp.setUpdateTime(new Date());
				// 更新状态
				templateVersionDao.update(templateVersionTemp, "updateTime", "status");
			}
		}
		if (templateVersion.getStatus() == TemplateVersion.StatusEnum.制作中.ordinal()) {
			// 设置状态
			templateVersion.setStatus(TemplateVersion.StatusEnum.测试中.ordinal());
			templateVersion.setUpdateTime(new Date());
			// 更新状态
			templateVersionDao.update(templateVersion, "updateTime", "status");
			
		}
	}

	@Override
	public void publishTemplateVersion(TemplateVersion templateVersion) {
		try {
			// 处理模板版本文件
			File templateVersionFile = new File(getDataPath() + templateVersion.templateVersionFilePath());
			boolean success = templateVersionFile.exists();
			if (success) {
				if (templateVersion.getTemplate().getValidateExpression() != null) {
					// 正则校验模板文件内容
					FileInputStream fileInputStream = new FileInputStream(templateVersionFile);
			        int size = fileInputStream.available();
			        byte[] bt=new byte[size];
			        fileInputStream.read(bt);
			        fileInputStream.close();
			        String template = new String(bt, "utf-8");
			        
			        // 临时  需要修改
			        String validateExpression = templateVersion.getTemplate().getValidateExpression();
			        JSONObject jsonObject = new JSONObject(validateExpression);
			        String regex = (String)jsonObject.get("list");
			        Pattern pattern = Pattern.compile(regex);
			        Matcher matcher = pattern.matcher(template);
			        while (matcher.find()) {
			        	System.out.println(matcher.group());
			        }
				}
		        
		        // 查询同一模板下已发布的模板版本
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("template", new QueryField("templateId", templateVersion.getTemplate().getTemplateId())));
				predicateQueryFieldList.add(new QueryField("status", TemplateVersion.StatusEnum.已发布.ordinal()));
				List<TemplateVersion> templateVersionList = templateVersionDao.loadAll(predicateQueryFieldList, null);
				if (templateVersionList != null && templateVersionList.size() > 0) {
					for (TemplateVersion temp : templateVersionList) {
						temp.setStatus(TemplateVersion.StatusEnum.已撤销.ordinal());
						temp.setUpdateTime(new Date());
						templateVersionDao.update(temp, "updateTime", "status");
					}
					cleanPublishedFiles();
				}
				// 拷贝模板文件至发布路径
				writePublishedFiles(templateVersion);
			} else {
				cleanPublishedFiles();
				return;
			}
			// 设置发布状态
			templateVersion.setStatus(TemplateVersion.StatusEnum.已发布.ordinal());
			templateVersion.setUpdateTime(new Date());
			// 更新发布状态
			templateVersionDao.update(templateVersion, "updateTime", "status");
		} catch (IOException e) {
			return;
		}
	}
	
	/**
	 * 写入已发布文件
	 */
	private void writePublishedFiles(TemplateVersion templateVersion) {
		try {
			File templateVersionFile = new File(getDataPath() + templateVersion.templateVersionFilePath());
			File templateVersionPublishFile = new File(getPublishPath() + templateVersion.templateVersionFilePath());
			templateVersionPublishFile.getParentFile().mkdirs();
			// 拷贝文件
			Files.copy(templateVersionFile.toPath(), templateVersionPublishFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 清除所有已发布文件
	 */
	private void cleanPublishedFiles() {
		try {
			File templateVersionFile = new File(getPublishPath());
			if (!templateVersionFile.exists()) {
				return;
			}
			Files.walkFileTree(templateVersionFile.toPath(), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
					if (e == null) {
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					} else {
						throw e;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
