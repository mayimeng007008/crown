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
package org.crown.model.dto;

import java.time.LocalDateTime;

import org.crown.enums.StatusEnum;
import org.crown.framework.model.convert.Convert;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 活动DTO
 * </p>
 *
 * @author Caratacus
 */
@ApiModel
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ActivityDTO extends Convert {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "主键")
    private String uuid;
    /**
 	* 活动主题
	 */
    @ApiModelProperty(notes = "活动主题")
	private String theme;
	/**
	 * 副主题
	 */
    @ApiModelProperty(notes = "副主题")
	private String subtopic;
	/**
	 	* 活动地址
	 */
    @ApiModelProperty(notes = "活动地址")
	private String address;
    
    @ApiModelProperty(notes = "水印地址")
    private String watermarkImg;
    /**
     * 照片数量
     */
    @TableField(exist = false )
    private Integer count;
    
    /**
     * 相册状态：可见/隐藏
     */
    private Integer status=1;
    
    @TableField(exist = false )
    @ApiModelProperty(notes = "活动封面图")
    private String themeImgPath;
    
    
    @TableField(exist = false )
    @ApiModelProperty(notes = "相册访问量")
    private Integer visits;
	
	/**
	 * 活动日期
	 */
    @ApiModelProperty(notes = "活动日期")
	private String activityDate;
    
}
