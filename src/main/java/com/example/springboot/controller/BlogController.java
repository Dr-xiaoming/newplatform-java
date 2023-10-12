package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.annotation.AutoLog;
import com.example.springboot.entity.Collect;
import com.example.springboot.entity.Comment;
import com.example.springboot.service.ICollectService;
import com.example.springboot.service.ICommentService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;
import com.example.springboot.service.IUserService;

import com.example.springboot.service.IBlogService;
import com.example.springboot.entity.Blog;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private IBlogService blogService;

    @Resource
    private ICommentService commentService;

    @Resource
    private ICollectService collectService;

    @Resource
    IUserService userService;

    private final String now = DateUtil.now();

    /**
     * 新闻排行榜
     * @return
     */
    @GetMapping("/top")
    public Result top( @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize) {
        Page<Blog> page = blogService.page(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<Blog>().eq(Blog::getState,"审核通过").orderByDesc(Blog::getPageviews));
        return Result.success(page);
    }

    /**
     * 推荐新闻
     * @return
     */
    @GetMapping("/recommend")
    public Result recommend() {
        return Result.success(blogService.getRecommendBLog());
    }

    @GetMapping("/pageviews/{id}")
    public Result pageviews(@PathVariable Integer id) {
        if (id != null){
            Blog blog = blogService.getById(id);
            blog.setPageviews(blog.getPageviews() + 1);
            blogService.updateById(blog);
            return Result.success(blog);
        }
        return Result.success();
    }

    // 新增或者更新
    @PostMapping
    @AutoLog("新增|修改新闻信息")
    public Result save(@RequestBody Blog blog) {
        if (blog.getId() == null) {
            blog.setPageviews(0);
            blog.setTime(DateUtil.now());
            blog.setUser(TokenUtils.getCurrentUser().getNickname());
            //blog.setUserid(TokenUtils.getCurr
            // entUser().getId());
            if (Objects.isNull(blog.getBlogType())){
                return Result.error("201","请选择新闻类别");
            }
        }
        blogService.saveOrUpdate(blog);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @AutoLog("删除新闻信息")
    public Result delete(@PathVariable Integer id) {
        // 如果关联收藏 评论无法删除
        List<Comment> comments = commentService.list(new LambdaQueryWrapper<Comment>().eq(Comment::getBlogId, id));
        List<Collect> collects = collectService.list(new LambdaQueryWrapper<Collect>().eq(Collect::getBlogId, id));

        if (!CollectionUtils.isEmpty(comments) || !CollectionUtils.isEmpty(collects)){
            return Result.error("201","您无法删除该新闻");
        }
        blogService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    @AutoLog("批量删除新闻信息")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            List<Comment> comments = commentService.list(new LambdaQueryWrapper<Comment>().eq(Comment::getBlogId, id));
            List<Collect> collects = collectService.list(new LambdaQueryWrapper<Collect>().eq(Collect::getBlogId, id));

            if (!CollectionUtils.isEmpty(comments) || !CollectionUtils.isEmpty(collects)){
                return Result.error("201","您无法删除该新闻");
            }
        }
        blogService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(blogService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(blogService.getById(id));
    }

    @GetMapping("/page/type")
    public Result findPageType(@RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String type,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("state","审核通过");
        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
            queryWrapper.and(wrapper -> wrapper.or(wrapper1 -> wrapper1.like("name",name))
                    .or(wrapper2 -> wrapper2.like("blog_type",name)));
        }

        if (!"".equals(type) && !"0".equals(type)) {
            queryWrapper.like("blog_type", type);
        }
        return Result.success(blogService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
            queryWrapper.and(wrapper -> wrapper.or(wrapper1 -> wrapper1.like("name",name))
                    .or(wrapper2 -> wrapper2.like("blog_type",name)));
        }
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("ROLE_USER")) {
            queryWrapper.eq("user", currentUser.getNickname());
        }
        return Result.success(blogService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/page/search")
    public Result findPageSearch(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("state","审核通过");
        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
            queryWrapper.and(wrapper -> wrapper.or(wrapper1 -> wrapper1.like("name",name))
                    .or(wrapper2 -> wrapper2.like("blog_type",name)));
        }
        return Result.success(blogService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Blog> list = blogService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Blog信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Blog> list = reader.readAll(Blog.class);

        blogService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

