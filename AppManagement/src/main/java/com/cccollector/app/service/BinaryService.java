/**
 * 
 */
package com.cccollector.app.service;

import com.cccollector.app.model.Binary;
import com.cccollector.universal.service.GenericService;

/**
 * 二进制文件服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface BinaryService extends GenericService<Integer, Binary> {
	
	/**
	 * 获取数据目录
	 */
	public String getDataPath();
	
	/**
	 * 添加二进制文件
	 * 
	 * @param binary 待添加的二进制文件
	 * @param filePath 文件路径
	 */
	public void addBinary(Binary binary, String filePath);
	
	/**
	 * 更新二进制文件
	 * 
	 * @param binary 待更新的二进制文件
	 * @param filePath 文件路径
	 */
	public void updateBinary(Binary binary, String filePath);
	
	/**
	 * 删除二进制文件
	 * 
	 * @param binary 待删除的二进制文件
	 */
	public void deleteBinary(Binary binary);
	
	/**
	 * 发布二进制文件
	 * 
	 * @param binary 待发布的二进制文件
	 */
	public void publishBinary(Binary binary);
	
	/**
	 * 撤销发布二进制文件
	 * 
	 * @param binary 待撤销的二进制文件
	 */
	public void cancelPublishBinary(Binary binary);
	
	/**
	 * 废弃二进制文件
	 * 
	 * @param binary 待废弃的二进制文件
	 */
	public void discardBinary(Binary binary);
	
}
