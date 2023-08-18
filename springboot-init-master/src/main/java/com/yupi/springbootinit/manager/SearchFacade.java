package com.yupi.springbootinit.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.datasource.*;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.search.UnifySearchQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * SearchFacade
 * 门面模式
 *
 * @author xlhl
 * @version 1.0
 * @description 搜索门面
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private TypeDataSourceRegistry dataSourceRegistry;

    /**
     * 聚合搜索
     *
     * @return 每个类别的数据
     */
    public SearchVO unifySearch(@RequestBody UnifySearchQueryRequest searchRequest, HttpServletRequest request) {
        String type = searchRequest.getType();
        SearchTypeEnum typeEnum = SearchTypeEnum.getEnumByValue(type);

        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);

        SearchVO searchVO = new SearchVO();

        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();

        Page<UserVO> userPage = null;
        Page<Picture> picturePage = null;
        Page<PostVO> postPage = null;
        typeEnum = Optional.ofNullable(typeEnum).orElse(SearchTypeEnum.ALL);
        if (typeEnum == SearchTypeEnum.ALL) {
            //异步
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() ->
                    pictureDataSource.doSearch(searchText, current, pageSize)
            );
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() ->
                    postDataSource.doSearch(searchText, current, pageSize)
            );
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() ->
                    userDataSource.doSearch(searchText, current, pageSize)
            );

            //在线程全部执行完毕之前不会继续往下
            CompletableFuture.allOf(userTask, postTask, userTask).join();

            try {
                userPage = userTask.get();
                picturePage = pictureTask.get();
                postPage = postTask.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("查询异常：", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "无法获取到数据");
            }

            if (userPage != null) {
                searchVO.setUserList(userPage.getRecords());
            }
            if (postPage != null) {
                searchVO.setPostList(postPage.getRecords());
            }
            if (picturePage != null) {
                searchVO.setPictureList(picturePage.getRecords());
            }
            return searchVO;
        } else {
            TypeDataSource typeDataSource = dataSourceRegistry.getDataSource(type);
            Page<?> page = typeDataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());
        }
        return searchVO;
    }
}
