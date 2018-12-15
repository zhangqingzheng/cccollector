/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.StyleDao;
import com.cccollector.news.model.Style;
import com.cccollector.news.service.StyleService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 样式服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("styleService")
public class StyleServiceImpl extends GenericServiceHibernateImpl<Integer, Style> implements StyleService {
	
	@SuppressWarnings("unused")
	@Autowired
	private StyleDao styleDao;
	
	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;
	
	/**
	 * nginx数据目录
	 */
	@Value("${paths.nginxDataPath}")
	private String nginxDataPath;
	
	/**
	 * 发布路径
	 */
	@Value("${paths.publish}")
	private String publish;
	
	/**
	 * 样式路径
	 */
	@Value("${paths.styles}")
	private String styles;
	
	/**
	 * 发布URL
	 */
	@Value("${paths.publishUrl}")
	private String publishUrl;

	@Override
	public String getPublishPath() {
		return nginxDataPath + platformBundleId + File.separator + publish + File.separator;
	}

	@Override
	public String getCssPublishStylesUrl(Style style) {
		return publishUrl + style.cssUrlPath();
	}

	@Override
	public void updateStyleCss(Style style) {
		try {
			byte[] styleSheetBytes = style.getStyleSheet().getBytes("UTF-8");
			File cssPublishFile = new File(getPublishPath() + style.cssFilePath());
			cssPublishFile.getParentFile().mkdirs();
			Files.write(cssPublishFile.toPath(), styleSheetBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteStyleCss(Style style) {
		File cssPublishFile = new File(getPublishPath() + style.cssFilePath());
		cssPublishFile.delete();
	}
}
