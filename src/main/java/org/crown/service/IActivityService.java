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

import org.crown.framework.service.BaseService;
import org.crown.model.dto.ActivityDetailDTO;
import org.crown.model.entity.Activity;
import org.crown.model.entity.PageView;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 */
public interface IActivityService extends BaseService<Activity> {

	void deleteImgs(String[] imgPathArr);

	void deleteSelectedImgs(int[] ids);

	void deleteAllImgs(String uuId);

	void addVisits(PageView pv);

	void updateVisits(String uuid);

	PageView findVisits(String uuid);

	void changeStatus(String uuid, Integer status);

	void addWaterMarkImg(String hightImgName, String uuid);

	void deleteWaterImgs(String uuId);

	

	

}
