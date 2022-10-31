package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    private static String driver="com.mysql.cj.jdbc.Driver";
    private static String url="jdbc:mysql://localhost:3306/nkuser0?autoReconnect=true&serverTimezone=UTC&useSSL=true&useUnicode=true&characterEncoding=utf-8";
    private static String user="root";
    private static String password="jinli666";

    public static Connection getConnection() {
        // 创建连接（通过反射机制）
        Connection connection=null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;

    }

}
