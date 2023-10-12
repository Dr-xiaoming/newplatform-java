package com.example.springboot.service;

import com.example.springboot.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 
 */
public interface IBlogService extends IService<Blog> {
    List<Blog> getRecommendBLog();

}
