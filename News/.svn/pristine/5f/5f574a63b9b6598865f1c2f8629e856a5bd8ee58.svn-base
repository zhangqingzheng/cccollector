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
import com.cccollector.universal.view.GenericListBean;

/**
 * 样式列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class StyleListBean extends BaseListBean<Style> implements Serializable {

	private static final long serialVersionUID = -7038623221173082918L;

	public StyleListBean () {
		modelDisplayName = "样式";
		modelAttributeName = "style";
		modelIdAttributeName = "styleId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Style>() {

			@Override
			public List<Style> loadAllModelList() {
				if (getNewsSource() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("templateType", true));
				return styleService.loadAll(predicateQueryFieldList, orderQueryFieldList);
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
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}
	
	/**
	 * 加载样式CSS文件链接
	 */
	public String loadCssLink(Style style) {
		return styleService.getCssPublishStylesUrl(style);
	}
	
	/**
	 * 删除
	 */
	public void deleteStyleAction(Style style) {
		// 删除CSS文件
		styleService.deleteStyleCss(style);

		// 删除样式
		styleService.delete(style);
		addInfoMessage("删除样式成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除样式
	 */
	public void deleteStylesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个样式进行删除！");
			return;
		}

		for (Style style : getSelectedModels()) {
			// 删除CSS文件
			styleService.deleteStyleCss(style);
		}
		
		// 删除样式
		styleService.deleteAll(getSelectedModels());
		addInfoMessage("删除样式成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
