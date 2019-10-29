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
package org.crown.service.impl;

import java.io.File;

import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.mapper.ActivityMapper;
import org.crown.mapper.PageViewMapper;
import org.crown.model.entity.Activity;
import org.crown.model.entity.PageView;
import org.crown.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 */
@Transactional(readOnly = false)
@Service
public class ActivityServiceImpl extends BaseServiceImpl<ActivityMapper, Activity> implements IActivityService {

	@Autowired
	ActivityMapper activityMapper;
	@Autowired
	PageViewMapper pageViewMapper;
	
	@Value("${lycm.uploadPath}")
    private String uploadPath;
	@Value("${lycm.scaleImgPath}")
    private String scaleImgPath;

	
	@Override
	public void deleteImgs(String[] imgPathArr) {
		for (String imgName : imgPathArr) {
			String imgPath=uploadPath+imgName;
			String imgScaleImgPath = scaleImgPath+imgName;
			File fileimgPath = new File(imgPath);
			File fileimgScaleImgPath = new File(imgScaleImgPath);
			if(fileimgPath.exists()&&fileimgPath.isFile()) {
				fileimgPath.delete();
			}
			if(fileimgScaleImgPath.exists()&&fileimgScaleImgPath.isFile()) {
				fileimgScaleImgPath.delete();
			}
			activityMapper.deleteByPath(imgName);
		}
		
		
	}

	@Override
	public void deleteSelectedImgs(int[] ids) {
		for (int id : ids) {
			activityMapper.updateStatusById(id);
		}
		
	}

	@Override
	public void deleteAllImgs(String uuId) {
		activityMapper.deleteAllImgs(uuId);
		
	}

	@Override
	public void addVisits(PageView pv) {
		pageViewMapper.insert(pv);
		
	}

	@Override
	public void updateVisits(String uuid) {
		pageViewMapper.updateVisits(uuid);
		
	}

	@Override
	public PageView findVisits(String uuid) {
	
		
		return pageViewMapper.findVisits(uuid);
	}

	@Override
	public void changeStatus(String uuid,Integer status) {
		activityMapper.changeStatus(uuid,status);
		
	}

	@Override
	public void addWaterMarkImg(String hightImgName, String uuid) {
		activityMapper.addWaterMarkImg(hightImgName,uuid);
		
	}

	@Override
	public void deleteWaterImgs(String uuId) {
		activityMapper.deleteWaterImgs(uuId);
	}



	
	
}
