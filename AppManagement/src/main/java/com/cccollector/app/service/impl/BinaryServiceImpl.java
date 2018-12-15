/**
 * 
 */
package com.cccollector.app.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.BinaryDao;
import com.cccollector.app.model.Binary;
import com.cccollector.app.service.BinaryService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 二进制文件服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("binaryService")
public class BinaryServiceImpl extends GenericServiceHibernateImpl<Integer, Binary> implements BinaryService {
	
	@Autowired
	private BinaryDao binaryDao;
	
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
	
	private String getTempPath() {
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
	public void addBinary(Binary binary, String filePath) {
		// 设置二进制文件ID
		binary.setBinaryId(binary.getRelease().getReleaseId());
		
		// 设置文件大小
		File file = new File(filePath);
		binary.setFileSize((int) file.length());
		
		// 保存二进制文件
		binaryDao.save(binary);
		
		// 处理文件
		File tempFile = new File(filePath);
		File binaryFile = new File(getDataPath() + binary.binaryFilePath());
		binaryFile.getParentFile().mkdirs();
		try {
			Files.copy(tempFile.toPath(), binaryFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			tempFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateBinary(Binary binary, String filePath) {
		if (binary.getStatusEnum() != Binary.StatusEnum.未发布) {
			return;
		}
		
		// 获取二进制文件最新数据
		Binary binaryNew = binaryDao.loadById(binary.getBinaryId());
		
		// 处理文件
		if (filePath != null && filePath.startsWith(getTempPath())) {
			File tempFile = new File(filePath);
			File binaryFile = new File(getDataPath() + binaryNew.binaryFilePath());	
			binaryFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), binaryFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
				binaryNew.setFileSize((int) binaryFile.length());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteBinary(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.未发布) {
			return;
		}
		
		// 删除文件
		File binaryFile = new File(getDataPath() + binary.binaryFilePath());
		if (binaryFile.exists()) {
			binaryFile.delete();
		}
		
		// 删除二进制文件
		binaryDao.delete(binary);
	}

	@Override
	public void publishBinary(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.未发布) {
			return;
		}
		
		try {
			File binaryFile = new File(getDataPath() + binary.binaryFilePath());
			File binaryPublishFile = new File(getPublishPath() + binary.binaryFilePath());
			binaryPublishFile.getParentFile().mkdirs();
			// 拷贝文件
			Files.copy(binaryFile.toPath(), binaryPublishFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// 设置状态
		binary.setStatus(Binary.StatusEnum.已发布.ordinal());
		
		// 更新二进制文件
		binaryDao.update(binary, "status");
	}

	@Override
	public void cancelPublishBinary(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.已发布) {
			return;
		}
		
		// 删除nginx发布文件
		File binaryPublishFile = new File(getPublishPath() + binary.binaryFilePath());
		binaryPublishFile.delete();
		
		// 设置状态
		binary.setStatus(Binary.StatusEnum.未发布.ordinal());
		
		// 更新二进制文件
		binaryDao.update(binary, "status");
	}

	@Override
	public void discardBinary(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.已发布) {
			return;
		}
		
		// 删除nginx发布文件
		File binaryPublishFile = new File(getPublishPath() + binary.binaryFilePath());
		binaryPublishFile.delete();
	
		// 设置状态
		binary.setStatus(Binary.StatusEnum.已废弃.ordinal());
		
		// 更新二进制文件
		binaryDao.update(binary, "status");
	}
}
