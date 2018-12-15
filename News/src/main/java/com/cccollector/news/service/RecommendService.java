/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.Recommend;
import com.cccollector.universal.service.GenericService;

/**
 * 推荐服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface RecommendService extends GenericService<Integer, Recommend> {
	
	/**
	 * 获取ImageMagick命令路径
	 */
	public String getImageMagickPath();

	/**
	 * 获取临时目录
	 */
	public String getTempPath();
	
	/**
	 * 获取数据目录
	 */
	public String getDataPath();

	/**
	 * 获取推荐图片缩略图URL
	 */
	public String getPictureThumbnailRecommendsUrl(Recommend recommend);
    
	/**
	 * 根据搜索词加载关联内容列表
	 * 
	 * @param recommend 推荐
	 * @param searchWord 搜索词
	 * @return 关联内容列表列表
	 */
    public List<?> loadContentsBySearchWord(Recommend recommend, String searchWord);
	
	/**
	 * 添加推荐
	 * 
	 * @param recommend 待添加的推荐
	 * @param pictureFilePath 图片文件路径
	 */
	public void addRecommend(Recommend recommend, String pictureFilePath);
	
	/**
	 * 移动推荐
	 * 
	 * @param recommends 移动后的推荐
	 */
	public void moveRecommends(List<Recommend> recommends);
	
	/**
	 * 更新推荐
	 * 
	 * @param recommend 待更新的推荐
	 * @param pictureFilePath 图片文件路径
	 */
	public void updateRecommend(Recommend recommend, String pictureFilePath);
	
	/**
	 * 删除推荐
	 * 
	 * @param recommend 待删除的推荐
	 */
	public void deleteRecommend(Recommend recommend);
}
