package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xlhl
 */
@Component
@Slf4j
public class UserDataSource implements TypeDataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, Long pageNum, Long pageSize) {

        UserQueryRequest queryRequest = new UserQueryRequest();
        queryRequest.setUserName(searchText);
        queryRequest.setCurrent(pageNum);
        queryRequest.setPageSize(pageSize);

        return userService.listUser(queryRequest);
    }
}
