/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.templatemodel.TemplateBaseContent;
import com.cccollector.news.util.TemplateUtil;

/**
 * 测试内容模板Bean类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class TempContentHtmlBean extends BaseEditBean<String> implements Serializable {

	private static final long serialVersionUID = -3465036569831972010L;

	public TempContentHtmlBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModel<String>() {

			@Override
			public String loadModel(Integer modelId) {
				return null;
			}
		};
	}

	public String getHtml() {
		TemplateBaseContent content = templateService.loadStaticData(Integer.parseInt(getModelId1()), Integer.parseInt(getModelId2()),
				Integer.parseInt(getModelId3()), Integer.parseInt(getModelId4()), null);
		String html = TemplateUtil.content2Html(content);

		if (html != null && html.trim().length() > 0) {
			html = html.replace("<head>", "<head><base href=\"https://test.wencang.com.cn/\" />");
			return html;
		}

		return "<html>生成页面失败,请查看后台日志!</html>";
	}
}
