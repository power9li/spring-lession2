package com.power.learn.utils;

import com.power.spring.bean.User;
import com.power.spring.dao.UserDao;
import com.power.spring.dao.impl.UserDaoByFile;
import org.junit.Test;

import java.util.List;

/**
 * Created by shenli on 2017/1/1.
 */
public class TestUserDao {

    private UserDao userDao = new UserDaoByFile();

    @Test
    public void testCreateUser(){
        User u = new User();
        u.setUserName("李四");
        u.setEnabled(true);
        u.setPassword("12345");
        userDao.createUser(u);
    }

    @Test
    public void testLoadByUser(){
        User u = userDao.loadUserByNamePasswd("李四", "12345");
        System.out.println("u = " + u);
        System.out.println(u.getRegDate().getTime());
    }

    @Test
    public void testDisableUser(){
        User u = userDao.loadUserByNamePasswd("李四", "12345");
        boolean bl = userDao.disableUser(u.getUserId());
        System.out.println("bl = " + bl);
    }

    @Test
    public void testQueryUser(){
        List<User> list = userDao.queryUser("李", true);
        System.out.println("list.size() = " + list.size());
        for (User u : list) {
            System.out.println("u = " + u);
        }
    }

    @Test
    public void testDelete(){
        User u = userDao.loadUserByNamePasswd("李四", "12345");
        userDao.deleteUser(u.getUserId());
    }
}
