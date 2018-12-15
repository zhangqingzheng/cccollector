/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.SortOrder;

import com.cccollector.news.model.Comment;
import com.cccollector.news.model.Report;
import com.cccollector.news.model.Reply;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 举报列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ReportListBean extends BaseListBean<Report> implements Serializable {

	private static final long serialVersionUID = -4235793896540428414L;

	public ReportListBean () {
		modelDisplayName = "举报";
		modelAttributeName = "report";
		modelIdAttributeName = "reportId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerPage<Report>() {

			@Override
			public List<Report> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				if (showNotProcessed) {
					predicateQueryFieldList.add(new QueryField("status", Report.StatusEnum.待审核.ordinal()));
				}

				// 过滤条件
				if (filters != null) {
					for (String key : filters.keySet()) {
						Object value = filters.get(key);
						switch (key) {
						case "type":
							predicateQueryFieldList.add(new QueryField("type", value));
							break;
						case "contentType":
							predicateQueryFieldList.add(new QueryField("contentType", value));
							break;
						case "status":
							predicateQueryFieldList.add(new QueryField("status", value));
							break;
						case "userApp.name":
							predicateQueryFieldList.add(new QueryField("userApp", new QueryField("user", new QueryField("nickName", value, PredicateEnum.LIKE))));
							break;
						}
					}
				}

				predicateQueryFieldList = predicateQueryFieldList.size() == 0 ? null : predicateQueryFieldList;

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("createTime", Boolean.FALSE));
				
				// 总行数
				totalRowCount.append(reportService.count(predicateQueryFieldList).toString());

				List<Report> reports = reportService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
				// 设置关联内容
				for (Report report : reports) {
					switch (report.getContentTypeEnum()) {
					case 评论:
						Comment comment = commentService.loadById(report.getContentId());
						report.setRelativeContent(comment);
						break;
					case 回复:
						Reply reply = replyService.loadById(report.getContentId());
						report.setRelativeContent(reply);
						break;
					}
				}

				return reports;
			}
		};
	}

	/**
	 * 是否显示未处理
	 */
	private boolean showNotProcessed;

	public boolean getShowNotProcessed() {
		return showNotProcessed;
	}

	public void setShowNotProcessed(boolean _showNotProcessed) {
		showNotProcessed = _showNotProcessed;
	}

	/**
	 * 类别枚举数组
	 */
	public Report.TypeEnum[] getTypeEnums() {
		return Report.TypeEnum.values();
	}

	/**
	 * 关联内容类别枚举数组
	 */
	public Report.ContentTypeEnum[] getContentTypeEnums() {
		return Report.ContentTypeEnum.values();
	}

	/**
	 * 状态枚举数组
	 */
	public Report.StatusEnum[] getStatusEnums() {
		return Report.StatusEnum.values();
	}
	
	/**
	 * 审核通过举报
	 */
	public void passReportAction(Report report) {
		// 审核通过举报
		boolean success = reportService.passReport(report);
		if (success) {
			addInfoMessage("通过举报成功！");
		} else {
			addErrorMessage("通过举报失败！");
		}
	}
	
	/**
	 * 审核未通过举报
	 */
	public void notPassReportAction(Report report) {
		// 审核未通过举报
		boolean success = reportService.notPassReport(report);
		if (success) {
			addInfoMessage("未通过举报成功！");
		} else {
			addErrorMessage("未通过举报失败！");
		}
	}
	
	/**
	 * 恢复举报
	 */
	public void recoverReportAction(Report report) {
		// 恢复举报
		boolean success = reportService.recoverReport(report);
		if (success) {
			addInfoMessage("恢复举报成功！");
		} else {
			addErrorMessage("恢复举报失败！");
		}
	}
}
