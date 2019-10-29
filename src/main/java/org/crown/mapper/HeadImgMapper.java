package org.crown.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.crown.framework.mapper.BaseMapper;
import org.crown.model.dto.HeadImgDTO;
import org.crown.model.entity.HeadImg;

/**
 * <p>
 * 图片头像 Mapper 接口
 * </p>
 *
 * @author Caratacus
 */
@Mapper
public interface HeadImgMapper extends BaseMapper<HeadImg> {
	List<HashMap<String, Object>> getUUIDList();
	List<HeadImgDTO> groupList(String galleryUuid);

	HeadImg findHeadImgById(int id);
	List<HeadImg> getHeadImgs(String uuid);
	void updateHeadimgIds(Integer headOfPersonId, String imagesIds);
	void updatePersonHeadimgIds(String imagesIds, String headimgName);
	void updateCompare(Integer id);
	void updateHeadOfPersonId(Integer id, Integer headOfPersonId);
	void updateHeadimgIds2(Integer id, String imagesId);
	void deleteSelectedHeadImgs(Integer id);
	List<HeadImg> getUncheackedHeadImgs(String uuid);
	Integer findcheackCount0(String uuid);
	Integer findcheackCount1(String uuid);
	Integer findcheackCount2(String uuid);
	void updatedeleted(Integer id);
	void upadatefollowId(Integer id, Integer id2);
	void updateAllImgIds(Integer id, String imagesId);
	void updateCheack(Integer id);
	void updatefollowingid(Integer id, Integer id2);

}
