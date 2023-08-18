package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 新接入数据源接口，新接入的数据源必须实现此接口
 * 适配器模式
 *
 * @author xlhl
 */
public interface TypeDataSource<T> {

    /**
     * 分页搜索
     *
     * @param searchText 关键字
     * @param pageNum    当前页
     * @param pageSize   当前行展示大小
     * @return 分页数据
     */
    Page<T> doSearch(String searchText, Long pageNum, Long pageSize);
}
