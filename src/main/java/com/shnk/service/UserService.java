package com.shnk.service;


import com.shnk.entity.*;

import java.util.List;

public interface UserService {
    /**
     * 注册用户
     * @param user
     */
    public void registUser(User user);

    /**
     * 登录
     * @param user
     * @return 如果返回null，说明登录失败，返回有值，是登录成功
     */
    public User login(User user);

    /**
     * 检查 用户名是否可用
     * @param username
     * @return 返回true表示用户名已存在，返回false表示用户名可用
     */
    public boolean existsUsername(String username);

    List<UserPOVO> getUser(String name, Integer departmentId, Integer companyId);

    List<LoginLogPOVO> getLoginLog(String companyName, String departmentName, String name, String username, String start, String end);

    int addUser(User user, String username);

    int deleteUser(String idStr, User user);

    int deleteLoginLog(String idStr);

    List<LoginLogCount> getLoginLogCount(String companyName, String departmentName, String name, String username, String start, String end);

    int modifyUser(User user, String username);

    List<OperationInfo> getOperationInfo(String tableName, String type, String operator, String start, String end);

    String getLoginLogByUserId(int userId);
}
