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
 * 
 * </p>
 *
 * @author 
 * @since 
 */
@Getter
@Setter
@ApiModel(value = "Music对象", description = "")
public class Music implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @Alias("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    @Alias("名称")
    private String name;

    @ApiModelProperty("歌曲")
    @Alias("歌曲")
    private String url;

    @ApiModelProperty("描述")
    @Alias("描述")
    private String content;

    @ApiModelProperty("封面")
    @Alias("封面")
    private String img;

    @ApiModelProperty("歌词")
    @Alias("歌词")
    private String lrc;

    @ApiModelProperty("歌曲类别")
    @Alias("歌曲类别")
    private Integer typeId;

    @ApiModelProperty("歌手")
    @Alias("歌手")
    private Integer singerId;

    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private String singerName;

    private String file;

}
