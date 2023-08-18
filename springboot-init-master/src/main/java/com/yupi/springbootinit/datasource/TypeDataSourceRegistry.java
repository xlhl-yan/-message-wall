package com.yupi.springbootinit.datasource;

import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * TypeDataSourceRegistry
 * 注册模式
 *
 * @author xlhl
 * @version 1.0
 * @description 数据源注册
 */
@Component
@Slf4j
public class TypeDataSourceRegistry {
    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private PostDataSource postDataSource;

    private Map<String, TypeDataSource<?>> typeMap;

    /**
     * 项目启动时调用
     */
    @PostConstruct
    public void doInit() {
        typeMap = new HashMap(4) {{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
        }};
    }


    public TypeDataSource getDataSource(String type) {
        if (typeMap == null) {
            return null;
        }
        return typeMap.get(type);
    }
}
