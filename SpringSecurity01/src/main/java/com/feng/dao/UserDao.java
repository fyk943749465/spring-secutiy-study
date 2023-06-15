package com.feng.dao;

import com.feng.entity.Role;
import com.feng.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserDao {
    // 提供根据用户名返回用户的方法
    User loadUserByUsername(String username);

    // 根据用户的id 查询角色
    List<Role> getRolesByUid(Integer uid);

}
