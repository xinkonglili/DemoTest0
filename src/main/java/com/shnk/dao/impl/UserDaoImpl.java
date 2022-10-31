package com.shnk.dao.impl;


import com.alibaba.fastjson.JSONObject;
import com.shnk.dao.UserDao;
import com.shnk.entity.*;
import com.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl  implements UserDao {
    @Override
    public User queryUserByUsername(String username) {
        return null;
    }

    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        User user=null;
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from user_t where username = ? and password = ?";
        try {
            preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                user=new User();
                user.setUserId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setUsername(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                user.setStatus(resultSet.getInt(9));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public int saveUser(User user) {
        return 0;
    }

    //获取用户列表
    @Override
    public List<UserPOVO> getUser(String name, Integer departmentId, Integer companyId) {
        List<UserPOVO> userPOVOList = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "";
        if (departmentId == -1) {
            if (companyId == -1) {
                sql = "select a.*,b.company_name,c.department_name,b.company_id " +
                        "from  user_t a, company_t b,department_t c where a.department_id=c.department_id and c.company_id=b.company_id and a.name like " + " '%" + name + "%'";
            } else {
                sql = "select a.*,b.company_name,c.department_name,b.company_id " +
                        "from  user_t a, company_t b,department_t c where a.department_id=c.department_id and c.company_id=b.company_id and b.company_id=" + companyId + " and a.name like '%" + name + "%'";
            }
        } else {
            sql = "select a.*,b.company_name,c.department_name,b.company_id " +
                    "from  user_t a, company_t b,department_t c " +
                    "where a.department_id=c.department_id and c.company_id=b.company_id and a.department_id="
                    + departmentId + " and b.company_id=" + companyId + " and a.name like '%" + name + "%'";
        }
        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserPOVO userPoVO = new UserPOVO();
                userPoVO.setUserId(resultSet.getInt(1));
                userPoVO.setName(resultSet.getString(2));
                userPoVO.setUsername(resultSet.getString(3));
                userPoVO.setPassword(resultSet.getString(4));
                userPoVO.setDepartmentId(resultSet.getInt(5));
                userPoVO.setCreateTime(resultSet.getString(6));
                userPoVO.setEmail(resultSet.getString(7));
                userPoVO.setImages(resultSet.getString(8));
                userPoVO.setStatus(resultSet.getInt(9));
                userPoVO.setCompanyName(resultSet.getString(10));
                userPoVO.setDepartmentName(resultSet.getString(11));
                userPoVO.setCompanyId(resultSet.getInt(12));
                userPOVOList.add(userPoVO);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return userPOVOList;
    }

    @Override
    public int addUser(User user,String username) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        int result = 0;
        String sql = "insert into user_t(name,username,password,email,status,department_id,images) values(?,?,?,?,?,?,?)";
        String sql1="select * from user_t t where not exists (select 1 from user_t where user_id > t.user_id)";
        try {
            preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getUsername());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setString(4,user.getEmail());
            preparedStatement.setInt(5,user.getStatus());
            preparedStatement.setInt(6,user.getDepartmentId());
            preparedStatement.setString(7,user.getImages());
            result=preparedStatement.executeUpdate();
            //添加日志
            preparedStatement=conn.prepareStatement(sql1);
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            user.setUserId(resultSet.getInt(1));
            //保存到操作日志表
            OperationInfo operationInfo=new OperationInfo("user_t","空", JSONObject.toJSONString(user),username, "新增","操作成功");
            preparedStatement=conn.prepareStatement("insert into operationinfo_t(table_name ,type ,content_before,content_after,operator,result,time) values (?,?,?,?,?,?,NOW())");
            preparedStatement.setString(1,operationInfo.getTableName());
            preparedStatement.setString(2,operationInfo.getType());
            preparedStatement.setString(3,operationInfo.getContentBefore());
            preparedStatement.setString(4,operationInfo.getContentAfter());
            preparedStatement.setString(5,operationInfo.getOperator());
            preparedStatement.setString(6,operationInfo.getResult());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;

    }

    @Override
    public int deleteUser(String idStr,User user) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        int result = 0;
        //联动操作表里面的记录
        String sql="delete from user_t where user_id in ( "+idStr+" )";
        try {
            //1、先把用户的所有信息查出来
            preparedStatement=conn.prepareStatement("select * from user_t where user_id in ("+idStr+")");
            ResultSet resultSet=preparedStatement.executeQuery();
            List<User> userList=new ArrayList<>();
            while (resultSet.next()){
                User user1=new User();
                user1.setUserId(resultSet.getInt(1));
                user1.setName(resultSet.getString(2));
                user1.setUsername(resultSet.getString(3));
                user1.setPassword(resultSet.getString(4));
                user1.setDepartmentId(resultSet.getInt(5));
                user1.setCreateTime(resultSet.getString(6));
                user1.setEmail(resultSet.getString(7));
                user1.setImages(resultSet.getString(8));
                user1.setStatus(resultSet.getInt(9));
                userList.add(user1);
            }

            //2、保存到操作日志，直接把userlist序列化进行比对
            OperationInfo operationInfo=new OperationInfo("user_t", JSONObject.toJSONString(userList),"空",user.getUsername() , "删除","操作成功");
            preparedStatement=conn.prepareStatement("insert into operationinfo_t(table_name ,type ,content_before,content_after,operator,result,time) values (?,?,?,?,?,?,NOW())");
            preparedStatement.setString(1,operationInfo.getTableName());
            preparedStatement.setString(2,operationInfo.getType());
            preparedStatement.setString(3,operationInfo.getContentBefore());
            preparedStatement.setString(4,operationInfo.getContentAfter());
            preparedStatement.setString(5,operationInfo.getOperator());
            preparedStatement.setString(6,operationInfo.getResult());
            preparedStatement.executeUpdate();
            //执行删除操作
            preparedStatement = conn.prepareStatement(sql);
            result = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public List<LoginLogPOVO> getLoginLog(String companyName,String departmentName,String name,String username,String start, String end) {
        //新开一个实体类用来保存用户的登录日志需要的字段
        List<LoginLogPOVO> loginLogPOVOList = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql="select a.id,a.login_time,a.exit_time,b.name,b.username,c.company_name,d.department_name  from loginlog_t a,user_t b,company_t c,department_t d " +
                "where a.user_id=b.user_id and b.department_id=d.department_id and d.company_id=c.company_id " +
                "and c.company_name like '%"+companyName+"%' and d.department_name like '%"+departmentName+"%' and b.name like '%"+name+"%' and b.username like '%"+username+"%'";
        if(start.equals("")){
            if(end.equals("")){
                sql=sql+"order by a.id desc";
            }else{
                sql=sql+" and a.login_time< '"+end+"'"+"order by a.id desc";
            }
            //分页处理
        }else{
            if(end.equals("")){
                sql=sql+" and a.login_time> '"+start+"'"+"order by a.id desc";
            }else{
                sql=sql+" and a.login_time > '"+start+"' and login_time < '"+end+"'"+"order by a.id desc";
            }
        }
        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LoginLogPOVO loginLogPOVO=new LoginLogPOVO();
                loginLogPOVO.setId(resultSet.getInt(1));
                loginLogPOVO.setLoginTime(resultSet.getString(2));
                loginLogPOVO.setExitTime(resultSet.getString(3));
                loginLogPOVO.setName(resultSet.getString(4));
                loginLogPOVO.setUsername(resultSet.getString(5));
                loginLogPOVO.setCompanyName(resultSet.getString(6));
                loginLogPOVO.setDepartmentName(resultSet.getString(7));
                loginLogPOVOList.add(loginLogPOVO);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return loginLogPOVOList;

    }

    //删除登录日志
    @Override
    public int deleteLoginLog(String idStr) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        int result=0;
        String sql="delete from loginlog_t where id in ("+idStr+")";
        try {
            preparedStatement=conn.prepareStatement(sql);
            result= preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }

    //
    @Override
    public List<LoginLogCount> getLoginLogCount(String companyName, String departmentName, String name, String username, String start, String end) {
        List<LoginLogCount> loginLogCountList = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //四表连接查询loginlog_t ,user_t ,company_t ,department_t
        String sql="select b.name,b.username,c.company_name,d.department_name,count(*) num from loginlog_t a,user_t b,company_t c,department_t d " +
                "where a.user_id=b.user_id and b.department_id=d.department_id and d.company_id=c.company_id " +
                "and c.company_name like '%"+companyName+"%' and d.department_name like '%"+departmentName+"%' and b.name like '%"+name+"%' and b.username like '%"+username+"%'";
        if(start.equals("")){
            if(end.equals("")){
                sql=sql+" group by b.name,b.username,c.company_name,d.department_name order by num desc ";
            }else{
                sql=sql+" and a.login_time< '"+end+"'"+" group by b.name,b.username,c.company_name,d.department_name order by num desc";
            }
        }else{
            if(end.equals("")){
                sql=sql+" and a.login_time> '"+start+"'"+" group by b.name,b.username,c.company_name,d.department_name order by num desc";
            }else{
                sql=sql+" and a.login_time > '"+start+"' and login_time < '"+end+"'"+" group by b.name,b.username,c.company_name,d.department_name order by num desc ";
            }
        }
        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            System.out.println("统计sql---"+sql.toString());
            while (resultSet.next()) {
                LoginLogCount loginLogCount=new LoginLogCount();
                loginLogCount.setName(resultSet.getString(1));
                loginLogCount.setUsername(resultSet.getString(2));
                loginLogCount.setCompanyName(resultSet.getString(3));
                loginLogCount.setDepartmentName(resultSet.getString(4));
                loginLogCount.setCount(resultSet.getInt(5));
                loginLogCountList.add(loginLogCount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return loginLogCountList;
    }

    @Override
    public int modifyUser(User user,String username) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        int result = 0;
        String sql="update user_t set name=?,username=?,password=?,department_id=?,email=?,images=?,status=? where user_id=?";
        PreparedStatement preparedStatement1=null;
        User user1=new User();
        try {
            //修改前
            preparedStatement=conn.prepareStatement("select * from user_t where user_id ="+user.getUserId());
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                user1.setUserId(resultSet.getInt(1));
                user1.setName(resultSet.getString(2));
                user1.setUsername(resultSet.getString(3));
                user1.setPassword(resultSet.getString(4));
                user1.setDepartmentId(resultSet.getInt(5));
                user1.setCreateTime(resultSet.getString(6));
                user1.setEmail(resultSet.getString(7));
                user1.setImages(resultSet.getString(8));
                user1.setStatus(resultSet.getInt(9));
            }
            preparedStatement=conn.prepareStatement("insert into operationinfo_t(table_name ,type ,content_before,operator,time) values (?,?,?,?,Now())");
            preparedStatement.setString(1,"user_t");
            preparedStatement.setString(2,"修改");
            preparedStatement.setString(3,JSONObject.toJSONString(user1));
            preparedStatement.setString(4,username);
            preparedStatement.executeUpdate();

            preparedStatement=conn.prepareStatement("select id from operationinfo_t t where not exists (select 1 from operationinfo_t where id > t.id)");
            resultSet =preparedStatement.executeQuery();
            resultSet.next();
            int id=resultSet.getInt(1);

            //修改
            preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getUsername());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setInt(4,user.getDepartmentId());
            preparedStatement.setString(5,user.getEmail());
            preparedStatement.setString(6,user.getImages());
            preparedStatement.setInt(7,user.getStatus());
            preparedStatement.setInt(8,user.getUserId());
            result=preparedStatement.executeUpdate();

            //修改后
            preparedStatement=conn.prepareStatement("update operationinfo_t set content_after=?,result=? where id=? ");
            preparedStatement.setString(1,JSONObject.toJSONString(user));
            preparedStatement.setString(2,"操作成功");
            preparedStatement.setInt(3,id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public List<OperationInfo> getOperationInfo(String tableName, String type, String operator, String start, String end) {
        List<OperationInfo> operationInfoList=new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String sql="select * from operationinfo_t where table_name like '%"+tableName+"%' and type like '%"+type+"%' and operator like '%"+operator+"%' ";
        if(start.equals("")){
            if(end.equals("")){
                sql=sql;
            }else{
                sql=sql+" and time< '"+end+"'";
            }
        }else{
            if(end.equals("")){
                sql=sql+" and time> '"+start+"'";
            }else{
                sql=sql+" and time > '"+start+"' and time < '"+end+"'";
            }
        }
        try {
            preparedStatement=conn.prepareStatement(sql);
            System.out.println("sql------------>"+sql.toString());
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                OperationInfo operationInfo=new OperationInfo();
                operationInfo.setId(resultSet.getInt(1));
                operationInfo.setTableName(resultSet.getString(2));
                operationInfo.setContentBefore(resultSet.getString(3));
                operationInfo.setContentAfter(resultSet.getString(4));
                operationInfo.setTime(resultSet.getString(5));
                operationInfo.setOperator(resultSet.getString(6));
                operationInfo.setType(resultSet.getString(7));
                operationInfo.setResult(resultSet.getString(8));
                operationInfoList.add(operationInfo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return operationInfoList;
    }

    @Override
    public String getLoginLogByUserId(int userId) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String oldLoginTIme="";
        String sql="select * from loginlog_t t where user_id=? and not exists (select 1 from loginlog_t where id > t.id and user_id=?)";
        try {
            preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,userId);
            resultSet=preparedStatement.executeQuery();
            resultSet.next();
            oldLoginTIme=resultSet.getString(4);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return oldLoginTIme;
    }
}
