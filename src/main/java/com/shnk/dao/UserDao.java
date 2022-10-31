package com.shnk.dao;


import com.shnk.entity.*;

import java.util.List;

public interface UserDao {

    /*
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 如果返回null,说明没有这个用户。
     */
    public User queryUserByUsername(String username);

    /*
     * 根据 用户名和密码查询用户信息
     * @param username
     * @param password
     * @return 如果返回null,说明用户名或密码错误,反之亦然
     */
    public User queryUserByUsernameAndPassword(String username, String password);

    /*
     * 保存用户信息
     * @param user
     * @return 返回-1表示操作失败，其他是sql语句影响的行数
     */
    public int saveUser(User user);


    List<UserPOVO> getUser(String name, Integer departmentId, Integer companyId);

    int addUser(User user, String username);

    int deleteUser(String idStr, User user);
    List<LoginLogPOVO> getLoginLog(String companyName, String departmentName, String name, String username, String start, String end);

    int deleteLoginLog(String idStr);

    //传入5个参数
    List<LoginLogCount> getLoginLogCount(String companyName, String departmentName, String name, String username, String start, String end);

    int modifyUser(User user, String username);

    List<OperationInfo> getOperationInfo(String tableName, String type, String operator, String start, String end);

    String getLoginLogByUserId(int userId);
}
