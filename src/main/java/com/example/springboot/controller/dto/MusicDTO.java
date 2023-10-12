package com.example.springboot.controller.dto;

import com.example.springboot.entity.Menu;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 封装我的歌单前端实体
 */
@Data
@Accessors(chain = true)
public class MusicDTO {

    private String title;
    private String artist;
    private String url;
    private String pic;
    private String lrc;
    private String theme;

}
