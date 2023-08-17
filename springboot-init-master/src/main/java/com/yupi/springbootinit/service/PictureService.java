package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;

/**
 * 图片服务
 *
 * @author xlhl
 */
public interface PictureService {

    /**
     * 根据关键字查询图片信息
     *
     * @param searchText 关键字
     * @param pageNum    当前页数
     * @param pageSize   最大页展示
     * @return 图片信息合集
     */
    Page<Picture> searchPicture(String searchText, Long pageNum, Long pageSize);
}
