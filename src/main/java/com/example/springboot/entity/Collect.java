package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.hutool.core.annotation.Alias;

/**
 * <p>
 * 竞赛收藏表
 * </p>
 *
 * @author 
 * @since 
 */
@Getter
@Setter
@ApiModel(value = "Collect对象", description = "竞赛收藏表")
public class Collect implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @Alias("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("新闻")
    @Alias("新闻")
    private Integer blogId;

    @ApiModelProperty("用户")
    @Alias("用户")
    private Integer userId;

    @ApiModelProperty("添加时间")
    @Alias("添加时间")
    private String time;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String blogName;

    @TableField(exist = false)
    private Blog blog;

}
