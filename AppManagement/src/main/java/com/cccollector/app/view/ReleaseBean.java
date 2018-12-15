/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.Edition;
import com.cccollector.app.model.Release;

/**
 * 发行Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ReleaseBean extends BaseEditBean<Release> implements Serializable {

	private static final long serialVersionUID = 1337219921264750827L;

	public ReleaseBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Release>() {

			@Override
			public Release loadModel(Integer modelId) {
				Release release = null;
				if (modelId == null) {
					release = new Release();
					release.setStatus(Release.StatusEnum.未发布.ordinal());
					release.setEdition(getEdition());
				} else {
					release = releaseService.loadById(modelId);
				}
				return release;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return editionService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 发行
	 */
	public Release getRelease() {
		return getModel();
	}

	/**
	 * 所属版本
	 */
	public Edition getEdition() {
		return (Edition) relatedModel(1);
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		Release release = getRelease();
		if (release.getReleaseId() == null) {
			// 设置密钥
			release.setSecretKey(randomString(32));
			
			// 添加发行
			releaseService.save(release);
			addInfoMessageToFlash("添加发行成功！");
		} else {
			// 编辑发行
			releaseService.update(release);
			addInfoMessageToFlash("编辑发行成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("releaseList.xhtml?multiSelect=true");
		}
	}

	/**
	 * 随机生成包含数字、字母大小写的字符串
	 */
	private String randomString(int length) {
        String value = "";  
        Random random = new Random();  
           
        for (int i = 0; i < length; i++) {  
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            // 输出字母还是数字  
            if ("char".equalsIgnoreCase(charOrNum)) {  
                // 输出大写字母或小写字母 
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                value += (char) (random.nextInt(26) + temp);  
            } else if ("num".equalsIgnoreCase(charOrNum)) {  
                value += String.valueOf(random.nextInt(10));  
            }  
        }  
        return value;
	}
}
