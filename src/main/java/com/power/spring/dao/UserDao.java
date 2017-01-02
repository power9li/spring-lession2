package com.power.spring.dao;

import com.power.spring.bean.User;

import java.util.List;

/**
 * Created by shenli on 2017/1/1.
 */
public interface UserDao {

    boolean createUser(User user);

    boolean deleteUser(long userId);

    boolean disableUser(long userId);

    List<User> queryUser(String userNamePrex,boolean onlyValidUser);

    User loadUserByNamePasswd(String userName,String md5Passed);

}
