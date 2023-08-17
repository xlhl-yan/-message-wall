package com.yupi.springbootinit;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * CrawlerTest
 *
 * @author xlhl
 * @version 1.0
 * @description 爬虫测试类
 */
@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;


    /**
     * jsoup测试
     */
    @Test
    void testJsoup() throws IOException {
        Random random = new Random();
        Integer current = random.nextInt(1000);
        String url = String.format("https://cn.bing.com/images/search?q=小黑子表情包&form=HDRSC2&first=%s", current);
        System.out.println("url = " + url);
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("div[class=iuscp isv]");
        ArrayList<Picture> pictures = new ArrayList<>();
        assert elements.size() != 0;
        for (Element element : elements) {
            assert element != null;
            Picture picture = new Picture();
            //获取父标签
            Elements imgpt = element.select("div[class=imgpt]");
            //获取图片地址（m）
            Elements iusc = imgpt.select("a[class=iusc]");
            String m = iusc.get(0).attr("m");

            Map<String, String> map = JSON.parseObject(m, Map.class);
//            System.out.println("m = " + m);
            String murl = map.get("murl");
            //获取标题
            String title = map.get("t");
            picture.setUrl(murl);
            picture.setTitle(title);
            System.out.println("murl = " + murl);
            System.out.println("title = " + title);
        }
        System.out.println("pictures = " + pictures);
    }

    /**
     * 拿不到数据，小问题，关键在Elasticsearch使用，不在爬虫
     */
    @Test
    void testFetchPassage() {
        String url = "https://www.code-nav.cn/learn/passage";
        HashMap<String, Object> paramMap = new HashMap<>();
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
        assert flag;
    }
}
