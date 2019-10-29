
package org.crown.model.entity;

import java.time.LocalDateTime;

import org.crown.enums.StatusEnum;
import org.crown.framework.model.convert.Convert;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 相册活动详情表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("webimages")
public class Images extends Convert {

    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     	* 默认为0,1表示已被删除
     */
    private Integer deleted;
    /**
     * 默认为0,1表示已经打了水印
     */
    private Integer watermarked=0;
    /**
     * 默认为0,1表示已经截取过头像了
     */
    private Integer cutheadimg=0;
    /**
     * 相片数量
     */
    @TableField(exist = false )
    private Integer count;
    
    /**
     * 访问量
     */
   
    private Integer visits=0;
    /**
     * 点赞数
     */
    @TableField(value="thumbs_up")
    private Integer thumbsUp=0;
    
    /**
     	*此图片裁剪的头像集合
     */
    @TableField(value="head_img_ids")
    private String headImgIds;
    
    /**
     *原始图片名称
     */
    @TableField(value="img_name")
    private String imgName;
    
    /**
     * 缩略图显示的图片名称路径
     */
    @TableField(value="scale_img_path")
    private String scaleImgPath;
    /**
     	* 高清图下载图片名称路径
     */
    @TableField(value="img_path")
    private String imgPath;

    /**
     * 所属活动相册
     */
    @TableField(value="gallery_uuid")
    private String galleryUuid;
    /**
     * 创建者ID
     */
    @TableField(value="creator", fill = FieldFill.INSERT)
    private Integer createUid;
    
    /**
     * 创建时间``, ``,
 * ``, 
     */
    @TableField(value="created_date",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(value="update_date",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
