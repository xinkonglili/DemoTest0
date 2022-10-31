package com.shnk.entity;

import com.utils.JDBCUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements HttpSessionBindingListener {
    private int userId;
    private String name;
    private String username;
    private String password;
    private int departmentId;
    private String createTime;
    private String email;
    private String images;
    private int status;
    private String sessionId;
    public User(String username,String password){
        this.username=username;
        this.password=password;
    }

    ///
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        HttpSession session = event.getSession();
        sessionId=session.getId();
        String username= (String) session.getAttribute("username");
        Integer userId= (Integer)session.getAttribute("userId");
        Connection conn = JDBCUtil.getConnection();
        String sql = "insert into loginlog_t(session_id,user_id,login_time) values(?,?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,sessionId);
            preparedStatement.setInt(2,userId);
            preparedStatement.setString(3,getCurrDatetimeStr());
            preparedStatement.executeUpdate();
        }catch (SQLException throwables) {
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
    }


    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        Connection conn = JDBCUtil.getConnection();
        String sql = "update loginlog_t set exit_time=? where session_id=?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,getCurrDatetimeStr());
            preparedStatement.setString(2,sessionId);
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
    }

    private static String getCurrDatetimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


}
