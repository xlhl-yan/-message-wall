package com.yupi.springbootinit.esdao;

import com.yupi.springbootinit.model.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * ES 操作 帖子
 *
 * @author xlhl
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {

    /**
     * 根据创建人查询帖子信息
     *
     * @param userId
     * @return
     */
    List<PostEsDTO> findByUserId(Long userId);

    /**
     * 根据标签查询帖子
     *
     * @param title
     * @return
     */
    List<PostEsDTO> findByTitle(String title);
}