package com.feng.dao;

import com.feng.entity.Role;
import com.feng.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    User loadUserByUsername(String username);

    List<Role> getRolesByUid(Integer uid);

    Integer updatePassword(String username, String newPassword);
}
