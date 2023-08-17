package com.yupi.springbootinit.model.dto.search;

import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * UnifySearchQueryRequest
 *
 * @author xlhl
 * @version 1.0
 * @description 统一搜索参数封装类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UnifySearchQueryRequest extends PageRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}
