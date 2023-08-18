package com.yupi.springbootinit.datasource;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xlhl
 */
@Slf4j
@Component
public class PostDataSource implements TypeDataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, Long pageNum, Long pageSize) {
        PostQueryRequest queryRequest = new PostQueryRequest();
        queryRequest.setSearchText(searchText);
        queryRequest.setCurrent(pageNum);
        queryRequest.setPageSize(pageSize);
        Page<Post> postPage = postService.searchFromEs(queryRequest);
        log.info("postList：{}", postPage.getRecords());
        List<Post> records = postPage.getRecords();
        String jsonString = JSON.toJSONString(records);
        List<PostVO> postVO = JSON.parseArray(jsonString, PostVO.class);

        return new Page<PostVO>().setRecords(postVO);
    }
}




