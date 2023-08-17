package com.yupi.springbootinit.job.once;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获得初始文章数据
 *
 * @author xlhl
 */
@Slf4j
public class FetchInitPostList implements CommandLineRunner {
    // todo 添加 @Component 注解开启任务 CommandLineRunner 单次执行，在项目启动时执行run方法

    @Resource
    private PostService postService;


    @Override
    public void run(String... args) {
        String url = "https://www.code-nav.cn/learn/passage";
        HashMap<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("category", "文章");
        paramMap.put("pageSize", "1");
        paramMap.put("reviewStatus", "1");
        paramMap.put("sortField", "createTime");
        paramMap.put("sortOrder", "descend");
        String result = HttpUtil.post(url, paramMap);
        assert StringUtils.isNotBlank(result);

        Map<String, Object> map = JSONObject.parseObject(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        ArrayList<Post> posts = new ArrayList<>();

        for (Object record : records) {
            JSONObject object = (JSONObject) record;
            Post post = new Post();
            post.setTitle(object.getString("title"));
            post.setContent(object.getString("content"));
            JSONArray tags = (JSONArray) object.get("tags");
            List<String> tagList = tags.toList(String.class);

            post.setTags(JSON.toJSONString(tagList));
            post.setUserId(1L);
        }
        boolean flag = postService.saveBatch(posts);
        if (flag) {
            log.info("FetchInitPostList 获取帖子列表成功,总计：{}条", posts.size());
        } else {
            log.error("FetchInitPostList 获取帖子列表失败,总计：{}条", posts.size());
        }

    }
}
