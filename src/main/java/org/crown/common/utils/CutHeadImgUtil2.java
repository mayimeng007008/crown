package org.crown.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.crown.model.entity.HeadImg;
import org.crown.model.entity.Images;
import org.crown.service.IHeadImgService;
import org.crown.service.IImagesService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.aip.face.AipFace;

/**
 * Created by meng on 2018/5/8. 文件上传工具类
 */

@Component
public class CutHeadImgUtil2 {
	@Autowired
	IHeadImgService headImgService;

	@Autowired 
	IImagesService imgService;

	//@Value("${lycm.frontalfaceFilePath}")
	//private  String frontalfaceFilePath;

	@Value("${lycm.imagePath}")
	private String imagePath;
	//是否在检测的状态200为正在对比，201为没有检测
	int status=201;
	//已经检测过的照片数量
	private int cutImgNum=0;
	//检测的百分比*100
	private int cutPercent=0;

	public  String cutHeadImg(String uuid) throws InterruptedException {
		status=200;
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
		//CascadeClassifier faceDetector = new CascadeClassifier(frontalfaceFilePath);
		AipFace client =new AipFace("17576603", "bxYz7lba8CIIuarmIHmCZVOU", "WaZ3KRISDFH4YkpawxRd85Eto43QdGYa");

		System.out.println("开始截图！");

		long start = System.currentTimeMillis();
		List<Images> imgs = imgService.getImgs(uuid);
		System.out.println("照片个数："+imgs.size());
		if(imgs.size()==0) {
			status=201;
			return "没有可检测头像";
			
		}
		int count=0;
		for (Images img : imgs) {
			//Thread.currentThread().sleep(200);
			String imgPath = imagePath + img.getImgPath();
			String result=sample(client,imgPath);
			JSONObject dataJson= new JSONObject(result);//把字符串转成json数据
			if(dataJson.has("result")) {
				result = (String) dataJson.get("result").toString();//获取到json数据中result大括号内的数据
				System.out.println(result);
				if(!result.equals("null")) {
					String headImgIds="";
					List<HeadImg> headList = new ArrayList<HeadImg>();
					JSONObject face_list = new JSONObject(result);//把result内的字符串转成json数据，这边我要获取face_list数组里的数据
					JSONArray face_lists = face_list.getJSONArray("face_list");//  用JSONArray对象获取face_list数组数据
					System.out.println("一共有人脸："+face_lists.length());
					HeadImg hImg =null;
					CutImgUtil ct=null;
					for(int i=0;i<face_lists.length();i++){//循环数组，取出想要的数据
						JSONObject face1 = (JSONObject) face_lists.get(i);
						String faceToken= (String) face1.get("face_token").toString();
						String location= (String) face1.get("location").toString();
						JSONObject locationjson= new JSONObject(location);
						String top1= (String) locationjson.get("top").toString();
						String left1= (String) locationjson.get("left").toString();
						String width1= (String) locationjson.get("width").toString();
						String height1= (String) locationjson.get("height").toString();
						System.out.println("top:"+top1);
						System.out.println("left:"+left1);
						String[] toparr=top1.split("\\.");
						String[] leftarr=left1.split("\\.");
						int top=Integer.parseInt(toparr[0]);
						int left=Integer.parseInt(leftarr[0]);
						int width=Integer.parseInt(width1);
						int height=Integer.parseInt(height1);

						//设置头像信息
						String hImgName = "head" + i + "_" + img.getImgPath();
						hImg = new HeadImg();
						hImg.setFaceToken(faceToken);
						hImg.setImagesId(img.getId()+"");
						hImg.setImgFromId(img.getId());
						hImg.setImgName(hImgName);
						hImg.setGalleryUuid(img.getGalleryUuid());
						//截图
						ct = new CutImgUtil(left,top,width,height);
						ct.setSrcpath(imgPath);
						ct.setSubpath(imagePath+hImgName);
						if(top>0&&left>0) {
							try {
								ct.cut();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
							String headImgPath=imagePath+hImgName; long len = new
									File(headImgPath).length()/1024;
							if(len < 0.5){ 
								new File(headImgPath).delete();
							System.out.println("这个头像太小,为："+len+"kb，已经删除");
							}else {
								headList.add(hImg); 
								System.out.println("截图成功！");
								count++;
							}
						}else {
							System.out.println("坐标为负！");
						}
					
					}
					headImgService.saveBatch(headList);
					for(HeadImg HImg :headList ) {
						headImgIds += HImg.getId().toString() + ",";
					}
					if(headImgIds.length() > 1) {
						img.setCutheadimg(1);
						img.setHeadImgIds(headImgIds.substring(0,headImgIds.length()-1));
					}else {
						img.setCutheadimg(1);
					}
					imgService.updateById(img);
				}else {
					img.setCutheadimg(1);
					imgService.updateById(img);
					System.out.println("此图没有头像！");
				}
			}else {
				System.err.println("请求超限！");
			}
			cutImgNum++;
			cutPercent=(cutImgNum*100)/imgs.size();
			}
		status=201;
		cutImgNum=0;
		cutPercent=0;
		long end = System.currentTimeMillis();
		System.out.println("检测完毕，共检测头像"+count+"个，用时"+(end-start)/1000/60+"分"+((end-start)/1000)%60+"秒");	
		return "检测完毕，共检测头像"+count+"个，用时"+(end-start)/1000/60+"分"+((end-start)/1000)%60+"秒";
	}
	public List<Integer> getCutStatus(){
		List<Integer> list = new ArrayList<>();
		list.add(status);
		list.add(cutPercent);
		return list;
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



}






