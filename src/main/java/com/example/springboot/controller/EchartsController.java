package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Blog;
import com.example.springboot.entity.User;
import com.example.springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMusicService musicService;

    @Autowired
    private ISingerService singerService;

    @Autowired
    private ITypeService typeService;

    @Autowired
    private IBlogService blogService;

    @GetMapping("/count")
    public Result count() {
        long userCount = userService.count(new LambdaQueryWrapper<User>().ne(User::getUsername, "admin"));
        long musicCount = musicService.count();
        long singerCount = singerService.count();
        long typeCount = typeService.count();

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("userCount",userCount);
        result.put("musicCount",musicCount);
        result.put("singerCount",singerCount);
        result.put("typeCount",typeCount);
        return Result.success(result);
    }

    @GetMapping("/likes")
    public Result likes() {
        List<Blog> blogs = blogService.list();

        List<Map<String, Object>> maps = new ArrayList<>();
        for (Blog blog : blogs) {
            Map<String, Object> map = new HashMap<>();
            map.put("name",blog.getName());
            map.put("value",blog.getPageviews());
            maps.add(map);
        }
        return Result.success(maps);
    }

    @GetMapping("/members")
    public Result members() {
        List<User> list = userService.list();
        int q1 = 0; // 第一季度
        int q2 = 0; // 第二季度
        int q3 = 0; // 第三季度
        int q4 = 0; // 第四季度
        for (User user : list) {
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    @GetMapping("/data")
    public Result house1() {
        List<Dict> dicts = new ArrayList<>();
//        Dict dict = Dict.create();
//        dict.set("name", ).set("value", );
//        dicts.add(dict);
        return Result.success(dicts);
    }

}
