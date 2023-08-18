package com.yupi.springbootinit.model.vo;

import com.yupi.springbootinit.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索返回封装
 *
 * @author xlhl
 */
@Data
public class SearchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;

    /**
     * 通用数据源
     */
    private List<?> dataList;
}
