package com.power.spring.service;

import com.power.spring.bean.User;
import com.power.spring.bean.UserSession;

import java.util.List;

public interface UserService {

	public boolean createUser(User user);

	public boolean deleteUser(long userId);

	public boolean disableUser(long userId);

	public List<User> queryUsers(String userNamePrex, boolean onlyValidUser);
	
	/**
	 * 如果密码不对，返回的UserSession对象里sessionId为空，客户端可以依次判断，参照UserSession.isValid方法
	 * @param userName
	 * @param md5EncodedPassword
	 * @return
	 */
	public UserSession login(String userName, String md5EncodedPassword);
}
