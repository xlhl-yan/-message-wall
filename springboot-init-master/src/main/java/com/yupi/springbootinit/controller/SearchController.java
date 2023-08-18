package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.SearchFacade;
import com.yupi.springbootinit.model.dto.search.UnifySearchQueryRequest;
import com.yupi.springbootinit.model.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


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
    private SearchFacade searchFacade;


    /**
     * 聚合搜索
     *
     * @return 每个类别的数据
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> unifySearch(@RequestBody UnifySearchQueryRequest searchRequest, HttpServletRequest request) {

        SearchVO search = searchFacade.unifySearch(searchRequest, request);
        ThrowUtils.throwIf(search == null, ErrorCode.SYSTEM_ERROR, "未能查询到数据");
        return ResultUtils.success(search);
    }
}
