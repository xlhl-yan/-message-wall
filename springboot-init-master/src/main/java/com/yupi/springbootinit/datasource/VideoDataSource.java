package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * VideoDataSource
 *
 * @author xlhl
 * @version 1.0
 * @description 分页搜素视频数据源
 */
@Component
@Slf4j
public class VideoDataSource implements TypeDataSource<Object> {


    @Override
    public Page<Object> doSearch(String searchText, Long pageNum, Long pageSize) {
        return null;
    }
}
