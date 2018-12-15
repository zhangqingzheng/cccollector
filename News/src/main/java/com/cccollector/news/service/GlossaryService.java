/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.Glossary;
import com.cccollector.universal.service.GenericService;

/**
 * 术语服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface GlossaryService extends GenericService<Integer, Glossary> {

	/**
	 * 获取数据目录
	 */
	public String getDataPath();

	/**
	 * 获取术语图片缩略图URL
	 */
	public String getPictureThumbnailGlossariesUrl(Glossary glossary);
	
	/**
	 * 添加术语
	 * 
	 * @param glossary 待添加的术语
	 * @param pictureFilePath 图片文件路径
	 */
	public void addGlossary(Glossary glossary, String pictureFilePath);
	
	/**
	 * 更新术语
	 * 
	 * @param glossary 待更新的术语
	 * @param pictureFilePath 图片文件路径
	 */
	public void updateGlossary(Glossary glossary, String pictureFilePath);
	
	/**
	 * 删除术语
	 * 
	 * @param glossary 待删除的术语
	 */
	public void deleteGlossary(Glossary glossary);
}
