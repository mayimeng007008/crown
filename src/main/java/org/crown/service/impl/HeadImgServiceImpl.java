package org.crown.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.mapper.HeadImgMapper;
import org.crown.mapper.ImagesMapper;
import org.crown.model.dto.HeadImgDTO;
import org.crown.model.entity.HeadImg;
import org.crown.model.entity.Images;
import org.crown.service.IHeadImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 表 服务实现类
 * </p>
 *
 */
@Transactional(readOnly = false)
@Service
public class HeadImgServiceImpl extends BaseServiceImpl<HeadImgMapper, HeadImg> implements IHeadImgService {

	@Autowired
	HeadImgMapper headimgMapper;
	@Autowired
	ImagesMapper imagesMapper;
	@Value("${lycm.imagePath}")
	private String imagePath;
	@Override
	public List<HashMap<String, Object>> getUUIDList() {
		return headimgMapper.getUUIDList();
	}

	@Override
	public List<HeadImgDTO> groupList(String galleryUuid) {
		return headimgMapper.groupList(galleryUuid);
	}

	@Override
	public List<HeadImg> getHeadImgs(String uuid) {
		return headimgMapper.getHeadImgs(uuid);
	}

	@Override
	public void updateHeadimgIds(Integer headOfPersonId, String imagesIds) {
		headimgMapper.updateHeadimgIds(headOfPersonId,imagesIds);
		
	}

	@Override
	public void updateCompare(Integer id) {
		headimgMapper.updateCompare(id);
	}

	@Override
	public void updateHeadOfPersonId(Integer id, Integer headOfPersonId) {
		headimgMapper.updateHeadOfPersonId(id,headOfPersonId);
	}

	@Override
	public void updateHeadimgIds2(Integer id, String imagesId) {
		headimgMapper.updateHeadimgIds2(id,imagesId);
		
	}

	@Override
	public void deleteSelectedHeadImgs(Integer id) {
		 HeadImg headimg=headimgMapper.selectById(id);
		int imgId= headimg.getImgFromId();
		Images img=imagesMapper.selectById(imgId);
		String headIds = img.getHeadImgIds();
		String[] arrIds=headIds.split(",");
		List<String> list= new ArrayList<String>(Arrays.asList(arrIds));
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(id+"")) {
				list.remove(i);
			}
		}
		String path=imagePath+headimg.getImgName();
		new File(path).delete();
		String newIds=list.toString().substring(1,list.toString().length()-1).replace(" ", "");
		img.setHeadImgIds(newIds);
		imagesMapper.updateById(img);
		headimgMapper.deleteSelectedHeadImgs(id);
	}

	@Override
	public List<HeadImg> getUncheackedHeadImgs(String uuid) {
		return headimgMapper.getUncheackedHeadImgs(uuid);
	}

	@Override
	public List<Integer> getHeadCount(String uuid) {
		List<Integer> list = new ArrayList<>();
		list.add(headimgMapper.findcheackCount0(uuid));
		list.add(headimgMapper.findcheackCount1(uuid));
		list.add(headimgMapper.findcheackCount2(uuid));
		return list;
	}

	@Override
	public  int removeImgsId(Integer id, Integer imgId) {
		HeadImg headimg=headimgMapper.selectById(id);
		int imgIdfromhead= headimg.getImgFromId();
		if(imgIdfromhead!=imgId) {
			String imgids=headimg.getImagesId();
			String[] arrIds=imgids.split(",");
			List<String> list= new ArrayList<String>(Arrays.asList(arrIds));
			System.out.println("移除前："+list.toString());
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).equals(imgId+"")) {
					list.remove(i);
				}
			}
			System.out.println("移除后："+list.toString());
				String newIds=list.toString().substring(1,list.toString().length()-1).replace(" ", "");
				headimg.setImagesId(newIds);
				headimgMapper.updateById(headimg);
				return 200;
		}else {
			return 201;
		}
	}

	@Override
	public void updatedeleted(Integer id) {
		headimgMapper.updatedeleted(id);
	}

	@Override
	public void upadatefollowId(Integer id, Integer id2) {
		headimgMapper.upadatefollowId(id,id2);
	}

	@Override
	public void updateAllImgIds(Integer id, String imagesId) {
		headimgMapper.updateAllImgIds(id,imagesId);
	}

	@Override
	public void updateCheack(Integer id) {
		headimgMapper.updateCheack(id);
		
	}

	@Override
	public void person(Integer[] ids) {
		System.out.println("收到");
		List<HeadImg> list = new ArrayList<>();
		for (Integer id : ids) {
			list.add(headimgMapper.selectById(id));
		}
		int imgidsLength=0;
		int index=0;
		
		for (int i = 0; i < list.size(); i++) {
			int length=(list.get(i).getImagesId()).length();
			if(length>imgidsLength) {
				imgidsLength=length;
				index=i;
			}
		}
		String imgids=list.get(index).getImagesId();
		for (int i = 0; i < list.size(); i++) {
			if(i!=index) {
				imgids+=","+list.get(i).getImagesId();
			}
		}
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setImagesId(imgids);
			if(index!=i) {
				list.get(i).setFollowimgId(list.get(index).getId());
				list.get(i).setDeleted(3);
				headimgMapper.updatefollowingid(list.get(i).getId(),list.get(index).getId());
			}
		}
		for (HeadImg headImg : list) {
			headimgMapper.updateById(headImg);
		}
		headimgMapper.updateAllImgIds(list.get(index).getId(),list.get(index).getImagesId());
	
	}
}
