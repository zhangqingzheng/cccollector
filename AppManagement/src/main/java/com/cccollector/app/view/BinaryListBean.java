/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.app.model.Binary;
import com.cccollector.app.model.Release;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 二进制文件列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class BinaryListBean extends BaseListBean<Binary> implements Serializable {

	private static final long serialVersionUID = 2714610331381553757L;

	public BinaryListBean() {
		modelDisplayName = "二进制文件";
		modelAttributeName = "binary";
		modelIdAttributeName = "binaryId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Binary>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return releaseService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Binary> loadAllModelList() {
				if (getRelease() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("release", new QueryField("releaseId", getRelease().getReleaseId())));
				return binaryService.loadAll(predicateQueryFieldList, null);
			}
		};		
	}

	/**
	 * 所属发行
	 */
	public Release getRelease() {
		return (Release) relatedModel(1);
	}
	
	/**
	 * 发布二进制文件
	 */
	public void publishBinaryAction(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.未发布) {
			return;
		}
		binaryService.publishBinary(binary);
		addInfoMessage("发布二进制文件成功！");
		
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 撤销发布二进制文件
	 */
	public void cancelPublishBinaryAction(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.已发布) {
			return;
		}
		// 撤销发布二进制文件
		binaryService.cancelPublishBinary(binary);
		addInfoMessage("撤销发布二进制文件成功！");
		
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 废弃
	 */
	public void discardBinaryAction(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.已发布) {
			return;
		}
		// 废弃二进制文件
		binaryService.discardBinary(binary);
		addInfoMessage("废弃二进制文件成功！");
		
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 撤销废弃
	 */
	public void cancelDiscardBinaryAction(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.已废弃) {
			return;
		}
		// 设置状态
		binary.setStatus(Binary.StatusEnum.未发布.ordinal());
		binaryService.update(binary, "status");
		addInfoMessage("撤销废弃二进制文件成功！");
		
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除二进制文件
	 */
	public void deleteBinaryAction(Binary binary) {
		if (binary.getStatusEnum() != Binary.StatusEnum.未发布) {
			return;
		}
		// 删除二进制文件
		binaryService.deleteBinary(binary);
		addInfoMessage("删除二进制文件成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
