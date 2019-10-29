package org.crown.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.crown.framework.mapper.BaseMapper;
import org.crown.model.dto.ImagesDTO;
import org.crown.model.entity.Images;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author Caratacus
 */
@Mapper
public interface ImagesMapper extends BaseMapper<Images> {

	int findCount(String galleryUuid);

	void addImgVisits(int id);

	void addImageThumbsUpById(int id, int num);

	List<Images> getThumbsUpRanking(String uuid);

	List<Images> getVisitsRanking(String uuid);

	List<HashMap<String, Object>> getUUIDList();
	List<Images> groupList(String galleryUuid);

	List<Images> getImgs(String uuid);

	void setCutHeadImg(Integer id);

	List<Images> findAllImgsByUuid(String uuId);

	void updateWaterMarkStatus(Integer id);
}
