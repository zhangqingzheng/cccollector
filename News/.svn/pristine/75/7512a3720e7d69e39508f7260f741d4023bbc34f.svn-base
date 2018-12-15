/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.MediaDao;
import com.cccollector.news.model.Media;
import com.cccollector.news.service.MediaService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.cccollector.universal.view.BaseBean.SimpleImageInfo;

/**
 * 多媒体服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("mediaService")
public class MediaServiceImpl extends GenericServiceHibernateImpl<Integer, Media> implements MediaService {
	
	@Autowired
	private MediaDao mediaDao;
	
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
	 * 多媒体路径
	 */
	@Value("${paths.media}")
	private String media;
	
	/**
	 * 缩略图URL
	 */
	@Value("${paths.thumbnailUrl}")
	private String thumbnailUrl;
	
	/**
	 * 多媒体URL
	 */
	@Value("${paths.mediaUrl}")
	private String mediaUrl;
	
	/**
	 * 音频视频详情命令
	 */
	@Value("${command.audioVideoDetail}")
	private String audioVideoDetail;
	
	/**
	 * 转换至Wav命令
	 */
	@Value("${command.convertToWav}")
	private String convertToWav;
	
	/**
	 * 转换至M4a命令
	 */
	@Value("${command.convertToM4a}")
	private String convertToM4a;
	
	private String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}

	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}

	private String getImagePath() {
		return nginxDataPath + platformBundleId + File.separator + image + File.separator;
	}

	private String getMediaPath() {
		return nginxDataPath + platformBundleId + File.separator + media + File.separator;
	}

	@Override
	public String getMediaPictureThumbnailUrl(Media media) {
		return thumbnailUrl + "w0_h0/" + media.pictureUrlPath();
	}

	@Override
	public String getMediaFilePreviewUrl(Media media) {
		return mediaUrl + "h0/" + media.mediaUrlPath();
	}

	@Override
	public void addMedia(Media media, String pictureFilePath, String mediaFilePath) {
		// 设置存储路径
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(new Date().toString().getBytes());
			String prefix = new BigInteger(1, messageDigest.digest()).toString(16);
			media.setPath(prefix.substring(0, 2) + "/" + prefix.substring(2, 4) + "/" );
		} catch (NoSuchAlgorithmException e) {}

		// 设置存储后缀
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(new Date().toString().getBytes());
			String suffix = new BigInteger(1, messageDigest.digest()).toString(16);
			media.setSuffix(suffix.substring(suffix.length() - 6, suffix.length()));
		} catch (NoSuchAlgorithmException e) {}

		// 设置图片宽高
		if (pictureFilePath != null) {
			try {
				SimpleImageInfo simpleImageInfo = new SimpleImageInfo(new File(pictureFilePath));
				media.setWidth(simpleImageInfo.getWidth());
				media.setHeight(simpleImageInfo.getHeight());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			media.setWidth(0);
			media.setHeight(0);
		}
		
		// 设置音视频时长
		media.setDuration(0);

		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", media.getArticle().getArticleId())));
		Integer position = mediaDao.max("position", predicateQueryFieldList);
		media.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 保存多媒体
		mediaDao.save(media);

		// 处理图片文件
		if (pictureFilePath != null) {
			File tempFile = new File(pictureFilePath);
			File pictureFile = new File(getDataPath() + media.pictureFilePath());	
			pictureFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), pictureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 处理多媒体文件
		if (mediaFilePath != null) {
			File tempFile = new File(mediaFilePath);
			File mediaFile = new File(getDataPath() + media.mediaFilePath());	
			mediaFile.getParentFile().mkdirs();
			if (media.getTypeEnum() == Media.TypeEnum.音频 && mediaFilePath.endsWith("mp3")) {
				File wavFile = new File(tempFile.getAbsolutePath().replace(".mp3", ".wav"));	
				String command = convertToWav.replace("@{runtime.file.wavFilePath}", wavFile.getAbsolutePath()).replace("@{runtime.file.sourceFilePath}", tempFile.getAbsolutePath());
				convertMp3ToWav(command);
				File m4aFile = new File(tempFile.getAbsolutePath().replace(".mp3", ".m4a"));	
				command = convertToM4a.replace("@{runtime.file.targetFilePath}", m4aFile.getAbsolutePath()).replace("@{runtime.file.wavFilePath}", wavFile.getAbsolutePath());
				convertWavToM4a(command);
				try {
					Files.copy(m4aFile.toPath(), mediaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();
					wavFile.delete();
					m4aFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					Files.copy(tempFile.toPath(), mediaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
			// 设置音视频时长
			String command = audioVideoDetail.replace("@{runtime.file.filePath}", mediaFile.getAbsolutePath()).replace("@{runtime.file.fileAttribute}", "ID_LENGTH");
			Integer audioDuration = getAudioOrVideoDuration(command);
			media.setDuration(audioDuration);
		}
	}

	@Override
	public void moveMedias(List<Media> medias) {
		int position = 0;
		for (Media media : medias) {
			media.setPosition(position);
			mediaDao.update(media, "position");
			position++;	
		}
	}

	@Override
	public void updateMedia(Media media, String pictureFilePath, String mediaFilePath) {
		// 获取多媒体最新数据
		Media mediaNew = mediaDao.loadById(media.getMediaId());
		
		// 更新多媒体
		mediaNew.setCaption(media.getCaption());
		
		// 处理图片文件
		if (pictureFilePath != null && pictureFilePath.startsWith(getTempPath())) {
			File tempFile = new File(pictureFilePath);
			File pictureFile = new File(getDataPath() + mediaNew.pictureFilePath());	
			pictureFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), pictureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
				
				// 设置图片宽高
				SimpleImageInfo simpleImageInfo = new SimpleImageInfo(pictureFile);
				mediaNew.setWidth(simpleImageInfo.getWidth());
				mediaNew.setHeight(simpleImageInfo.getHeight());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 清除nginx缓存文件
			File[] sizeFiles = new File(getImagePath()).listFiles();
			if (sizeFiles != null) {
				for (File sizeFile : sizeFiles) {
					File pictureImageFile = new File(sizeFile, mediaNew.pictureFilePath());
					pictureImageFile.delete();
				}
			}
		}
		
		// 处理多媒体文件
		if (mediaFilePath != null && mediaFilePath.startsWith(getTempPath())) {
			File tempFile = new File(mediaFilePath);
			File mediaFile = new File(getDataPath() + mediaNew.mediaFilePath());	
			mediaFile.getParentFile().mkdirs();
			if (mediaNew.getTypeEnum() == Media.TypeEnum.音频 && mediaFilePath.endsWith("mp3")) {
				File wavFile = new File(tempFile.getAbsolutePath().replace(".mp3", ".wav"));	
				String command = convertToWav.replace("@{runtime.file.wavFilePath}", wavFile.getAbsolutePath()).replace("@{runtime.file.sourceFilePath}", tempFile.getAbsolutePath());
				convertMp3ToWav(command);
				File m4aFile = new File(tempFile.getAbsolutePath().replace(".mp3", ".m4a"));	
				command = convertToM4a.replace("@{runtime.file.targetFilePath}", m4aFile.getAbsolutePath()).replace("@{runtime.file.wavFilePath}", wavFile.getAbsolutePath());
				convertWavToM4a(command);
				try {
					Files.copy(m4aFile.toPath(), mediaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();
					wavFile.delete();
					m4aFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					Files.copy(tempFile.toPath(), mediaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
			// 设置音视频时长
			String command = audioVideoDetail.replace("@{runtime.file.filePath}", mediaFile.getAbsolutePath()).replace("@{runtime.file.fileAttribute}", "ID_LENGTH");
			Integer audioDuration = getAudioOrVideoDuration(command);
			mediaNew.setDuration(audioDuration);
			
			// 清除nginx缓存文件
			File[] sizeFiles = new File(getMediaPath()).listFiles();
			if (sizeFiles != null) {
				for (File sizeFile : sizeFiles) {
					File fileMediaFile = new File(sizeFile, mediaNew.mediaFilePath());
					fileMediaFile.delete();
				}
			}
		}
	}

	@Override
	public void deleteMedia(Media media) {
		// 删除图片文件
		File pictureFile = new File(getDataPath() + media.pictureFilePath());
		if (pictureFile.exists()) {
			pictureFile.delete();
		}
		
		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File pictureImageFile = new File(sizeFile, media.pictureFilePath());
				pictureImageFile.delete();
			}
		}
		
		// 删除多媒体文件
		File mediaFile = new File(getDataPath() + media.mediaFilePath());
		if (mediaFile.exists()) {
			mediaFile.delete();
		}
		
		// 清除nginx缓存文件
		sizeFiles = new File(getMediaPath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File fileMediaFile = new File(sizeFile, media.mediaFilePath());
				fileMediaFile.delete();
			}
		}
		
		// 删除多媒体
		mediaDao.delete(media);
	}
	
	/**
	 * 处理音频文件mp3转换至wav
	 *
	 * @param convertToWavCommand 转换至Wav
	 * @return 
	 */
	public static void convertMp3ToWav(String convertToWavCommand) {
		try {
			Process process = null;
			if (File.separator.equals("/")) {
				process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", convertToWavCommand });
			} else {
				process = Runtime.getRuntime().exec("cmd /c C: " + convertToWavCommand);
			}
			getProcessReturns(process);
			process.waitFor();
		} catch (Exception e) {}
	}
	
	/**
	 * 处理音频文件wav转换至m4a
	 *
	 * @param convertToM4aCommand 转换至M4a
	 * @return 
	 */
	public static void convertWavToM4a(String convertToM4aCommand) {
		try {
			Process process = null;
			if (File.separator.equals("/")) {
				process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", convertToM4aCommand });
			} else {
				process = Runtime.getRuntime().exec("cmd /c C: " + convertToM4aCommand);
			}
			getProcessReturns(process);
			process.waitFor();
		} catch (Exception e) {}
	}
	
	/**
	 * 获取音频文件比特率
	 * 
	 * @return 比特率
	 */
	public static Integer getAudioBitrate(String command) {
		Integer audioBitrate = null;
		try {
			Process process = null;
			if (File.separator.equals("/")) {
				process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command});
			} else {
				process = Runtime.getRuntime().exec("cmd /c C: " + command);
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String duration = bufferedReader.readLine();
			String[] durationSplits = duration.split("=");
			long durationLong = Long.parseLong(durationSplits[1]);
			int mp3Rate = (int) (durationLong / 1000);
			audioBitrate = mp3Rate >= 128 ? 128 : 64;
		} catch (Exception e) {}
		return audioBitrate;
	}
	
	/**
	 * 获取音视频文件时长
	 * 
	 * @return 时长
	 */
	public static Integer getAudioOrVideoDuration(String command) {
		Integer durationToReturn = null;
		try {
			Process process = null;
			if (File.separator.equals("/")) {
				process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
			} else {
				process = Runtime.getRuntime().exec("cmd /c C: " + command);
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String duration = bufferedReader.readLine();
			String[] durationSplits = duration.split("=");
			durationToReturn = (int)Math.round(Double.parseDouble(durationSplits[1]));
		} catch (Exception e) {}
		return durationToReturn;
	}
	
	/**
	 * 获取进程执行后的返回值以及输出
	 * 
	 * @param process 进程
	 * @return 返回值数组
	 */
	public static String[] getProcessReturns(Process process) {
		String[] returns = null;
		try {
			returns = new String[3];
			Future<String> outputFuture = getOutput(process.getInputStream());
			Future<String> errorFuture = getOutput(process.getErrorStream());
			Integer returnValue = process.waitFor();
			returns[0] = returnValue.toString();
			returns[1] = outputFuture.get();
			returns[2] = errorFuture.get();
		} catch (Exception e) {}
		return returns;
	}
	
	/**
	 * 获取输出
	 * 
	 * @param outputStream 输出流
	 * @return 输出字符
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static Future<String> getOutput(final InputStream outputStream) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Callable<String> callable = new Callable<String>() {
			public String call() throws IOException {
				StringBuilder outputStringBuilder = new StringBuilder();
				BufferedReader outputBufferedReader = new BufferedReader(new InputStreamReader(outputStream));
				String line = null;
				while ((line = outputBufferedReader.readLine()) != null) {
					outputStringBuilder.append(line + "\n");
				}
				return outputStringBuilder.toString();
			}
		};
		return executorService.submit(callable);
	}
}