/**
 * 
 */
package com.cccollector.app.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

import com.cccollector.app.model.Binary;
import com.cccollector.app.model.Release;

/**
 * 二进制文件Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class BinaryBean extends BaseEditBean<Binary> implements Serializable {

	private static final long serialVersionUID = 2991830342991151876L;

	public BinaryBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Binary>() {

			@Override
			public Binary loadModel(Integer modelId) {
				Binary binary = null;
				if (modelId == null) {
					binary = new Binary();
					binary.setType(Binary.TypeEnum.APK.ordinal());
					binary.setStatus(Binary.StatusEnum.未发布.ordinal());
					binary.setRelease(getRelease());
				} else {
					binary = binaryService.loadById(modelId);
					
					// 处理文件
					loadFile(binaryService.getDataPath() + binary.binaryFilePath());
				}
				return binary;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return releaseService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 二进制文件
	 */
	public Binary getBinary() {
		return getModel();
	}

	/**
	 * 所属发行
	 */
	public Release getRelease() {
		return (Release) relatedModel(1);
	}

	/**
	 * 改变类别
	 */
	public void changeTypeAction() {
		handleFileDelete(1);
	}

	/**
	 * 加载文件
	 */
	public void loadFile(String filePath) {
		int index = 1;
		try {
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
			String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
			fileFormatMap.put(index, fileFormat);
			filePathMap.put(index, filePath);
			FileInputStream fileInputStream = new FileInputStream(filePath);
			fileMap.put(index, new DefaultStreamedContent(fileInputStream, "application/octet-stream", fileName));
		} catch (Exception e) {
			fileFormatMap.remove(index);
			filePathMap.remove(index);
			fileMap.remove(index);
		}
	}

	/**
	 * 处理文件上传
	 */
	public void handleFileUpload(FileUploadEvent fileUploadEvent) {
		int index = 1;
		try {
			String webAppRootKey = stringFromConfig("paths.webAppRootKey");
			String webTemp = stringFromConfig("paths.webTemp");
			String tempPath = System.getProperty(webAppRootKey) + webTemp + File.separator;
			new File(tempPath).mkdirs();
			
			String fileFormat = getBinary().getTypeEnum().name().toLowerCase();
			fileFormatMap.put(index, fileFormat);
			String tempFileName = new Date().getTime() + "_" + new Random().nextInt(10000) + "." + fileFormat;
			filePathMap.put(index, tempPath + tempFileName);
			fileUploadEvent.getFile().write(filePathMap.get(index));
			FileInputStream fileInputStream = new FileInputStream(filePathMap.get(index));
			fileMap.put(index, new DefaultStreamedContent(fileInputStream, "application/octet-stream", tempFileName));
		} catch (Exception e) {
			fileFormatMap.remove(index);
			filePathMap.remove(index);
			fileMap.remove(index);
		}
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		Binary binary = getBinary();
		// 文件必填
		if (filePath(1) == null) {
			addValidatingMessage("文件为必填项");
			return;
		}
		
		if (binary.getBinaryId() == null) {
			// 添加二进制文件
			binaryService.addBinary(binary, filePath(1));
			addInfoMessageToFlash("添加二进制文件成功！");
		} else {
			// 编辑二进制文件
			binaryService.updateBinary(binary, filePath(1));
			addInfoMessageToFlash("编辑二进制文件成功！");	
		}
		
		handleNavigation("binaryList.xhtml?multiSelect=true");
	}
}
