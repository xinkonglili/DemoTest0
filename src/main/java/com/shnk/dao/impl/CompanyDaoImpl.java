package com.shnk.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.shnk.dao.CompanyDao;
import com.shnk.entity.Company;
import com.shnk.entity.OperationInfo;
import com.shnk.entity.User;
import com.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {

    @Override
    public List<Company> getAllCompany() {
        List<Company> companyList = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        String sql = "select * from company_t where status=1";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Company company = new Company();
                company.setCompanyId(resultSet.getInt(1));
                company.setCompanyName(resultSet.getString(2));
                company.setAddress(resultSet.getString(3));
                company.setPhone(resultSet.getString(4));
                company.setFax(resultSet.getString(5));
                company.setStatus(resultSet.getInt(6));
                companyList.add(company);
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
        return companyList;

    }

    //按Id删除单位信息
    @Override
    public int deleteCompanyById(String idStr,User user) {
        //保存 要删除 的单位
        List<Company> companyList=new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        String sql1="select a.*,b.* from company_t a,department_t b where a.company_id in ("+idStr+")"+" and a.company_id=b.company_id";
        PreparedStatement preparedStatement1=null;
        ResultSet resultSet=null;
        int result = 0;
        try {
            preparedStatement1=conn.prepareStatement(sql1);
            resultSet=preparedStatement1.executeQuery();
            if(resultSet.next()){
                return result;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String sql = "delete from company_t where company_id in ("+idStr+")";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement=conn.prepareStatement("select * from company_t where company_id in ("+idStr+")");
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Company company=new Company();
                company.setCompanyId(resultSet.getInt(1));
                company.setCompanyName(resultSet.getString(2));
                company.setAddress(resultSet.getString(3));
                company.setPhone(resultSet.getString(4));
                company.setFax(resultSet.getString(5));
                company.setStatus(resultSet.getInt(6));
                companyList.add(company);
            }
            OperationInfo operationInfo=new OperationInfo("company_t",JSONObject.toJSONString(companyList),"空",user.getUsername() , "删除","操作成功");
            preparedStatement=conn.prepareStatement("insert into operationinfo_t(table_name ,type ,content_before,content_after,operator,result,time) values (?,?,?,?,?,?,NOW())");
            preparedStatement.setString(1,operationInfo.getTableName());
            preparedStatement.setString(2,operationInfo.getType());
            preparedStatement.setString(3,operationInfo.getContentBefore());
            preparedStatement.setString(4,operationInfo.getContentAfter());
            preparedStatement.setString(5,operationInfo.getOperator());
            preparedStatement.setString(6,operationInfo.getResult());
            preparedStatement.executeUpdate();
            preparedStatement = conn.prepareStatement(sql);
            result = preparedStatement.executeUpdate();
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
        return result;


    }
    //查询单位信息
    public List<Company> getCompanyByName(String companyName) {
        String sql;
        if(companyName==null||companyName.equals("")){
            sql = "select * from company_t";
        }else{
            sql = "select * from company_t where company_name like '%"+companyName+"%'";
        }
        List<Company> companyList = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Company company = new Company();
                company.setCompanyId(resultSet.getInt(1));
                company.setCompanyName(resultSet.getString(2));
                company.setAddress(resultSet.getString(3));
                company.setPhone(resultSet.getString(4));
                company.setFax(resultSet.getString(5));
                company.setStatus(resultSet.getInt(6));
                companyList.add(company);
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
        return companyList;
    }
    //添加单位信息
    @Override
    public void addCompany(Company company, User user) {
        Connection conn=JDBCUtil.getConnection();
        String sql="insert into company_t(company_name,address,phone,fax,status) values(?,?,?,?,?)";
        String sql1="select * from company_t t where not exists (select 1 from company_t where company_id > t.company_id)";
        PreparedStatement preparedStatement1=null;
        ResultSet resultSet=null;
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,company.getCompanyName());
            preparedStatement.setString(2,company.getAddress());
            preparedStatement.setString(3,company.getPhone());
            preparedStatement.setString(4,company.getFax());
            preparedStatement.setInt(5,company.getStatus());
            preparedStatement.executeUpdate();
            preparedStatement1=conn.prepareStatement(sql1);
            resultSet=preparedStatement1.executeQuery();
            resultSet.next();
            company.setCompanyId(resultSet.getInt(1));
            OperationInfo operationInfo=new OperationInfo("company_t","空", JSONObject.toJSONString(company),user.getUsername(), "新增","操作成功");
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
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    //通过公司的id，查找公司的详细信息
    @Override
    public List<Company> getCompanyById(Integer id) {
        List<Company> companyList=new ArrayList<>();
        Connection conn=JDBCUtil.getConnection();
        String sql="select * from company_t where company_id=?";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()) {
                Company company = new Company();
                company.setCompanyId(resultSet.getInt(1));
                company.setCompanyName(resultSet.getString(2));
                company.setAddress(resultSet.getString(3));
                company.setPhone(resultSet.getString(4));
                company.setFax(resultSet.getString(5));
                company.setStatus(resultSet.getInt(6));
                companyList.add(company);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return companyList;
    }

    //修改公司
    @Override
    public int modifyCompany(Company company,User user) {
        Company company1=new Company();
        Connection conn=JDBCUtil.getConnection();
        //先把修改前的信息给记录下来，再执行这条sql
        String sql="update company_t set company_name=?,address=?,phone=?,fax=?,status=? where company_id=?";
        PreparedStatement preparedStatement=null;
        int result = 0;
        try {
            //修改前
            preparedStatement=conn.prepareStatement("select * from company_t where company_id ="+company.getCompanyId());
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                company1.setCompanyId(resultSet.getInt(1));
                company1.setCompanyName(resultSet.getString(2));
                company1.setAddress(resultSet.getString(3));
                company1.setPhone(resultSet.getString(4));
                company1.setFax(resultSet.getString(5));
                company1.setStatus(resultSet.getInt(6));
            }
            //插入操作日志
            preparedStatement=conn.prepareStatement("insert into operationinfo_t(table_name ,type ,content_before,operator) values (?,?,?,?)");
            preparedStatement.setString(1,"company_t");
            preparedStatement.setString(2,"修改");
            preparedStatement.setString(3,JSONObject.toJSONString(company1));
            preparedStatement.setString(4,user.getUsername());
            preparedStatement.executeUpdate();

            //保持是该用户的最新记录
            preparedStatement=conn.prepareStatement("select id from operationinfo_t t where not exists (select 1 from operationinfo_t where id > t.id)");
            resultSet =preparedStatement.executeQuery();
            resultSet.next();
            int id=resultSet.getInt(1);
            System.out.println(id);
            //开始修改
            preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,company.getCompanyName());
            preparedStatement.setString(2,company.getAddress());
            preparedStatement.setString(3,company.getPhone());
            preparedStatement.setString(4,company.getFax());
            preparedStatement.setInt(5,company.getStatus());
            preparedStatement.setInt(6,company.getCompanyId());
            result=preparedStatement.executeUpdate();
            //修改后
            preparedStatement=conn.prepareStatement("update operationinfo_t set content_after=?,result=? where id=? ");
            preparedStatement.setString(1,JSONObject.toJSONString(company));
            preparedStatement.setString(2,"操作成功");
            preparedStatement.setInt(3,id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }

    //通过公司的id，查找公司的名称
    @Override
    public String getCompanyNameById(int companyId) {
        Connection conn=JDBCUtil.getConnection();
        String sql="select company_name from company_t where company_id="+companyId;
        PreparedStatement preparedStatement=null;
        String companyName="";
        try {
            preparedStatement=conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            companyName=resultSet.getString(1);
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
        return companyName;
    }
}
