package org.crown.scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.crown.common.utils.BMPLoaderUtil;
import org.crown.model.dto.HeadImgDTO;
import org.crown.model.entity.HeadImg;
import org.crown.model.entity.Images;
import org.crown.service.IHeadImgService;
import org.crown.service.IImagesService;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CompareTask {
	
	/*
	 * @Value("${lycm.opencvpath}") private String opencvpath;
	 */
    
	@Autowired
	private IHeadImgService headImgService;
	
	@Autowired
	private IImagesService imgService;

	@Value("${lycm.imagePath}")
	private String imagePath;
	
	@Value("${lycm.similarRate}")
	private Integer similarRate;
	
	@Value("${lycm.frontalfaceFilePath}")
	private  String frontalfaceFilePath;
	
	/*
	 * @Value("${lycm.eyeDetectorFilePath}") private String eyeDetectorFilePath;
	 */
	// = "D:\\software\\opencv\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml";


	//@Scheduled(cron = "0 0/2 * * * ?")
	public void cutHeadImg() {
		// 开始时间
		long start = System.currentTimeMillis();
		
		try {
			List<HashMap<String, Object>> groupList = imgService.getUUIDList();
			for (HashMap<String, Object> resultMap : groupList) {
				String galleryUuid = (String) resultMap.get("gallery_uuid");// images_id
				if(null == galleryUuid) {
					continue;
				}
				System.err.println("cute img查询galleryUuid消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒"+groupList);
				List<Images> list = imgService.groupList(galleryUuid);
		        String basePicPath = imagePath;

		        int length = list.size();
		        boolean flag = true;
		        String HightImgName = null;
		        for (int j = 0; j < length; j++) {

		        	try {
		        		// 初始化人脸探测器
						CascadeClassifier faceDetector = null;
						//CascadeClassifier eyeDetector = null;
						System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
						/*
						 * if(flag) { String opencvDllName = opencvpath + Core.NATIVE_LIBRARY_NAME +
						 * ".dll"; System.load(opencvDllName); flag = false; }
						 */
						faceDetector = new CascadeClassifier(frontalfaceFilePath);
						//eyeDetector = new CascadeClassifier(eyeDetectorFilePath);
			        	Images dto = null;
			        	HeadImg hImg = null;
			        	String headImgIds = "";
			        	
			        	ArrayList<HeadImg> headList = null;
			        	dto = list.get(j);
			        	HightImgName =  dto.getImgPath();
						String imgPath = basePicPath + HightImgName;
						Mat image = Imgcodecs.imread(imgPath);
						MatOfRect face = new MatOfRect();
						
						// 3 特征匹配
						faceDetector.detectMultiScale(image, face);
						Rect[] rects = face.toArray();

		        	
						if (rects.length >= 1) {
							// 4 匹配 Rect 矩阵 数组
							int i = 1;
							headList = new ArrayList<HeadImg>();
							
							for (Rect rect : face.toArray()) {
								String hImgName = "head" + i + "_" + HightImgName;
								hImg = new HeadImg();
								hImg.setImagesId(dto.getId()+"");
								hImg.setImgName(hImgName);
								hImg.setGalleryUuid(dto.getGalleryUuid());
								imageCut(imgPath, basePicPath + hImgName, rect.x, rect.y, rect.width, rect.height);// 进行图片裁剪
								/* 截取图像路径 */
								  String headImgPath=basePicPath+hImgName; 
								  long len = new File(headImgPath).length()/1024;
								  if(len < 2){
									  hImg = null;
									  new File(headImgPath).delete(); 
								  } else {
									  Mat image2=Imgcodecs.imread(headImgPath); 
									  // 3 特征匹配 face
									  MatOfRect face2= new MatOfRect(); 
									  faceDetector.detectMultiScale(image2, face2);
									  //校正截取图像是否为正确的人脸照片 
									  if(face2.toArray().length > 0) { 
										  if(null != hImg) {
											  i++; 
											  headList.add(hImg); 
										  }
									  } else { 
										  hImg = null;
										  new File(headImgPath).delete(); 
									  }
								  }
							}

							if(null!=headList) {
								System.out.println(headList);
								headImgService.saveBatch(headList);
								for(HeadImg headImg :headList ) {
									if(null == headImg.getId() || headImg.getId().toString().equals("")) {
										continue;
									}
									headImgIds += headImg.getId().toString() + ",";
									
								}
								if(headImgIds.length() >= 1) {
									String ids = headImgIds.substring(0,headImgIds.length()-2);
									dto.setHeadImgIds(ids);
									dto.setCutheadimg(1);
								}
								imgService.updateById(dto);
							}
						}
					} catch (Exception e) {
						log.info("cute img task happend error:", e);
						continue;
					}
		        }
			}
		} catch (Exception e) {
			log.info("cute img task happend error:", e);
		}
		System.err.println("执行任务总消耗了 ：" + (System.currentTimeMillis() - start)/1000/60 + "分钟");
	}

	//@Scheduled(cron = "0 0/1 * * * ?")
	public void compareHeadImg() {
		
		try {
			// 开始时间
			long start = System.currentTimeMillis();
			List<HashMap<String, Object>> groupList = headImgService.getUUIDList();
			 System.err.println("查询galleryUuid消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒"+groupList);
			for (HashMap<String, Object> resultMap : groupList) {
				String galleryUuid = (String) resultMap.get("gallery_uuid");// images_id
				List<HeadImgDTO> list = headImgService.groupList(galleryUuid);
		        System.err.println("查询所有人物头像消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒"+list.size());
		        String basePicPath = imagePath;
		        int length = list.size();
		        int tem = 0;
		        int temp = 0;
		        
		        for (int j = 0; j < length; j++) {
		        	HeadImgDTO dto = list.get(j);
		        	String srcImgPath = basePicPath + dto.getImgName();
		        	HeadImg img = null;
		        	HeadImg img2 = null;
		        	String ids = null;
		        	if(!(new File(srcImgPath).exists()) || 
		        			(null != dto.getDeleted() && dto.getDeleted().equals("3")) ){
	        			continue;
	        		}
		        	
		        	for (int k = j+1; k < length; k++) {
		        		temp++;
		        		int n = k< length?k:length-1;
		        		HeadImgDTO dto2 = list.get(n);
		        		String destImgPath = basePicPath + dto2.getImgName();
		        		if(!(new File(destImgPath).exists()) || 
		        				(null != dto2.getDeleted() && dto2.getDeleted().equals("3")) || 
		        				dto2.getImagesId().equals(dto.getImagesId())){
		        			continue;
		        		}
		        		
		        		try {
							Integer deep = BMPLoaderUtil.compareImage(srcImgPath, destImgPath);
							if (deep >= similarRate) { 
								tem++;
								
								//System.out.println("人脸匹配相似度为" + deep);
								img = new HeadImg();
								img2 = new HeadImg();
								img.setId(dto.getId());
								System.out.println(srcImgPath+"************************"+destImgPath);
								if(null == dto.getDeleted() || dto.getDeleted().toString().equals("")) {
									System.out.println("图1" + dto);
									img.setDeleted(2);
								}
								img.setSimilarityRate(deep);
								img2.setId(dto2.getId());
								if(null == dto.getDeleted() || dto2.getDeleted().equals("")) {
									System.out.println("图2：" + dto2);
									img2.setDeleted(3);
								}
								
								img2.setSimilarityRate(deep);
								img2.setExpend(dto.getId() + "," + dto2.getId());
								ids = dto.getImagesId() + "," + dto2.getImagesId();
								img.setImagesId(ids);
								img2.setImagesId(ids);
								headImgService.updateById(img);
								headImgService.updateById(img2);
							}
							 
		        		} catch(Exception e) {
		        			log.info("zqchen's compare img task happend error:", e);
		        			continue;
		        		}
		        		
		            }
		        	//System.err.println((System.currentTimeMillis() - start)/1000/60"执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒" +tem);
		        }
		        
		        System.err.println(temp+"执行任务总消耗了 ：" + (System.currentTimeMillis() - start)/1000/60 + "分钟" +tem);
			}
		} catch (Exception e) {
			log.info("zqchen task happend error:", e);
		}

	}

	/**
	 * 裁剪人脸
	 * 
	 * @param imagePath
	 * @param outFile
	 * @param posX
	 * @param posY
	 * @param width
	 * @param height
	 */
	public boolean imageCut(String imagePath, String outFile, int posX, int posY, int width, int height) {
		// 原始图像
		Mat image = null;
		image = Imgcodecs.imread(imagePath);
		// 截取的区域：参数,坐标X,坐标Y,截图宽度,截图长度
		Rect rect = null;
		rect = new Rect(posX, posY, width, height);
		// 两句效果一样
		Mat sub = null;
		sub = image.submat(rect); // Mat sub = new Mat(image,rect);
		Mat mat = null;
		mat = new Mat();
		Size size = null;
		size = new Size(width, height);
		Imgproc.resize(sub, mat, size);// 将人脸进行截图并保存
		return Imgcodecs.imwrite(outFile, mat);
		// System.out.println(String.format("图片裁切成功，裁切后图片文件为： %s", outFile));
	}
}
