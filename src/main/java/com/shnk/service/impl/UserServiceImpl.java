package com.shnk.service.impl;


import com.shnk.dao.impl.UserDaoImpl;
import com.shnk.entity.*;
import com.shnk.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDaoImpl userDao = new UserDaoImpl();

    @Override
    public void registUser(User user) {
        userDao.saveUser(user);
    }

    @Override
    public User login(User user) {
        return userDao.queryUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Override
    public boolean existsUsername(String username) {

        if (userDao.queryUserByUsername(username) == null) {
           // 等于null,说明没查到，没查到表示可用
           return false;
        }
        return true;

    }

    @Override
    public List<UserPOVO> getUser(String name, Integer departmentId, Integer companyId) {
        return userDao.getUser(name,departmentId,companyId);
    }

    @Override
    public List<LoginLogPOVO> getLoginLog(String companyName, String departmentName, String name, String username, String start, String end) {
        return userDao.getLoginLog(companyName,departmentName,name,username,start,end);
    }

    @Override
    public int addUser(User user,String username) {
        return userDao.addUser(user,username);
    }

    @Override
    public int deleteUser(String idStr,User user) {
        return userDao.deleteUser(idStr,user);
    }

    @Override
    public int deleteLoginLog(String idStr) {
        return userDao.deleteLoginLog(idStr);
    }

    @Override
    public List<LoginLogCount> getLoginLogCount(String companyName, String departmentName, String name, String username, String start, String end) {
        return userDao.getLoginLogCount(companyName,departmentName,name,username,start,end);
    }

    @Override
    public int modifyUser(User user,String username) {
        return userDao.modifyUser(user,username);
    }

    @Override
    public List<OperationInfo> getOperationInfo(String tableName, String type, String operator, String start, String end) {
        return userDao.getOperationInfo(tableName,type,operator,start,end);
    }

    @Override
    public String getLoginLogByUserId(int userId) {
        return userDao.getLoginLogByUserId(userId);
    }

}
