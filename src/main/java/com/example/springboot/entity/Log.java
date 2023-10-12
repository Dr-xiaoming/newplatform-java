package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import cn.hutool.core.annotation.Alias;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since
 */
@Getter
@Setter
@Builder
@TableName("sys_log")
@ApiModel(value = "Log对象", description = "")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("操作")
    @Alias("操作")
    private String name;

    @ApiModelProperty("入参")
    @Alias("入参")
    private String params;

    @ApiModelProperty("出参")
    @Alias("出参")
    private String output;

    @ApiModelProperty("url")
    @Alias("url")
    private String url;

    @ApiModelProperty("执行时间")
    @Alias("执行时间")
    private Integer duration;

    @ApiModelProperty("IP")
    @Alias("IP")
    private String ip;

    @ApiModelProperty("地址")
    @Alias("地址")
    private String address;

    @ApiModelProperty("操作人")
    @Alias("操作人")
    private String username;

    @ApiModelProperty("创建时间")
    @Alias("创建时间")
    private String createTime;

    @ApiModelProperty("软删除")
    @Alias("软删除")
    private Integer deleted;


}
