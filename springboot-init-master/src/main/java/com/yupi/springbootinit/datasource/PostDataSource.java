package com.yupi.springbootinit.datasource;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.post.PostEsDTO;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

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

    @Deprecated
    public Page<Post> searchFromEs(PostQueryRequest postQueryRequest) {
        //参数校验
        Long id = postQueryRequest.getId();
        Long notId = postQueryRequest.getNotId();
        String searchText = postQueryRequest.getSearchText();
        String title = postQueryRequest.getTitle();
        String content = postQueryRequest.getContent();
        List<String> tags = postQueryRequest.getTags();
        List<String> orTags = postQueryRequest.getOrTags();
        Long userId = postQueryRequest.getUserId();
        long current = postQueryRequest.getCurrent();
        long pageSize = postQueryRequest.getPageSize();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //查询有效数据
        boolQuery.filter(QueryBuilders.termQuery("isDelete", 0));
        //根据id查询
        if (id != null) {
            boolQuery.must(QueryBuilders.termQuery("id", id));
        }
        //必须包含的标签
        if (CollectionUtils.isNotEmpty(tags)) {
            tags.forEach(tag -> {
                boolQuery.must(QueryBuilders.termQuery("tags", tag));
            });
        }
        //根据标题查询
        if (StringUtils.isNotBlank(title)) {
            boolQuery.must(QueryBuilders.matchQuery("title", title));
        }
        //根据内容查询
        if (StringUtils.isNotBlank(content)) {
            boolQuery.must(QueryBuilders.matchQuery("content", content));
        }
        //排除某个文章的id
        if (notId != null) {
            boolQuery.filter(QueryBuilders.termQuery("id", notId));
        }
        //根据搜索关键字查询
        if (StringUtils.isNotBlank(searchText)) {
            boolQuery.should(QueryBuilders.matchQuery("title", searchText));
            boolQuery.should(QueryBuilders.matchQuery("content", searchText));
            boolQuery.minimumShouldMatch("1");
        }
        //包含某个特定的标签即可
        if (CollectionUtils.isNotEmpty(orTags)) {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            orTags.forEach(orTag -> {
                boolQueryBuilder.should(QueryBuilders.termQuery("tags", orTag));
            });
            boolQueryBuilder.minimumShouldMatch("1");
            boolQuery.should(boolQueryBuilder);
        }
        //排除帖子主人
        if (userId != null) {
            boolQuery.filter(QueryBuilders.termQuery("userId", userId));
        }
        //设置分页

        //聚合条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery).build();
        SearchHits<PostEsDTO> search = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);

        System.out.println("search = " + search);


        //拿查询结果去数据库获取点赞数与收藏数

        //数据库中是否存在有效数据
        return null;
    }
}




