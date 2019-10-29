/*
 * Copyright (c) 2018-2022 Caratacus, (caratacus@qq.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.crown.service;

import java.util.HashMap;
import java.util.List;

import org.crown.framework.service.BaseService;
import org.crown.model.dto.ActivityDetailDTO;
import org.crown.model.dto.HeadImgDTO;
import org.crown.model.dto.ImagesDTO;
import org.crown.model.entity.Activity;
import org.crown.model.entity.HeadImg;
import org.crown.model.entity.Images;

/**
 * <p>
 * 上传图片表 服务类
 * </p>
 *
 */
public interface IImagesService extends BaseService<Images> {

	int findCount(String galleryUuid);

	void addImgVisits(int id);

	Images findImageById(int id);

	void addImageThumbsUpById(int id, int num);

	List<Images> getThumbsUpRanking(String uuid);

	List<Images> getVisitsRanking(String uuid);

	HeadImg findHeadImgById(int id);

	Images getImgsByHead(int id);

	List<HashMap<String, Object>> getUUIDList();
	List<Images> groupList(String galleryUuid);

	List<Images> getImgs(String uuid);

	void setCutHeadImg(Integer id);

	List<Images> findAllImgsByUuid(String uuId);

	void updateWaterMarkStatus(Integer id);

	
}
