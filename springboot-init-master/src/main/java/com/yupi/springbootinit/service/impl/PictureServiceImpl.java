package com.yupi.springbootinit.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * PictureServiceImpl
 *
 * @author xlhl
 * @version 1.0
 * @description
 */
@Service
public class PictureServiceImpl implements PictureService {

    @Override
    public Page<Picture> searchPicture(String searchText, Long pageNum, Long pageSize) {
        Long current = (pageNum - 1) * pageSize;
        String url = String.format("https://cn.bing.com/images/search?q=%s&form=HDRSC2&first=%s", searchText, current);
        System.out.println("url = " + url);
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "无法获取到数据");
        }
        Elements elements = doc.select("div[class=iuscp isv]");
        ArrayList<Picture> pictures = new ArrayList<>();

        for (Element element : elements) {
            Picture picture = new Picture();
            //获取父标签
            Elements imgpt = element.select("div[class=imgpt]");
            //获取图片地址（m）
            Elements iusc = imgpt.select("a[class=iusc]");
            String m = iusc.get(0).attr("m");

            Map<String, String> map = JSON.parseObject(m, Map.class);
            String murl = map.get("murl");
            //获取标题
            String title = map.get("t");
            picture.setUrl(murl);
            picture.setTitle(title);
            pictures.add(picture);
            if (pictures.size() >= pageSize) {
                break;
            }
        }
        Page<Picture> page = new Page<>();
        page.setRecords(pictures);
        return page;
    }
}
