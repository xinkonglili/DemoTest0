package com.shnk.controller;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        BufferedImage image = new BufferedImage(100, 30, BufferedImage.TYPE_INT_BGR);
        //获取画笔
        Graphics g = image.getGraphics();
        Random r = new Random();
        //设置画笔颜色,需要传一个对象nextInt(),0~255之间的颜色随机
        g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
        //绘制矩形背景
        g.fillRect(1530,1530,100,45);
        //绘制8条干扰线
        for (int i = 0; i < 8; i++){
            g.setColor(new Color(r.nextInt(255), r.nextInt(255),r.nextInt(255)));
            g.drawLine(r.nextInt(100),r.nextInt(30),r.nextInt(100),r.nextInt(30));
        }
        //设置画笔颜色
        g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
        //调用方法，获取长度为5的随机字符串
        String number = getNumbers(5);
        //用session存下来比较验证码是不是一致
        HttpSession session = req.getSession();
        //绑定
        session.setAttribute("userIdCode",number);
        //粗细，斜体，大小
        g.setFont(new Font(null,Font.ITALIC+Font.BOLD,24));
        //写的内容，字符串位置
        g.drawString(number,5,25);
        //画完之后要发送给客户端
        //response是封装响应数据的,告诉浏览器你现在看到的是一张图片，要以图片的形式解析
        res.setContentType("image/jpeg");
        //以流的形式写入到response里面，要获取response的输出流
        OutputStream out = res.getOutputStream();
        //ImageIO这个类把图片传到out流里面
        ImageIO.write(image,"jpeg",out);
        out.close();
        super.service(req, res);
    }

    private String getNumbers(int size) {
        String str = "QWERTYUIOPLKJHGFDSAZXCVBNM0123456789";
        String number = "";
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            //给一个位置，就把该位置的字符给你str.charAT(index)
            //r.nextInt(str.length()),范围从0~255，随机字符串
            char c = str.charAt(r.nextInt(str.length()));
            number = number + c; //变成字符串
        }
        return number;
    }
}
