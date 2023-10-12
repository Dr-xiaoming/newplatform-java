package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Blog;
import com.example.springboot.entity.Collect;
import com.example.springboot.entity.Comment;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.BlogMapper;
import com.example.springboot.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.service.ICollectService;
import com.example.springboot.service.ICommentService;
import com.example.springboot.service.IUserService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {


    @Resource
    private IUserService userService;

    @Resource
    private ICommentService commentService;

    @Resource
    private ICollectService collectService;

    // 基于用户的协同推荐算法
    // 推荐电影
    public List<Blog> getRecommendBLog() {
        User currentUser = TokenUtils.getCurrentUser();
        List<User> userList = userService.list(new QueryWrapper<User>().eq("role", "ROLE_USER"));

        List<RelateDTO> relateDTOList = new ArrayList<>();
        List<Blog> filmList = list(new QueryWrapper<Blog>().orderByDesc("id"));  // 筛选电影
        for (Blog blog : filmList) {
            for (User user : userList) {
                int index = 1;
                Set<Blog> userFilmSet = getUserShortCommentaryFilm(user.getId());
                boolean comment = userFilmSet.stream().anyMatch(g -> g.getId().equals(blog.getId()));
                if (comment) {
                    index += 2;  // 评论过，分数+2
                }
                List<Collect> collectList = collectService.list(new QueryWrapper<Collect>().eq("user_id", user.getId()).eq("blog_id", blog.getId()));
                if (collectList.size() > 0) {
                    index += 2;  // 收藏过  分数+2
                }
                if (index > 1) {
                    RelateDTO relateDTO = new RelateDTO(user.getId(), blog.getId(), index);
                    relateDTOList.add(relateDTO);
                }
            }
        }
        List<Integer> recommendFilms = UserCF.recommend(currentUser.getId(), relateDTOList);
        return recommendFilms.stream().map(filmId -> filmList.stream().filter(film -> film.getId().equals(filmId)).findFirst().orElse(null)).limit(6).collect(Collectors.toList());
    }

    private Set<Blog> getUserShortCommentaryFilm(Integer userid) {
        Set<Blog> blogSet = new HashSet<>();
        List<Comment> comments = commentService.list(new QueryWrapper<Comment>().eq("user_id", userid));// 筛选用户写过的影评
        for (Comment comment : comments) {
            List<Comment> orderItems = commentService.list(new QueryWrapper<Comment>().eq("blog_id", comment.getBlogId()));
            blogSet.addAll(orderItems.stream().map(orderItem -> getById(orderItem.getBlogId())).collect(Collectors.toSet()));
        }
        return blogSet;
    }

}
