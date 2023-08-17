package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.UnifySearchQueryRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * SearchController
 *
 * @author xlhl
 * @version 1.0
 * @description 统一的搜索接口
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;


    /**
     * 聚合搜索
     *
     * @return 每个类别的数据
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> unifySearch(@RequestBody UnifySearchQueryRequest unifyRequest, HttpServletRequest request) {
        String searchText = unifyRequest.getSearchText();

        // 异步
        CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
            long current = unifyRequest.getCurrent();
            long pageSize = unifyRequest.getPageSize();

            return pictureService.searchPicture(searchText, current, pageSize);
        });
        CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            return postService.listPostVoByPage(postQueryRequest, request);
        });
        CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            return userService.listUser(userQueryRequest);
        });

        //在线程全部执行完毕之前不会继续往下
        CompletableFuture.allOf(userTask, postTask, userTask).join();
        Page<UserVO> userPage = null;
        Page<Picture> picturePage = null;
        Page<PostVO> postPage = null;
        try {
            userPage = userTask.get();
            picturePage = pictureTask.get();
            postPage = postTask.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("查询异常：", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "无法获取到数据");
        }

        SearchVO searchVO = new SearchVO();
        if (userPage != null) {
            searchVO.setUserList(userPage.getRecords());
        }
        if (postPage != null) {
            searchVO.setPostList(postPage.getRecords());
        }
        if (picturePage != null) {
            searchVO.setPictureList(picturePage.getRecords());
        }

        return ResultUtils.success(searchVO);
    }
}
