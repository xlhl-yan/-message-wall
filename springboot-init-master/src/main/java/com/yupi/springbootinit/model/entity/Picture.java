package com.yupi.springbootinit.model.entity;

import lombok.Data;

/**
 * Picture
 *
 * @author xlhl
 * @version 1.0
 * @description 图片返回包装实体类
 */
@Data
public class Picture {
    /**
     * 标题
     */
    private String title;
    /**
     * 图片链接
     */
    private String url;
}
