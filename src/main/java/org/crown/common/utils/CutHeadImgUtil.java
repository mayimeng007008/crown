package org.crown.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.crown.model.dto.ImagesDTO;
import org.crown.model.entity.HeadImg;
import org.crown.model.entity.Images;
import org.crown.service.IHeadImgService;
import org.crown.service.IImagesService;
import org.crown.service.impl.ImagesServiceImpl;
import org.json.JSONObject;
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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.aip.face.AipFace;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mayimeng on . 文件上传工具类
 */


public class CutHeadImgUtil {
	@Autowired
	IHeadImgService headImgService;

	@Autowired 
	IImagesService imgService;
	
	@Value("${lycm.frontalfaceFilePath}")
	private  String frontalfaceFilePath;

	@Value("${lycm.imagePath}")
	private String imagePath;

		public  void cutHeadImg(String uuid) throws Exception {
			//初始化
			AipFace client =new AipFace("17576603", "bxYz7lba8CIIuarmIHmCZVOU", "WaZ3KRISDFH4YkpawxRd85Eto43QdGYa");
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
			CascadeClassifier faceDetector = new CascadeClassifier(frontalfaceFilePath);
			System.out.println("开始截图！");
				
			long start = System.currentTimeMillis();
			List<Images> imgs = imgService.getImgs(uuid);
			System.out.println("照片个数："+imgs.size());
			MatOfRect face=new MatOfRect();;
			List<HeadImg> headList=null;
			Mat image=null;
			//循环检测人头
			for (Images img : imgs) {
				headList=new ArrayList<HeadImg>();
				String imgPath = imagePath + img.getImgPath();
				try {
					
					image = Imgcodecs.imread(imgPath);
				} catch (Exception e) {
					System.out.println("找不到图片");
					continue;
				}
				faceDetector.detectMultiScale(image, face);
				Rect[] rects = face.toArray();
				System.out.println("一共有人脸："+rects.length);
				//检测出头像
				if(rects.length>0) {
					int i = 1;
					String headImgIds="";
					HeadImg	hImg=null;
					//循环截图
					for (Rect headImg : rects) {
						hImg = new HeadImg();
						String hImgName = "head" + i + "_" + img.getImgPath();
						hImg.setImagesId(img.getId()+"");
						hImg.setImgFromId(img.getId());
						hImg.setImgName(hImgName);
						hImg.setGalleryUuid(img.getGalleryUuid());
						imageCut(imgPath, imagePath + hImgName, headImg.x, headImg.y, headImg.width, headImg.height);// 进行图片裁剪
						System.out.println("截图成功！");
						String headImgPath=imagePath+hImgName; 
						long len = new File(headImgPath).length()/1024;
					
					  if(len < 2){ 
						  new File(headImgPath).delete();
					  System.out.println("这个头像太小,为："+len+"kb，已经删除");
					  }else{
						  
						  //opencv校验人脸
						  image = Imgcodecs.imread(headImgPath);
						  faceDetector.detectMultiScale(image, face);
						  rects = face.toArray();
						  System.out.println("opencv校验脸数组长度："+rects.length);
						  
						  //百度校验人脸
						  Thread.currentThread().sleep(300);
						  String result2=sample(client,headImgPath);
						  JSONObject dataJson2= new JSONObject(result2);//把字符串转成json数据
						  if (dataJson2.has("result")) {
							  result2 = (String) dataJson2.get("result").toString();//获取到json数据中result大括号内的数据
						  }else {
							  System.err.println("百度吃不消了！");
						  }
						    System.out.println("百度校验结果："+result2);
						    if(!result2.equals("null")||rects.length!=0) {
						    	if(result2.equals("null")||rects.length==0) {
						    		hImg.setNeedCheack(1);
						    		System.out.println("标记了一个需要审核的头像");
						    	}
						    	headList.add(hImg); 
								  i++;
						    }else {
						    	new File(headImgPath).delete();
						    	System.out.println("验证人脸为空，已经删除");
						    }
					  }
					
					}
				

					headImgService.saveBatch(headList);
						
						for(HeadImg HImg :headList ) {
							headImgIds += HImg.getId().toString() + ",";
						}
						
						if(headImgIds.length() > 1) {
							img.setCutheadimg(1);
							img.setHeadImgIds(headImgIds.substring(0,headImgIds.length()-1));
							imgService.updateById(img);
						}
					
				}else {
					img.setCutheadimg(1);
					imgService.updateById(img);
					System.out.println("此图没有头像！");
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("截取头像一共用了"+(end-start)/1000+"秒");
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
		public void imageCut(String imagePath, String outFile, int posX, int posY, int width, int height) {
			// 原始图像
			Mat image = Imgcodecs.imread(imagePath);
			// 截取的区域：参数,坐标X,坐标Y,截图宽度,截图长度
			Rect rect = new Rect(posX, posY, width, height);
			// 两句效果一样
			Mat sub = image.submat(rect); // Mat sub = new Mat(image,rect);
			Mat mat = new Mat();
			Size size = new Size(width, height);
			Imgproc.resize(sub, mat, size);// 将人脸进行截图并保存
			Imgcodecs.imwrite(outFile, mat);
			// System.out.println(String.format("图片裁切成功，裁切后图片文件为： %s", outFile));
		}
		
		public static String getImgStr(String imgFile) {
			InputStream in = null;
			byte[] data = null;
			try {
				in = new FileInputStream(imgFile);
				data = new byte[in.available()];
				in.read(data);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
				return new String(Base64.encodeBase64(data));
		}
		
		public  String sample(AipFace client,String path) {
		    // 传入可选参数调用接口
		    HashMap<String, String> options = new HashMap<String, String>();
		    options.put("face_field", "age");
		    options.put("max_face_num", "10");
		    options.put("face_type", "LIVE");
		    options.put("liveness_control", "LOW");
		    
		    String image = getImgStr(path);
		    String imageType = "BASE64";
		    
		    // 人脸检测
		    JSONObject res = client.detect(image, imageType, options);
		    String result=res.toString();
		    	return result;
		}

}






