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
 * 活动DTO
 * </p>
 *
 * @author Caratacus
 */
@ApiModel
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImagesDTO extends Convert {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "主键")
    private Integer id;
    
    /**
     * 相片数量
     */
    @ApiModelProperty(notes = "相片数量")
    private Integer count;
    
    /**
	 	*此图片裁剪的头像集合
	 */
    @ApiModelProperty(notes = "此图片裁剪的头像集合")
	private String headImgIds;

    /**
	 	*原始图片名称
	 */
    @ApiModelProperty(notes = "原始图片名称")
	private String imgName;
	/**
	 * 缩略图显示的图片名称路径
	 */
    @ApiModelProperty(notes = "缩略图显示的图片名称路径")
	private String scaleImgPath;
	/**
	 	* 高清图下载图片名称路径
	 */
    @ApiModelProperty(notes = "高清图下载图片名称路径")
	private String imgPath;
	
	/**
	 * 所属活动相册
	 */
    @ApiModelProperty(notes = "所属活动相册")
	private String galleryUuid;
}
