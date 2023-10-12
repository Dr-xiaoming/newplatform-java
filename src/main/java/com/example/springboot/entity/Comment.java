package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.hutool.core.annotation.Alias;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author 
 * @since 
 */
@Getter
@Setter
@ApiModel(value = "Comment对象", description = "评论表")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @Alias("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("评论内容")
    @Alias("评论内容")
    private String content;

    @ApiModelProperty("用户")
    @Alias("用户")
    private Integer userId;

    @ApiModelProperty("新闻")
    @Alias("新闻")
    private Integer blogId;

    @ApiModelProperty("父级评论ID")
    @Alias("父级评论ID")
    private Integer pid;

    @ApiModelProperty("评论时间")
    @Alias("评论时间")
    private String time;

    @TableField(exist = false)
    private List<Comment> children;

    @TableField(exist = false)
    private String img;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String blogName;

    @TableField(exist = false)
    private String parentName;

}
