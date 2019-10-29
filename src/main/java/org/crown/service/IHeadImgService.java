package org.crown.service;

import java.util.HashMap;
import java.util.List;

import org.crown.framework.responses.ApiResponses;
import org.crown.framework.service.BaseService;
import org.crown.model.dto.HeadImgDTO;
import org.crown.model.entity.HeadImg;

/**
 * <p>
 * 图片头像服务类
 * </p>
 *
 */
public interface IHeadImgService extends BaseService<HeadImg> {
	List<HashMap<String, Object>> getUUIDList();
	List<HeadImgDTO> groupList(String galleryUuid);
	List<HeadImg> getHeadImgs(String uuid);
	void updateHeadimgIds(Integer headOfPersonId, String imagesIds);
	void updateCompare(Integer id);
	void updateHeadOfPersonId(Integer id, Integer HeadOfPersonId);
	void updateHeadimgIds2(Integer id, String imagesId);
	void deleteSelectedHeadImgs(Integer id);
	List<HeadImg> getUncheackedHeadImgs(String uuid);
	List<Integer> getHeadCount(String uuid);
	 int removeImgsId(Integer id, Integer imgId);
	void upadatefollowId(Integer id, Integer id2);
	void updatedeleted(Integer id);
	void updateAllImgIds(Integer id, String imagesId);
	void updateCheack(Integer id);
	void person(Integer[] ids);

	
}
