package org.crown.model.dto;

import org.crown.framework.model.convert.Convert;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 图片头像DTO
 * </p>
 *
 * @author Caratacus
 */
@ApiModel
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HeadImgDTO extends Convert {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "主键")
    private Integer id;
    
    /**
	 	*原始图片名称
	 */
    @ApiModelProperty(notes = "图片截取头像名称")
	private String imgName;
    
    /**
     * 默认为0,1表示此活动中唯一的人物头像
     */
    @ApiModelProperty(notes = "默认为0,1表示此活动中唯一的人物头像")
    private Integer deleted;
    /**
     *百度返回的头像唯一标识
     */
    @ApiModelProperty(notes = "人脸唯一表示")
    private String faceToken;
    
    /**
     * 是否需要审核的头像
     */
    @TableField(value="need_cheack")
    private Integer needCheack;
	
	/**
	 * 所属活动相册
	 */
    @ApiModelProperty(notes = "所属图片的id")
	private String imagesId;
    
    /**
	 * 所属活动相册
	 */
    @ApiModelProperty(notes = "所属活动相册的uuid")
	private String galleryUuid;
    
    /**
     * 人物头像对比的相似率
     */
    @ApiModelProperty(notes = "人物头像对比的相似率")
    private Integer similarityRate;
    
    /**
     * 备用拓展字段
     */
    @ApiModelProperty(notes = "备用拓展字段")
    private String expend;
}
