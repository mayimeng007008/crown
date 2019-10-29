package org.crown.common.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.crown.model.dto.ImagesDTO;
import org.crown.model.entity.Images;
import org.crown.service.IActivityService;
import org.crown.service.IImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Created by meng on 2018/5/8. 文件上传工具类
 */
@Getter
@Setter
@ConfigurationProperties
@Component
public class UploadUtil {

	/**
	 * 按照当日创建文件夹
	 */
	@Value("${lycm.isDayType}")
	private boolean isDayType;
	/**
	 * 自定义文件路径
	 */
	@Value("${lycm.uploadPath}")
	private String uploadPath;

	@Value("${lycm.imagePath}")
	private String imagePath;

	@Value("${lycm.scaleImgPath}")
	private String scaleImgPath;

	/*
	 * high-scale-size: 0.5f high-quality-size: 0.5f scale-size: 0.3f quality-size:
	 * 0.25f
	 */
	@Value("${lycm.high-scale-size}")
	private float highScale;

	@Value("${lycm.high-quality-size}")
	private float highQuality;

	@Value("${lycm.scale-size}")
	private float scaleSize;

	@Value("${lycm.quality-size}")
	private float qualitySize;

	@Value("${lycm.file-size}")
	private float fileSize;

	@Value("${lycm.img-size}")
	private float imgSize;

	@Autowired
	private IImagesService imagesService;
	@Autowired
	private IActivityService activityService;
	
	public static final String IMAGE_SUFFIX = "bmp,jpg,png,gif,jpeg";

	public UploadUtil() {
	}

	@Transactional
	public Map<String, Object> upload(MultipartFile[] multipartFiles, String uuid,int iswatermark) {
		System.out.println(iswatermark);
		if (null == multipartFiles) {
			throw new RuntimeException("上传数据/地址获取异常");
		}
		File targetFile = new File(uploadPath);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		}

		File scaleImgFolder = new File(scaleImgPath);
		if (!scaleImgFolder.exists()) {
			scaleImgFolder.mkdir();
		}

		Map<String, Object> map = new HashMap();
		ImagesDTO dto = new ImagesDTO();
		Images img = new Images();
		
		for (MultipartFile multipartFile : multipartFiles) {
			if (multipartFile.getSize() >= imgSize * 1024 * 1024) {
				throw new RuntimeException("上传图片大小不能超过10M");
			}

			String curr = multipartFile.getOriginalFilename();
			int suffixLen = curr.lastIndexOf(".");
			String suffix = curr.substring(suffixLen, curr.length());
			int index = Arrays.binarySearch(IMAGE_SUFFIX.split(","), suffix.replace(".", ""));

			String scaleImgName = UUID.randomUUID() + suffix;
			String HightImgName ="H-"+ UUID.randomUUID() + suffix;
			
			// 按比例压缩
			try {
				// 高清图下载用
				Thumbnails.of(multipartFile.getInputStream()).scale(highScale).outputQuality(highQuality)
						.toFile(uploadPath + HightImgName);
				// 缩略图显示
				if(iswatermark!=1) {
					
					Thumbnails.of(multipartFile.getInputStream()).scale(scaleSize).outputQuality(qualitySize)
					.toFile(uploadPath + scaleImgName);
				}

				img.setGalleryUuid(uuid);
				img.setImgName(curr);
				img.setImgPath(HightImgName);
				img.setScaleImgPath(scaleImgName);
				
				if (null != uuid&&iswatermark!=1) {
					imagesService.save(img);
					System.out.println("上传完毕！！！！！！！！！");
				}
				if (null != uuid&&iswatermark==1) {
					activityService.addWaterMarkImg(HightImgName,uuid);
					System.out.println("水印图片上传完毕！！！！！！！！！");
				}
				dto = img.convert(ImagesDTO.class);
				map.put("imgInfo", dto);
				
			} catch (Exception e) {
				System.out.println("210异常原因："+e.getCause());
				System.out.println("210异常消息："+e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 格式化文件名 默认采用UUID
	 *
	 * @return
	 */
	public LoadType fileNameStyle(MultipartFile multipartFile) {
		String curr = multipartFile.getOriginalFilename();
		int suffixLen = curr.lastIndexOf(".");
		boolean flag = false;
		int index = -1;
		if ("blob".equals(curr)) {
			flag = true;
			index = 0;
			curr = UUID.randomUUID() + ".png";
		} else if (suffixLen == -1) {
			throw new RuntimeException("文件获取异常");
		}
		if (!flag) {
			String suffix = curr.substring(suffixLen, curr.length());
			index = Arrays.binarySearch(IMAGE_SUFFIX.split(","), suffix.replace(".", ""));

			curr = UUID.randomUUID() + suffix;
		}
		LoadType loadType = new LoadType();
		loadType.setFileName(curr);
		// image 情况
		curr = StringUtils.isEmpty(imagePath) || index == -1 ? uploadPath + File.separator + curr
				: imagePath + File.separator + curr;
		loadType.setCurrentFile(new File(curr));
		return loadType;
	}

	private boolean isNull(MultipartFile multipartFile) {
		if (null != multipartFile) {
			return false;
		}
		return true;
	}

}

@Data
class LoadType {
	private String fileName;
	private String scaleImgUrlPath;
	private String imgUrlPath;
	private File currentFile;
}
