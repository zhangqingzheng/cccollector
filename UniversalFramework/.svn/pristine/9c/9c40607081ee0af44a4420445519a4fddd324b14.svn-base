/**
 * 
 */
package com.cccollector.universal.util;

import java.io.File;
import java.io.FileInputStream;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * 图像工具类
 *
 * @author 谢朋
 * Copyright © 2016年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class ImageUtils {

	/**
	 * 等比缩放图像
	 * 
	 * @param sourceFilePath 源图片文件路径
	 * @param scaleFilePath 缩放图片文件路径
	 * @param imageMagickPath ImageMagick路径
	 * @param maxWidth 最大宽度
	 * @param maxHeight 最大高度
	 * @return 是否成功
	 */
	public static boolean ratioImage(String sourceFilePath, String scaleFilePath, String imageMagickPath, Integer maxWidth, Integer maxHeight) {
		return scaleImage(sourceFilePath, scaleFilePath, imageMagickPath, maxWidth, maxHeight, false, false);
	}
	
	/**
	 * 裁切图像
	 * 
	 * @param sourceFilePath 源图片文件路径
	 * @param scaleFilePath 裁切图片文件路径
	 * @param imageMagickPath ImageMagick路径
	 * @param cropWidth 裁切宽度
	 * @param cropHeight 裁切高度
	 * @return 是否成功
	 */
	public static boolean cropImage(String sourceFilePath, String scaleFilePath, String imageMagickPath, Integer cropWidth, Integer cropHeight) {
		return scaleImage(sourceFilePath, scaleFilePath, imageMagickPath, cropWidth, cropHeight, true, false);
	}
	
	/**
	 * 补白图像
	 * 
	 * @param sourceFilePath 源图片文件路径
	 * @param scaleFilePath 缩放图片文件路径
	 * @param imageMagickPath ImageMagick路径
	 * @param width 宽度
	 * @param height 高度
	 * @return 是否成功
	 */
	public static boolean fillWhiteImage(String sourceFilePath, String scaleFilePath, String imageMagickPath, Integer width, Integer height) {
		return scaleImage(sourceFilePath, scaleFilePath, imageMagickPath, width, height, false, true);
	}
	
	/**
	 * 缩放图像
	 * 
	 * @param sourceFilePath 源图片文件路径
	 * @param scaleFilePath 缩放图片文件路径
	 * @param imageMagickPath ImageMagick路径
	 * @param scaleWidth 缩放宽度
	 * @param scaleHeight 缩放高度
	 * @param crop 是否裁切
	 * @param fillWhite 是否填充白色
	 * @return 是否成功
	 */
	public static boolean scaleImage(String sourceFilePath, String scaleFilePath, String imageMagickPath, Integer scaleWidth, Integer scaleHeight, boolean crop, boolean fillWhite) {
		try {
			File scaleFile = new File(scaleFilePath);
			scaleFile.getParentFile().mkdirs();
			scaleFile.delete();
			
			IMOperation operation = new IMOperation();
			operation.addImage();
			if (crop) {
				operation.resize(scaleWidth, scaleHeight, '^').gravity("center").extent(scaleWidth, scaleHeight);
			} else {
				operation.resize(scaleWidth, scaleHeight);
			}
			if (fillWhite) {
				operation.background("white").gravity("center").extent(scaleWidth, scaleHeight);
			}
			operation.quality(80.0);
			operation.addImage();
			
			if (imageMagickPath != null && !imageMagickPath.equals("")) {
				ProcessStarter.setGlobalSearchPath(imageMagickPath);
			}
	        ConvertCmd convert = new ConvertCmd();
	        convert.run(operation, sourceFilePath, scaleFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 加载补白图像
	 * 
	 * @param sourceFilePath 源图片文件路径
	 * @param scaleFilePath 缩放图片文件路径
	 * @param imageMagickPath ImageMagick路径
	 * @param width 宽度
	 * @param height 高度
	 * @return scaleFilePath图像StreamedContent
	 */
	public static StreamedContent loadFillWhiteImage(String sourceFilePath, String scaleFilePath, String imageMagickPath, Integer width, Integer height) {
		return loadScaleImage(sourceFilePath, scaleFilePath, imageMagickPath, width, height, false, true);
	}

	/**
	 * 加载缩放图像
	 * 
	 * @param sourceFilePath 源图片文件路径
	 * @param scaleFilePath 缩放图片文件路径
	 * @param imageMagickPath ImageMagick路径
	 * @param scaleWidth 缩放宽度
	 * @param scaleHeight 缩放高度
	 * @param crop 是否裁切
	 * @param fillWhite 是否填充白色
	 * @return 缩放图像StreamedContent
	 */
	public static StreamedContent loadScaleImage(String sourceFilePath, String scaleFilePath, String imageMagickPath, Integer scaleWidth, Integer scaleHeight, boolean crop, boolean fillWhite) {
		File scaleFile = new File(scaleFilePath);
		if (!scaleFile.exists() && !scaleImage(sourceFilePath, scaleFilePath, imageMagickPath, scaleWidth, scaleHeight, crop, fillWhite)) {
			return null;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(scaleFile);
			return new DefaultStreamedContent(fileInputStream, "image/jpeg");
		} catch (Exception e) {}
		return null;
	}
	
	/**
	 * DPI缩放图像
	 * 
	 * @param sourceFilePath 源图片文件路径
	 * @param scaleFilePath 缩放图片文件路径
	 * @param imageMagickPath ImageMagick路径
	 * @param density 缩放DPI
	 * @return 是否成功
	 */
	public static boolean densityImage(String sourceFilePath, String scaleFilePath, String imageMagickPath, int density) {
		try {
			File scaleFile = new File(scaleFilePath);
			scaleFile.getParentFile().mkdirs();
			scaleFile.delete();
			
			IMOperation operation = new IMOperation();
			operation.density(density);
			operation.addImage();
			operation.quality(80.0);
			operation.addImage();
			
			if (imageMagickPath != null && !imageMagickPath.equals("")) {
				ProcessStarter.setGlobalSearchPath(imageMagickPath);
			}
	        ConvertCmd convert = new ConvertCmd();
	        convert.run(operation, sourceFilePath, scaleFilePath);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
