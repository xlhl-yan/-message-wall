package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class PostDataSourceTest {

    @Resource
    private PostDataSource postDataSource;

    @Test
    void searchFromEs() {
        assert postDataSource != null;
        PostQueryRequest queryRequest = new PostQueryRequest();
        queryRequest.setSearchText("AzurLane");
        queryRequest.setCurrent(1);
        queryRequest.setPageSize(10);

        Page<Post> postPage = postDataSource.searchFromEs(queryRequest);
        assert postPage == null;
    }
}