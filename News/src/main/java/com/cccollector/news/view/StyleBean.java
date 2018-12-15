/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Style;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericEditBean;

/**
 * 样式Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class StyleBean extends BaseEditBean<Style> implements Serializable {

	private static final long serialVersionUID = 8319981893777423243L;

	public StyleBean () {
		genericEditBeanHandler = new GenericEditBean.GenericEditBeanHandlerModelRelated<Style>() {

			@Override
			public Style loadModel(Integer modelId) {
				Style style = null;
				if (modelId == null) {
					style = new Style();
					style.setNewsSource(getNewsSource());
				} else {
					style = styleService.loadById(modelId);
				}
				return style;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 样式
	 */
	public Style getStyle() {
		return getModel();
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		Style style = getStyle();
		if (style.getStyleId() == null) {
			// 如果已存在相同模版类别的样式，则返回
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", style.getNewsSource().getNewsSourceId())));
			predicateQueryFieldList.add(new QueryField("templateType", style.getTemplateType()));
			long count = styleService.count(predicateQueryFieldList);
			if (count > 0) {
				addValidatingMessage("已存在相同模版类别的样式！");
				return;
			}

			// 添加样式
			styleService.save(style);
			addInfoMessageToFlash("添加样式成功！");
		} else {
			// 编辑样式
			styleService.update(style);
			addInfoMessageToFlash("编辑样式成功！");
		}
		
		// 更新样式CSS文件
		styleService.updateStyleCss(style);

		handleNavigation("styleList.xhtml?multiSelect=true");
	}
}
