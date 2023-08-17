package com.yupi.springbootinit.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Picture
 *
 * @author xlhl
 * @version 1.0
 * @description 图片返回包装实体类
 */
@Data
public class Picture implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;
    /**
     * 图片链接
     */
    private String url;
}
