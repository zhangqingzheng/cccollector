/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.Template;
import com.cccollector.universal.view.GenericEditBean;

/**
 * 模板Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class TemplateSiteBean extends BaseEditBean<Template> implements Serializable {

	private static final long serialVersionUID = 6705116727637779060L;

	public TemplateSiteBean () {
		genericEditBeanHandler = new GenericEditBean.GenericEditBeanHandlerModelRelated<Template>() {

			@Override
			public Template loadModel(Integer modelId) {
				Template template = null;
				if (modelId == null) {
					template = new Template();
					template.setType(Template.TypeEnum.栏目.ordinal());
					template.setSiteVersion(getSiteVersion());
				} else {
					template = templateService.loadById(modelId);
				}
				return template;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteVersionService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 模板
	 */
	public Template getTemplate() {
		return getModel();
	}


	/**
	 * 所属站点版本
	 */
	public SiteVersion getSiteVersion() {
		return (SiteVersion) relatedModel(1);
	}
	
	/**
	 * 改变类别
	 */
	public void changeTypeAction() {
		Template template = getTemplate();
		template.setSubtype(0);
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		Template template = getTemplate();
		// 置空
		if (template.getValidateExpression().equals("")) {
			template.setValidateExpression(null);
		}
		
		if (template.getTemplateId() == null) {
			// 添加模板
			templateService.addTemplate(template);
			addInfoMessageToFlash("添加模板成功！");
		} else {
			// 编辑模板
			templateService.updateTemplate(template);
			addInfoMessageToFlash("编辑模板成功！");
		}	
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("templateList.xhtml?multiSelect=true");
		}
	}
}
