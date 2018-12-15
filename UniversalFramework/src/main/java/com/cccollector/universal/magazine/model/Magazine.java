/**
 * 
 */
package com.cccollector.universal.magazine.model;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 杂志类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
public class Magazine implements Serializable {
	
	private static final long serialVersionUID = -2270603766181499902L;
	
	private Integer _magazineId;

	/**
	 * 杂志ID
	 */
	public Integer getMagazineId() {
		return _magazineId;
	}

	public void setMagazineId(Integer magazineId) {
		_magazineId = magazineId;
	}

	/**
	 * 杂志URL路径
	 */
	public String getMagazineUrlPath() {
		return "magazines/" + _magazineId + "/";
	}

	/**
	 * 杂志文件路径
	 */
	public String getMagazineFilePath() {
		return "magazines" + File.separator + _magazineId + File.separator;
	}
	
	private String _name;

	/**
	 * 名称
	 */
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private String _magazineIdentifier;
	
	/**
	 * 杂志标识符
	 */
	public String getMagazineIdentifier() {
		return _magazineIdentifier;
	}

	public void setMagazineIdentifier(String magazineIdentifier) {
		_magazineIdentifier = magazineIdentifier;
	}
	
	private Integer _language;

	/**
	 * 语言
	 */
	public Integer getLanguage() {
		return _language;
	}

	public void setLanguage(Integer language) {
		_language = language;
	}

	/**
	 * 语言枚举
	 */
	public static enum LanguageEnum {
		简体中文,
		繁体中文,
		英文,
		法文,
		德文,
		日文,
		韩文,
		西班牙文
	}

	/**
	 * 语言的枚举
	 */
	public LanguageEnum getLanguageEnum() {
		return LanguageEnum.values()[_language];
	}

	private Map<Integer, Map<Integer, Map<Integer, Format>>> _formats = new HashMap<Integer, Map<Integer, Map<Integer, Format>>>();
	
	/**
	 * 包含的版式
	 */
	public Map<Integer, Map<Integer, Map<Integer, Format>>> getFormats() {
		return _formats;
	}

	public void setFormats(Map<Integer, Map<Integer, Map<Integer, Format>>> formats) {
		_formats = formats;
	}
}
