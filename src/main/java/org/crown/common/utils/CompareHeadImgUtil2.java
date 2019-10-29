package org.crown.common.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.crown.model.entity.HeadImg;
import org.crown.service.IHeadImgService;
import org.crown.service.IImagesService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;

/**
 *  头像比对工具 ma
 */

@Component
public class CompareHeadImgUtil2 {
	@Autowired
	private IHeadImgService headImgService;
	
	@Autowired
	private IImagesService imgService;

	@Value("${lycm.imagePath}")
	private String imagePath;
	
	@Value("${lycm.similarRate}")
	private Integer similarRate;
	
	//@Value("${lycm.frontalfaceFilePath}")
	//private  String frontalfaceFilePath;
	int status=201;
	private int compareNum=0;
	private int comparePercent=0;
		public  String CompareHeadImg(String uuid) throws Exception {
			status=200;
			System.out.println("开始比对头像！！");
			long start = System.currentTimeMillis();
			int e=0;
			//获取没有比对过的头像
			List<HeadImg> headimgs = headImgService.getHeadImgs(uuid);
			if(!headimgs.isEmpty()) {
				System.out.println("有头像："+headimgs.size());
				//获取所有人物列表
				int a =1;
				for (int i = 0; i < headimgs.size(); i++) {
					int b=1;
					int c=0;
					int d=0;
					if(headimgs.get(i).getDeleted()==2) {
						String faceToken=headimgs.get(i).getFaceToken();
						for (int j = i+1; j < headimgs.size(); j++) {
							b++;
						//没进行比较过的才进行比较
							if(headimgs.get(j).getCompared()!=1&&headimgs.get(j).getDeleted()==2) {
								//来自同一照片不比对
								e++;
									if(!headimgs.get(i).getImgFromId().equals(headimgs.get(j).getImgFromId())) {
										String faceToken2=headimgs.get(j).getFaceToken();
										//Thread.currentThread().sleep(100);
										AipFace client =new AipFace("17576603", "bxYz7lba8CIIuarmIHmCZVOU", "WaZ3KRISDFH4YkpawxRd85Eto43QdGYa");
										double deep = sample(client,faceToken,faceToken2);
										if(deep>=93.0) {
											c++;
											String imgids=headimgs.get(i).getImagesId();
											imgids+=","+headimgs.get(j).getImagesId();
											headimgs.get(i).setImagesId(imgids);
											headimgs.get(j).setDeleted(3);
											headImgService.upadatefollowId(headimgs.get(j).getId(),headimgs.get(i).getId());
											headImgService.updatedeleted(headimgs.get(j).getId());
										}else {
											d++;
										}
										
									}else {
										d++;
										System.out.println("来自同一图片不需要比对！");
									}
								}else {
									d++;
									System.out.println("此图片已经比较过了！");
								}
						
						
							System.out.println("第："+a+"张头像与第："+b+"张头像进行比对，"+c+"个比对成功,"+d+"个不匹配");
					}
						System.out.println("第"+a+"张照片比对完成："+c+"个比对成功,"+d+"个不匹配");
						System.out.println("第"+a+"张照片开始统一imgids");
						headImgService.updateHeadimgIds2(headimgs.get(i).getId(),headimgs.get(i).getImagesId());
						headImgService.updateAllImgIds(headimgs.get(i).getId(),headimgs.get(i).getImagesId());
						System.out.println("完成统一！");
					}else {
						System.err.println("名花有主！！");
					}
				
					a++;
					headImgService.updateCompare(headimgs.get(i).getId());
					compareNum++;
					comparePercent=(compareNum*100)/headimgs.size();
				}
			}else {
				System.out.println("没有可以对比的图片");
				return "请先确认头像！";
			}
			compareNum=0;
			status=201;
			comparePercent=0;
			long end = System.currentTimeMillis();
			System.out.println("e:"+e);
	
			if(e==0) {
				System.out.println("没有新增确认头像，请确认");
				return "没有新增确认头像，请确认";
			}else {
				
				System.out.println("总共耗时："+(end-start)/1000/60+"分："+((end-start)/1000)%60+"秒");
				return "对比结束，头像："+headimgs.size()+"个，总共耗时："+(end-start)/1000/60+"分："+((end-start)/1000)%60+"秒";
			}
		}
		
		public List<Integer> getCompareStatus(){
			List<Integer> list = new ArrayList<>();
			list.add(status);
			list.add(comparePercent);
			return list;
		}
		
		
		public  double sample(AipFace client,String pa,String pa2) throws Exception {
	    			// image1/image2也可以为url或facetoken, 相应的imageType参数需要与之对应。
	    			MatchRequest req1 = new MatchRequest(pa, "FACE_TOKEN");
	    			MatchRequest req2 = new MatchRequest(pa2, "FACE_TOKEN");
	    			ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
	    			requests.add(req1);
	    			requests.add(req2);

	    			JSONObject res = client.match(requests);
	    			 String result=res.toString();
	    			 JSONObject dataJson= new JSONObject(result);//把字符串转成json数据
	    			 if(dataJson.has("result")) {
	    				 result = (String) dataJson.get("result").toString();//获取到json数据中result大括号内的数据
	    				 System.out.println(result);
	    				 if(!result.equals("null")) {
	    					 JSONObject face1 = new JSONObject(result) ;
	    					 String score1 = (String) face1.get("score").toString();
	    					 if(!score1.equals("null")) {
	    						 double  score = Double.parseDouble(score1);
	    						 return score;
	    					 }else {
	    						 double score=0.0;
	    						 System.out.println(score);
	    						 return score;
	    					 }
	    					 
	    				 }else {
	    					 return 0.0; 
	    				 }
	    			 }else {
	    				 System.out.println("请求百度超时！");
	    				 return 0.0; 
	    			 }
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



