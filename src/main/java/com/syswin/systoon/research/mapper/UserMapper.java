package com.syswin.systoon.research.mapper;

import com.syswin.systoon.research.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by rocky on 2018/3/13.
 */
@Mapper
public interface UserMapper {

    User selectByPK(Integer id);

    User selectByName(String name);

    int insert(User user);

    List<User> queryAllUser();
}
