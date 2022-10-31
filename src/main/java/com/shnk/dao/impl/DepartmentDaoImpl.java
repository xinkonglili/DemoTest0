package com.shnk.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.shnk.dao.DepartmentDao;
import com.shnk.entity.*;
import com.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DepartmentDaoImpl implements DepartmentDao {
    //人員統計
    @Override
    public List<Department> getDepartmentByCompanyId(Integer companyId) {
        List<Department> departmentList=new ArrayList<>();
        Connection connection = JDBCUtil.getConnection();
        String sql="";
        if(companyId==-1){
            sql="select * from department_t and status=1";
        }else{
            sql="select * from department_t where company_id="+companyId+" and status=1";
        }
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Department department=new Department();
                department.setDepartmentId(resultSet.getInt(1));
                department.setDepartmentName(resultSet.getString(2));
                department.setStatus(resultSet.getInt(3));
                department.setCompanyId(resultSet.getInt(4));
                departmentList.add(department);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return departmentList;
    }

    //部门管理，直接根据部门id来进行查寻
    @Override
    public List<DepartmentPOVO> getDepartment(Integer companyId) {
        List<DepartmentPOVO> departmentPOVOList=new ArrayList<>();
        String  sql="select a.*,b.company_name from department_t a,company_t b where a.company_id=b.company_id and a.company_id="+companyId;
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                DepartmentPOVO departmentPOVO=new DepartmentPOVO();
                departmentPOVO.setDepartmentId(resultSet.getInt(1));
                departmentPOVO.setDepartmentName(resultSet.getString(2));
                departmentPOVO.setStatus(resultSet.getInt(4));
                departmentPOVO.setCompanyId(resultSet.getInt(3));
                departmentPOVO.setCompanyName(resultSet.getString(5));
                departmentPOVOList.add(departmentPOVO);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return departmentPOVOList;


    }

    //部门管理：添加部门
    @Override
    public Boolean addDepartment(Department department, User user) {
        String sql="insert into department_t(department_name,status,company_id) values(?,?,?)";
        String sql1="select * from department_t t where not exists (select 1 from department_t where department_id > t.department_id)";
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement=null;
        int result=0;
        try {

            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.setInt(2,department.getStatus());
            preparedStatement.setInt(3,department.getCompanyId());
            result=preparedStatement.executeUpdate();
            //添加日志
            preparedStatement=connection.prepareStatement(sql1);
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            department.setDepartmentId(resultSet.getInt(1));
            //添加部门日志
            OperationInfo operationInfo=new OperationInfo("department_t","空", JSONObject.toJSONString(department),user.getUsername(), "新增","操作成功");
            preparedStatement=connection.prepareStatement("insert into operationinfo_t(table_name ,type ,content_before,content_after,operator,result,time) values (?,?,?,?,?,?,NOW())");
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
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        if(result==0){
            return false;
        }
        return true;
    }
    //删除部门
    @Override
    public int deleteDepartment(String idStr,User user) {
        String sql="delete from department_t where department_id in ( "+idStr+" )";
        System.out.println(sql);
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement=null;
        int result=0;
        try {
            //删除前
            preparedStatement=connection.prepareStatement("select * from department_t where department_id in ("+idStr+")");
            ResultSet resultSet=preparedStatement.executeQuery();
            List<Department> departmentList=new ArrayList<>();
            while (resultSet.next()){
                Department department=new Department();
                department.setDepartmentId(resultSet.getInt(1));
                department.setDepartmentName(resultSet.getString(2));
                department.setStatus(resultSet.getInt(3));
                department.setCompanyId(resultSet.getInt(4));
                departmentList.add(department);
            }
            OperationInfo operationInfo=new OperationInfo("department_t", JSONObject.toJSONString(departmentList),"空",user.getUsername() , "删除","操作成功");
            preparedStatement=connection.prepareStatement("insert into operationinfo_t(table_name ,type ,content_before,content_after,operator,result) values (?,?,?,?,?,?)");
            preparedStatement.setString(1,operationInfo.getTableName());
            preparedStatement.setString(2,operationInfo.getType());
            preparedStatement.setString(3,operationInfo.getContentBefore());
            preparedStatement.setString(4,operationInfo.getContentAfter());
            preparedStatement.setString(5,operationInfo.getOperator());
            preparedStatement.setString(6,operationInfo.getResult());
            preparedStatement.executeUpdate();

            //执行删除
            preparedStatement=connection.prepareStatement(sql);
            result=preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }

    //部门管理：修改部门
    @Override
    public int modifyDepartment(Department department,User user) {
        Connection connection = JDBCUtil.getConnection();
        String sql="update department_t set department_name=? ,status=? where department_id=?";
        PreparedStatement preparedStatement=null;
        int result=0;
        Department department1=new Department();
        try {
            //修改前
            preparedStatement=connection.prepareStatement("select * from department_t where department_id ="+department.getDepartmentId());
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                department1.setDepartmentId(resultSet.getInt(1));
                department1.setDepartmentName(resultSet.getString(2));
                department1.setStatus(resultSet.getInt(3));
                department1.setCompanyId(resultSet.getInt(4));
            }
            preparedStatement=connection.prepareStatement("insert into operationinfo_t(table_name ,type ,content_before,operator) values (?,?,?,?)");
            preparedStatement.setString(1,"department_t");
            preparedStatement.setString(2,"修改");
            preparedStatement.setString(3,JSONObject.toJSONString(department1));
            preparedStatement.setString(4,user.getUsername());
            preparedStatement.executeUpdate();

            preparedStatement=connection.prepareStatement("select id from operationinfo_t t where not exists (select 1 from operationinfo_t where id > t.id)");
            resultSet =preparedStatement.executeQuery();
            resultSet.next();
            int id=resultSet.getInt(1);
            //修改
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.setInt(2,department.getStatus());
            preparedStatement.setInt(3,department.getDepartmentId());
            result=preparedStatement.executeUpdate();
            //修改后
            preparedStatement=connection.prepareStatement("update operationinfo_t set content_after=?,result=? where id=? ");
            preparedStatement.setString(1,JSONObject.toJSONString(department));
            preparedStatement.setString(2,"操作成功");
            preparedStatement.setInt(3,id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;

    }

    //人员统计，根据公司id和部门id查找该公司，该部门下的用户信息，并统计
    @Override
    public List<StaffCount> getStaffCount(Integer companyId, Integer departmentId) {
        List<StaffCount> staffCountList=new ArrayList<>();
        Connection connection = JDBCUtil.getConnection();
            String sql="select a.company_name,b.department_name,count(*) from company_t a,department_t b,user_t c " +
                    "where a.company_id=b.company_id and b.department_id=c.department_id" +
                    " and a.company_id="+companyId+" and b.department_id="+departmentId+"" +
                    " group by a.company_name,b.department_name order by a.company_name";

        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                StaffCount staffCount=new StaffCount();
                staffCount.setCompanyName(resultSet.getString(1));
                staffCount.setDepartmentName(resultSet.getString(2));
                staffCount.setCount(resultSet.getInt(3));
                staffCountList.add(staffCount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return staffCountList;
    }

    @Override
    public String getDepartmentNameById(int departmentId) {
        Connection conn=JDBCUtil.getConnection();
        String sql="select department_name from department_t where department_id="+departmentId;
        PreparedStatement preparedStatement=null;
        String departmentName="";
        try {
            preparedStatement=conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            departmentName=resultSet.getString(1);
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
        return departmentName;

    }
}
