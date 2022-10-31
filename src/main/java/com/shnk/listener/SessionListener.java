package com.shnk.listener;
import javax.servlet.http.*;
import java.net.URLEncoder;

//监听session创建，把session存入cookie中，2小时有效
public class SessionListener extends HttpServlet implements HttpSessionListener {
    public static String sessionId;//
    HttpServletResponse response;
    //监听session的创建,synchronized 防并发bug
    @Override
    public synchronized void sessionCreated(HttpSessionEvent event) {
        try{  //把sessionId记录在浏览器
            sessionId=event.getSession().getId();
            System.out.println("create:"+sessionId);
            Cookie c = new Cookie("JSESSIONID", URLEncoder.encode(sessionId, "utf-8"));
            c.setPath("/");
            c.setMaxAge( 2*60*60);
            response.addCookie(c);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
