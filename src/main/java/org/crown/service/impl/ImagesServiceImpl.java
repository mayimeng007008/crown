package org.crown.service.impl;

import java.util.HashMap;
import java.util.List;

import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.mapper.HeadImgMapper;
import org.crown.mapper.ImagesMapper;
import org.crown.model.dto.ImagesDTO;
import org.crown.model.entity.HeadImg;
import org.crown.model.entity.Images;
import org.crown.service.IImagesService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ImagesServiceImpl extends BaseServiceImpl<ImagesMapper, Images> implements IImagesService {

	@Autowired
	ImagesMapper imagesMapper;
	@Autowired
	HeadImgMapper headImgMapper;
	@Override
	public int findCount(String galleryUuid) {
	
		return imagesMapper.findCount(galleryUuid);
	}

	@Override
	public void addImgVisits(int id) {
		
		imagesMapper.addImgVisits(id);
	}

	@Override
	public Images findImageById(int id) {
		
		return imagesMapper.selectById(id);
	}

	@Override
	public void addImageThumbsUpById(int id, int num) {
		
		imagesMapper.addImageThumbsUpById(id,num);
	}

	@Override
	public List<Images> getThumbsUpRanking(String uuid) {
		
		return imagesMapper.getThumbsUpRanking(uuid);
	}

	@Override
	public List<Images> getVisitsRanking(String uuid) {
		return imagesMapper.getVisitsRanking(uuid);
	}

	@Override
	public HeadImg findHeadImgById(int id) {
		
		return headImgMapper.findHeadImgById(id);
	}

	@Override
	public Images getImgsByHead(int id) {
		
		return imagesMapper.selectById(id);
	}

	@Override
	public List<HashMap<String, Object>> getUUIDList() {
		return imagesMapper.getUUIDList();
	}

	@Override
	public List<Images> groupList(String galleryUuid) {
		return imagesMapper.groupList(galleryUuid);
	}

	@Override
	public List<Images> getImgs(String uuid) {
		return imagesMapper.getImgs(uuid);
	}

	@Override
	public void setCutHeadImg(Integer id) {
		System.out.println("我service执行了！");
		System.out.println("我收到的id："+id);
		imagesMapper.setCutHeadImg(id);
		
	}

	@Override
	public List<Images> findAllImgsByUuid(String uuId) {
		
		return imagesMapper.findAllImgsByUuid(uuId);
	}

	@Override
	public void updateWaterMarkStatus(Integer id) {
		imagesMapper.updateWaterMarkStatus(id);		
	}

	
}
