/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ColumnDao;
import com.cccollector.news.dao.ColumnReplacementDao;
import com.cccollector.news.dao.NewsSourceDao;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.ColumnReplacement;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.service.ColumnService;
import com.cccollector.news.service.SiteVersionService;
import com.cccollector.news.templatemodel.TemplateColumn;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 栏目服务实现类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("columnService")
public class ColumnServiceImpl extends GenericServiceHibernateImpl<Integer, Column> implements ColumnService {

	@Autowired
	private NewsSourceDao newsSourceDao;
	
	@Autowired
	private ColumnReplacementDao columnReplacementDao;
	
	@Autowired
	private ColumnDao columnDao;

	@Autowired
	private SiteVersionService siteVersionService;
	
	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;

	/**
	 * Tomcat数据目录
	 */
	@Value("${paths.tomcatDataPath}")
	private String tomcatDataPath;

	/**
	 * nginx数据目录
	 */
	@Value("${paths.nginxDataPath}")
	private String nginxDataPath;

	/**
	 * 应用根路径键
	 */
	@Value("${paths.webAppRootKey}")
	private String webAppRootKey;

	/**
	 * 临时路径
	 */
	@Value("${paths.webTemp}")
	private String webTemp;

	/**
	 * 图像路径
	 */
	@Value("${paths.image}")
	private String image;

	/**
	 * 缩略图URL
	 */
	@Value("${paths.thumbnailUrl}")
	private String thumbnailUrl;
	/**
	 * 预定义栏目
	 */
	LinkedHashMap<String, Column> predefinedColumns = new LinkedHashMap<>();

	public String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}

	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}

	public String getImagePath() {
		return nginxDataPath + platformBundleId + File.separator + image + File.separator;
	}

	@Override
	public String getIconThumbnailColumnsUrl(Column column) {
		return thumbnailUrl + "w0_h100/" + column.iconFilePath();
	}

	@Override
	public void addColumn(Column column, NewsSource newsSource, String iconFilePath) {
		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
		predicateQueryFieldList.add(column.getParentColumn() == null ? new QueryField("parentColumn", null, PredicateEnum.IS_NULL)
				: new QueryField("parentColumn", new QueryField("columnId", column.getParentColumn().getColumnId())));
		Integer position = columnDao.max("position", predicateQueryFieldList);
		column.setPosition(position == null ? 0 : position.intValue() + 1);

		// 设置排序代码
		String code = String.format("%02d", column.getPosition());
		if (column.getParentColumn() != null) {
			code = column.getParentColumn().getCode() + ":" + code;
		}
		column.setCode(code);

		// 保存栏目
		columnDao.save(column);
		
		// 处理图标文件
		File iconFile = new File(getDataPath() + column.iconFilePath());
		if (iconFilePath != null) {
			File tempFile = new File(iconFilePath);
			iconFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), iconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
				column.setIconUpdateTime(new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (iconFile.exists()) {
				iconFile.delete();
			}
			column.setIconUpdateTime(null);
		}
	}

	@Override
	public void updateColumn(Column column, String iconFilePath) {
		// 获取栏目最新数据
		Column columnNew = columnDao.loadById(column.getColumnId());

		// 更新栏目
		columnNew.setName(column.getName());
		columnNew.setIntro(column.getIntro());
		columnNew.setIconRatio(column.getIconRatio());
		columnNew.setTemplateType(column.getTemplateType());
		columnNew.setThumbnailRatio(column.getThumbnailRatio());
		columnNew.setBusinessType(column.getBusinessType());
		columnNew.setBusinessIdentifiers(column.getBusinessIdentifiers());
		columnNew.setEnabled(column.getEnabled());
		columnNew.setCategoryTags(column.getCategoryTags());

		// 处理图标文件
		File iconFile = new File(getDataPath() + columnNew.iconFilePath());
		if (iconFilePath != null) {
			if (iconFilePath.startsWith(getTempPath())) {
				File tempFile = new File(iconFilePath);
				iconFile.getParentFile().mkdirs();
				try {
					Files.copy(tempFile.toPath(), iconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();
					columnNew.setIconUpdateTime(new Date());
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 清除nginx缓存文件
				File[] sizeFiles = new File(getImagePath()).listFiles();
				if (sizeFiles != null) {
					for (File sizeFile : sizeFiles) {
						File iconImageFile = new File(sizeFile, columnNew.iconFilePath());
						iconImageFile.delete();
					}
				}
			}
		} else {
			if (iconFile.exists()) {
				iconFile.delete();
			}
			columnNew.setIconUpdateTime(null);
		}
	}

	@Override
	public void deleteColumn(Column column) {

		// 删除图标文件
		File iconFile = new File(getDataPath() + column.iconFilePath());
		if (iconFile.exists()) {
			iconFile.delete();
		}

		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File iconImageFile = new File(sizeFile, column.iconFilePath());
				iconImageFile.delete();
			}
		}

		// 删除栏目
		columnDao.delete(column);
	}

	/**
	 * 获取栏目数据
	 */
	@Override
	@Cacheable(value="templateColumns")
	public List<TemplateColumn> loadAllColumns(int siteId, int siteVersionId) {
		try {
			// 预定义栏目
			List<Column> columnlist = new ArrayList<>();
			Column column1 = new Column();
			Column column2 = new Column();
			Column column3 = new Column();
			column1.setName("首页");
			column2.setName("精选阅读");
			column3.setName("文藏合作");
			columnlist.add(column1);
			columnlist.add(column2);
			columnlist.add(column3);

			List<TemplateColumn> columns = new ArrayList<TemplateColumn>();
			List<TemplateColumn> childColumns = new ArrayList<TemplateColumn>();
			List<TemplateColumn> threeLevelColumns = new ArrayList<TemplateColumn>();
			TemplateColumn columnToReturn1 = new TemplateColumn();
			// 根据站点Id获取新闻源列表
			List<NewsSource> newsSources = getNewsSources(siteId);

			// 根据站点版本Id获取栏目替代列表
			List<ColumnReplacement> columnReplacements = getColumnReplacements(siteVersionId);

			// 处理站点对应的新闻源包含的栏目与栏目替代列表
			if (newsSources.size() > 0 && columnReplacements.size() > 0) {
				// 预定义栏目
				LinkedHashMap<String, Column> predefinedColumns = refreshColumnReplacements(newsSources, columnReplacements, columnlist);
				for (String key : predefinedColumns.keySet()) {
					columnToReturn1 = new TemplateColumn();
					columnToReturn1.setColumnId(predefinedColumns.get(key).getColumnId());
					columnToReturn1.setName(predefinedColumns.get(key).getName());
					columnToReturn1.setLink(TemplateServiceImpl.calculateUrl4Column(predefinedColumns.get(key).getColumnId()));
					childColumns = new ArrayList<>();
					for (Column column : predefinedColumns.get(key).getChildColumns()) {
						TemplateColumn columnToReturn = new TemplateColumn();
						if (column.getChildColumns() != null && column.getChildColumns().size() > 0) {
							threeLevelColumns = new ArrayList<TemplateColumn>();
							for (Column threeLevelColumn : column.getChildColumns()) {
								TemplateColumn threeLevelColumnToReturn = new TemplateColumn();
								threeLevelColumnToReturn.setColumnId(threeLevelColumn.getColumnId());
								threeLevelColumnToReturn.setName(threeLevelColumn.getName());
								threeLevelColumnToReturn.setLink(TemplateServiceImpl.calculateUrl4Column(threeLevelColumn.getColumnId()));
								threeLevelColumns.add(threeLevelColumnToReturn);
							}
							columnToReturn.setChildColumns(threeLevelColumns);
						}
						columnToReturn.setColumnId(column.getColumnId());
						columnToReturn.setName(column.getName());
						columnToReturn.setLink(TemplateServiceImpl.calculateUrl4Column(column.getColumnId()));
						childColumns.add(columnToReturn);
					}
					columnToReturn1.setChildColumns(childColumns);
					columns.add(columnToReturn1);
				}
			}
			return columns;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据站点Id获取新闻源列表
	 */
	private List<NewsSource> getNewsSources(int siteId) {

		// 关联的新闻源
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("sites", new QueryField("siteId", siteId)));
		predicateQueryFieldList.add(new QueryField("sites", new QueryField("enabled", true)));
		predicateQueryFieldList.add(new QueryField("enabled", true));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("position", true));
		List<NewsSource> newsSourceList = newsSourceDao.loadAll(predicateQueryFieldList, orderQueryFieldList);

		List<NewsSource> newsSources = new ArrayList<>();
		for (NewsSource newsSource : newsSourceList) {
			List<Column> columnToRemove = new ArrayList<>();
			for (Column column : newsSource.getColumns()) {
				if (column.getEnabled()) {
					// 所属父栏目ID
					if (column.getParentColumn() != null) {
						column.setParentColumnId(column.getParentColumn().getColumnId());
					}				
				} else {
					columnToRemove.add(column);					
				}
			}
			newsSource.getColumns().removeAll(columnToRemove);
			newsSources.add(newsSource);
		}
		return newsSources;
	}

	/**
	 * 根据站点版本Id获取栏目替代列表
	 */
	private List<ColumnReplacement> getColumnReplacements(int siteVersionId) {
		// 查询栏目替代
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", siteVersionId)));
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("status", SiteVersion.StatusEnum.已发布.ordinal())));
		predicateQueryFieldList.add(new QueryField("enabled", true));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("position", true));
		List<ColumnReplacement> columnReplacements = columnReplacementDao.loadAll(predicateQueryFieldList, orderQueryFieldList);

		for (ColumnReplacement columnReplacement : columnReplacements) {
			columnReplacement.setSourceColumnId(columnReplacement.getSourceColumn().getColumnId());
			columnReplacement.setTargetColumnId(columnReplacement.getTargetColumn().getColumnId());
		}
		return columnReplacements;
	}

	/**
	 * 栏目替代刷新数据
	 */
	private LinkedHashMap<String, Column> refreshColumnReplacements(List<NewsSource> newsSources, List<ColumnReplacement> columnReplacements, List<Column> columnlists) {
		// 预定义栏目
		LinkedHashMap<String, Column> predefinedColumns = new LinkedHashMap<String, Column>();
		HashMap<Integer, NewsSource> newsSourceMap = new HashMap<>();
		HashMap<Integer, Column> columnMap = new HashMap<>();
		if (newsSources.size() != 0 && columnReplacements.size() != 0) {
			for (NewsSource newsSourceNew : newsSources) {
				// 新闻源映射
				newsSourceMap.put(newsSourceNew.getNewsSourceId(), newsSourceNew);
				for (Column column : newsSourceNew.getColumns()) {
					Column columnCopy = column;
					// 清空原有子栏目
					List<Column> childColumnList = new ArrayList<>();
					columnCopy.setChildColumns(childColumnList);
					columnCopy.setNewsSource(newsSourceNew);
					columnMap.put(columnCopy.getColumnId(), columnCopy);
					// 子栏目
					if (columnCopy.getParentColumnId() != null) {
						Column parentColumn = columnMap.get(columnCopy.getParentColumnId());
						columnCopy.setParentColumn(parentColumn);
						List<Column> childColumns = parentColumn.getChildColumns();
						ArrayList<Integer> childColumnIds = new ArrayList<>();
						if (childColumns != null && childColumns.size() != 0) {
							for (Column childColumn : childColumns) {
								childColumnIds.add(childColumn.getColumnId());
							}
							if (!childColumnIds.contains(columnCopy.getColumnId())) {
								parentColumn.getChildColumns().add(columnCopy);
							}
						} else {
							parentColumn.getChildColumns().add(columnCopy);
						}
					}
				}
			}

			for (ColumnReplacement columnReplacement : columnReplacements) {
				// 获取目标栏目Id
				int targetColumnId = columnReplacement.getTargetColumnId();
				// 获取目标栏目
				Column targeColumn = columnMap.get(targetColumnId);
				// 获取源栏目Id
				int sourceColumnId = columnReplacement.getSourceColumnId();
				// 获取源栏目
				Column column = columnMap.get(sourceColumnId);
				if (columnReplacement.getModeEnum() != ColumnReplacement.ModeEnum.引用) {
					if (targeColumn != null) {
						Integer parentColumnId = targeColumn.getParentColumnId();
						if (parentColumnId == null)
							return null;
						// 获取目标栏目的父栏目
						Column parentColumn = columnMap.get(parentColumnId);
						// 获取目标栏目的父栏目的所有子栏目
						List<Column> childColumns = parentColumn.getChildColumns();
						List<Column> childColumn = new ArrayList<>();
						for (Column c : childColumns) {
							if (targetColumnId == c.getColumnId()) {
								if (columnReplacement.getModeEnum() == ColumnReplacement.ModeEnum.替换) {
									column.setName(columnReplacement.getName());
									childColumn.add(column);
								} else if (columnReplacement.getModeEnum() == ColumnReplacement.ModeEnum.移除) {
									continue;
								} else if (columnReplacement.getModeEnum() == ColumnReplacement.ModeEnum.在之前添加) {
									column.setName(columnReplacement.getName());
									childColumn.add(column);
									childColumn.add(c);
								} else if (columnReplacement.getModeEnum() == ColumnReplacement.ModeEnum.在之后添加) {
									column.setName(columnReplacement.getName());
									childColumn.add(c);
									childColumn.add(column);
								}
							} else {
								childColumn.add(c);
							}
						}
						parentColumn.setChildColumns(childColumn);
						targeColumn.setParentColumn(parentColumn);
						columnMap.put(targeColumn.getColumnId(), targeColumn);
					}
				}
			}

			for (ColumnReplacement columnReplacement : columnReplacements) {
				if (columnReplacement.getModeEnum() == ColumnReplacement.ModeEnum.引用) {
					// 获取源栏目Id
					int sourceColumnId = columnReplacement.getSourceColumnId();
					// 获取源栏目
					Column column = columnMap.get(sourceColumnId);
					if (column != null) {
						// 预定义栏目
						for (Column tempColumn : columnlists) {
							if (columnReplacement.getName().equals(tempColumn.getName())) {
								column.setName(columnReplacement.getName());
								predefinedColumns.put(columnReplacement.getName(), column);
							}
						}
					}
				}
			}
			return predefinedColumns;
		}
		return null;
	}
}
