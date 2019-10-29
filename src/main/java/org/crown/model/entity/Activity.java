
package org.crown.model.entity;

import java.time.LocalDateTime;

import org.crown.enums.StatusEnum;
import org.crown.framework.model.convert.Convert;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 相册活动表
 * </p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("webactivity")
public class Activity extends Convert {

    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.UUID)
    private String uuid;
    /**
     	* 默认为0,1表示已被删除
     */
    private Integer deleted;
    
    /**
     	* 活动主题
     */
    private String theme;
    /**
     * 活动主题
     */
    @TableField(value="watermark_img")
    private String watermarkImg;
    /**
     * 副主题
     */
    private String subtopic;
    
    /**
     * 照片数量
     */
    @TableField(exist = false )
    private Integer count;
    /**
     * 相册状态：可见/隐藏
     */
    private Integer status=1;
    /**
     	* 活动地址
     */
    private String address;
    /**
     * 相册封面图片
     */
    @TableField(exist = false )
    private String themeImgPath;
    
    
    @TableField(exist = false )
    @ApiModelProperty(notes = "相册访问量")
    private Integer visits;

    /**
     * 创建者ID
     */
    @TableField(value="creator", fill = FieldFill.INSERT)
    private Integer createUid;
    
    /**
     * 活动日期
     */
    @TableField(value="activity_date")
    private String activityDate;
    
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
