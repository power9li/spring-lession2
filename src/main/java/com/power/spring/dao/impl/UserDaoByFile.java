package com.power.spring.dao.impl;

import com.google.gson.Gson;
import com.power.spring.bean.User;
import com.power.spring.bean.UserSession;
import com.power.spring.dao.UserDao;
import com.power.spring.utils.HexUtils;
import com.power.spring.utils.MD5Utils;
import com.power.spring.utils.PropUtils;
import com.power.spring.utils.UserSessionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by shenli on 2017/1/1.
 */
public class UserDaoByFile implements UserDao{

    private static String FILE_PATH = PropUtils.getProp("user.file.path");
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static AtomicLong maxUserId = new AtomicLong(1);
    private Lock read = rwLock.readLock();
    private Lock write = rwLock.writeLock();
    private static final File maxIdFile = new File(FILE_PATH+"/maxid");

    static {

        try {
            if (!maxIdFile.exists()) {
                maxIdFile.createNewFile();
            }
            String idStr = FileUtils.readFileToString(maxIdFile);
            if (StringUtils.isNotEmpty(idStr)) {
                maxUserId.set(Long.parseLong(idStr));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean createUser(final User user) {

        boolean bl = writeOperation(() ->{

            //TODO: filter seam username
            long userId = maxUserId.incrementAndGet();
            user.setUserId(userId);
            user.setRegDate(new Date());
            String filePath = FILE_PATH+"/users/"+userId;
            File f = new File(filePath);
            if (!f.exists()) {
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdir();
                }
                f.createNewFile();
            }
            else{
                System.err.println("UserDaoByFile.createUser: file name exist.");
            }
            String userStr = new Gson().toJson(user);
            System.out.println("before write file userStr = " + userStr);
            FileUtils.writeStringToFile(f, userStr);

            //write UserId&&PasswdMD5 Index File
            String loginCheckKey = HexUtils.byte2hex(MD5Utils.getMD5(user.getUserName() + "&&" + user.getPassword()));
            FileUtils.writeStringToFile(new File(FILE_PATH + "/UserPass/" + loginCheckKey), String.valueOf(userId));

            //write maxIdFile
            FileUtils.writeStringToFile(maxIdFile, String.valueOf(userId));
            return true;
        });
        return bl;
    }

    @Override
    public boolean deleteUser(final long userId) {
        boolean bl = writeOperation(() ->{
            if (userId == 0L) {
                return false;
            }
            File uF = new File(FILE_PATH+"/users/"+userId);
            if (!uF.exists()) {
                System.err.println("user file ["+uF.getName()+"] not exist;");
                return false;
            }
            FileUtils.forceDelete(uF);
            //TODO :delete userSession if Exist
            return true;
        });
        return bl;
    }

    @Override
    public boolean disableUser(final long userId) {
        boolean bl = writeOperation(() -> {
            File f = new File(FILE_PATH + "/users/" + userId);
            if (!f.exists()) {
                System.err.println("file ["+f.getName()+"] not exists;");
                return false;
            }
            String json = FileUtils.readFileToString(f);
            User u = new Gson().fromJson(json, User.class);
            u.setEnabled(false);
            json = new Gson().toJson(u);
            FileUtils.writeStringToFile(f,json);
            return true;
        });
        return bl;
    }

    @Override
    public List<User> queryUser(final String userNamePrex, final boolean onlyValidUser) {
        List<User> users = writeOperation(() -> {
            File folder = new File(FILE_PATH+"/users/");
            List<User> users2 = new ArrayList<User>();
            File[] files = folder.listFiles();
            for (File f : files) {
                String uJson = FileUtils.readFileToString(f);
                User u = new Gson().fromJson(uJson, User.class);
                boolean passed = true;
                if (userNamePrex != null) {
                    passed = passed && u.getUserName().startsWith(userNamePrex);
                }
                if (passed && onlyValidUser) {
                    //验证是否是有效用户
                    UserSession session = UserSessionUtils.getSessionByUserId(u.getUserId());
                    if (session == null) {
                        passed = false;
                    }
                    else{
                        passed = session.isValid();
                    }
                }
                if(passed) {
                    users2.add(u);
                }
            }
            return users2;
        });
        return users;
    }

    @Override
    public User loadUserByNamePasswd(final String userName, final String md5Passed) {
        User u = readOperation(()-> {
            String loginCheckKey = HexUtils.byte2hex(MD5Utils.getMD5(userName + "&&" + md5Passed));
            File f = new File(FILE_PATH + "/UserPass/" + loginCheckKey);
            if (f.exists()) {
                String userId = FileUtils.readFileToString(f);
                File uf = new File(FILE_PATH + "/users/" + userId);
                if (uf.exists()) {
                    String json = FileUtils.readFileToString(uf);
                    User u2 = new Gson().fromJson(json, User.class);
                    return u2;
                }
            }
            return null;
        });
        return u;
    }

    public <T> T writeOperation(Callable<T> cab){
        T t = null;
        try {
            write.lock();
            t = cab.call();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            write.unlock();
        }
        return t;
    }

    public <T> T readOperation(Callable<T> cab){
        T t = null;
        try {
            read.lock();
            t = cab.call();
        } catch(Exception e){
            System.err.println(e.getMessage());
        } finally {
            read.unlock();
        }
        return t;
    }


}
