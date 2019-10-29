
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
import lombok.experimental.Accessors;

/**
 * <p>
 * 照片截取头像表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain=true)
@TableName("webheadimg")
public class HeadImg extends Convert {

    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     	* 表示头像状态
     	* 2表示为主照片，唯一
     	* 3表示从照片  不唯一，达到去重效果
     */
    private Integer deleted=2;
    
    /**
     * 是否需要审核的头像
     * 默认为0  表示需要确定，不需要审核
     * 1表示需要审核
     * 2表示已经确认
     */
    @TableField(value="need_cheack")
    private Integer needCheack=0;
   
    
    /**
 	* 来自哪张图片
 */
    @TableField(value="img_from_id")
    private Integer imgFromId;
    /**
     * 头像分类id，也就是主照片id
     */
    @TableField(value="followimg_id")
    private Integer followimgId;
    /**
     * 默认为0,1表示已经进行比较过
     */
    private Integer compared=0;
    
    /**
     	*原始图片名称
     */
    @TableField(value="img_name")
    private String imgName;
    /**
     *百度返回的头像唯一标识
     */
    @TableField(value="face_token")
    private String faceToken;
    
    /**
     * 所属图片id
     */
    @TableField(value="images_id")

    private String imagesId;

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

    /**
     * 人物头像对比的相似率
     */
    private Integer similarityRate;
    
    /**
     * 备用拓展字段
     */
    private String expend;
    
}
