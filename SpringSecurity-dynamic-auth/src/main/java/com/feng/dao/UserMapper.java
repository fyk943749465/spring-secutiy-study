package com.feng.dao;

import com.feng.entity.Role;
import com.feng.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<Role> getUserRoleByUid(Integer uid);

    User loadUserByUsername(String username);
}
