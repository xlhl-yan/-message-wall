package com.yupi.springbootinit.constant;

/**
 * 通用常量
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * 搜索图片分类缓存
     */
    String SEARCH_TYPE_KEY = "search_type_cache_key";

    /**
     * redis过期时间
     */
    Integer REDIS_KEY_TTL = 1000 * 60 * 60 * 24;
}
