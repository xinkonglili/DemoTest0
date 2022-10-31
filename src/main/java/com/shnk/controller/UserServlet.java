package com.shnk.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.alibaba.fastjson.JSONObject;
import com.shnk.entity.*;
import com.shnk.service.impl.CompanyServiceImpl;
import com.shnk.service.impl.DepartmentServiceImpl;
import com.shnk.service.impl.UserServiceImpl;
import com.utils.WebUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import yanzhengma.Verify;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


public class UserServlet extends BaseServlet {

    private UserServiceImpl userService = new UserServiceImpl();

    /**
     * 处理登录的功能
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(req.getSession().getAttribute("user")!=null){
            resp.sendRedirect("index.jsp");
        }else{
        //  1、获取请求的参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //用户输入的验证码
        String code = req.getParameter("userIdCode");//把验证码文本框里面的内容获取到
        HttpSession session = req.getSession();
        //强转，默认是object，拿到图片上的验证码
        String vcode = (String) session.getAttribute("userIdCode");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        String lastAccessTime=null;
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String currentTime = sdf.format(new Date());
        req.getSession().setAttribute("currentTime",currentTime);
        Cookie[] cookies = req.getCookies();
        //遍历Cookie数组，取出上次访问时间 lastAccessTime
        for(int i=0;cookies!=null&&i<cookies.length;i++){
            if("lastAccess".equals(cookies[i].getName())){
                lastAccessTime = URLDecoder.decode(cookies[i].getValue());
                break;
            }
        }

        // 用户第一次请求时，lastAccessTime为null
        // 非第一次请求时，lastAccessTime不为null
        if(lastAccessTime==null){
            req.getSession().setAttribute("currentTime",currentTime);
        }else {
            req.getSession().setAttribute("lastAccessTime",lastAccessTime);
        }
        // 每次进入都需要将当前时间更新进Cookie，覆盖原来记录的时间
        // 设置过期时间 10天
        /**
         * 关于设置Cookie的value时报错：Cookie值中存在无效字符 的问题
         * 此处应在设置时统一编码和解码方式：
         * Cookie cookie=new Cookie("lastAccess", URLEncoder.encode(currentTime));
         * lastAccessTime=URLDecoder.decode(cookies[i].getValue());
         */
        Cookie cookie=new Cookie("lastAccess", URLEncoder.encode(currentTime));
        cookie.setMaxAge(10*24*60*60);
        resp.addCookie(cookie);

        /*String verifyCode=req.getParameter("verifyCode");
        String vCode= (String) req.getSession().getAttribute("vCode");*/
        if(code.equalsIgnoreCase(vcode)){
            // 调用 userService.login()登录处理业务
            User user = userService.login(new User(username, password));
            // 如果等于null,说明登录 失败!
            if (user == null) {
                // 把错误信息，和回显的表单项信息，保存到Request域中
                req.setAttribute("msg", "用户或密码错误！");
                req.setAttribute("username", username);
                //   跳回登录页面
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
            } else {
                if(user.getStatus()==0){
                    req.setAttribute("msg","账号已被禁用");
                    req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
                }else{
                    int userId=user.getUserId();
                    String oldLoginTime=userService.getLoginLogByUserId(userId);
                    // 登录 成功
                    //HttpSession session = req.getSession();
                    session.setAttribute("username",username);
                    session.setAttribute("userId",userId);
                    session.setAttribute("user", user);
                    session.setAttribute("oldLoginTime",oldLoginTime);
                    Cookie c = new Cookie("JSESSIONID", URLEncoder.encode(session.getId(), "utf-8"));
                    c.setPath("/");
                    c.setMaxAge( 2*60*60);
                    resp.addCookie(c);
                    resp.sendRedirect("index.jsp");
                }
            }
        }else{
            req.setAttribute("msg","验证码错误");
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
        }
        }


    }


    /**
     * 注销用户
     * @param req
     * @param resp
     */
    protected void logout(HttpServletRequest req,HttpServletResponse resp){
        req.getSession().invalidate();

        try {
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void existsUserName(HttpServletRequest req,HttpServletResponse resp) throws IOException {

        String username = req.getParameter("username");

        if(username!=null){
            boolean  existsUsername = userService.existsUsername(username);
            Map<String,Object> map=new HashMap<>();
            map.put("existsUsername",  existsUsername);

        }
    }
    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 生成了一个 验证码
        try {
            Verify.getVirify(request, response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //用户管理：用户列表
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonResult<UserPOVO> jsonResult=new JsonResult<>();
        String name=request.getParameter("name");
        if(name==null){
            name="";
        }else{
            name=new String(name.getBytes("ISO-8859-1"), "UTF-8");
        }
        Integer departmentId=request.getParameter("departmentId")==null?-1:Integer.parseInt(request.getParameter("departmentId"));
        Integer companyId=request.getParameter("companyId")==null?-1:Integer.parseInt(request.getParameter("companyId"));
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));;//每页几条
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));//当前页
        List<UserPOVO> userPOVOList= userService.getUser(name,departmentId,companyId);
        int count=userPOVOList.size();
        jsonResult.setData(userPOVOList.subList(page * limit - limit, (page * limit) >= count ? count : (page * limit)));
        jsonResult.setCode(200);
        jsonResult.setCount(count);
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }
    //用户管理：登录日志
    public void getLoginLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));;//每页几条
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));//当前页
        String start=request.getParameter("start") == null ? "" : request.getParameter("start");//开始时间
        String end=request.getParameter("end") == null ? "" : request.getParameter("end");//截止时间
        Integer departmentId=request.getParameter("departmentId")==null?-1:Integer.parseInt(request.getParameter("departmentId"));
        Integer companyId=request.getParameter("companyId")==null?-1:Integer.parseInt(request.getParameter("companyId"));
        String companyName="";
        String departmentName="";
        if(companyId!=-1){
            companyName=new CompanyServiceImpl().getCompanyNameById(companyId);
        }
        if(departmentId!=-1){
            departmentName=new DepartmentServiceImpl().getDepartmentNameById(departmentId);
        }
        String name=request.getParameter("name") == null ? "" : new String(request.getParameter("name").getBytes("ISO-8859-1"),"utf-8");
        String username=request.getParameter("username") == null ? "" : new String(request.getParameter("username").getBytes("ISO-8859-1"),"utf-8");

        List<LoginLogPOVO> loginLogPOVOList=userService.getLoginLog(companyName,departmentName,name,username,start,end);
        //保存查询结果
        Export.loginLogPOVOList=loginLogPOVOList;
        JsonResult<LoginLogPOVO> jsonResult=new JsonResult<>();
        int count=loginLogPOVOList.size();
        jsonResult.setCount(count);
        jsonResult.setCode(200);
        jsonResult.setData(loginLogPOVOList.subList(page * limit - limit, (page * limit) >= count ? count : (page * limit)));
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().getAttribute("user");
        User user = (User) request.getSession().getAttribute("user");
        String idStr=request.getParameter("idStr");
        int result=userService.deleteUser(idStr,user);
        JsonResult<User> jsonResult=new JsonResult<>();
        jsonResult.setCode(200);
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }
    public void upload(HttpServletRequest request, HttpServletResponse response){
        User user1= (User) request.getSession().getAttribute("user");
        String username=user1.getUsername();
        List<String> images=new ArrayList<>();
        User user=new User();
        FileItemFactory f =new DiskFileItemFactory();
        ServletFileUpload su =new ServletFileUpload(f);
        su.setHeaderEncoding("utf-8");
        try{
            List<FileItem> items =su.parseRequest(request);
            for(FileItem item:items){
                if(!item.isFormField()){
                    //文件名字
                    String filename =item.getName();
                    //文件后缀
                    String fExt =filename.substring(filename.lastIndexOf("."));
                    //为了避免重复 改名
                    String newName = UUID.randomUUID().toString().replaceAll("-","")+fExt;

                    ServletContext application =this.getServletConfig().getServletContext();
                    String path ="D:\\DemoTest00\\DemoTest0\\target\\DemoTest0\\upload\\"+newName;
                    //把图片保存路径放到数据库以备下次使用
                    item.write(new File(path));
                    images.add("upload/"+newName);
                }else{
                    if(item.getFieldName().equals("departmentId")){
                        user.setDepartmentId(Integer.parseInt(new String(item.getString().getBytes("ISO-8859-1"),"utf-8")));
                    }else if(item.getFieldName().equals("name")){
                        user.setName(new String(item.getString().getBytes("ISO-8859-1"),"utf-8"));
                    }else if(item.getFieldName().equals("username")){
                        user.setUsername(new String(item.getString().getBytes("ISO-8859-1"),"utf-8"));
                    }else if(item.getFieldName().equals("password")){
                        user.setPassword(new String(item.getString().getBytes("ISO-8859-1"),"utf-8"));
                    }else if(item.getFieldName().equals("email")){
                        user.setEmail(new String(item.getString().getBytes("ISO-8859-1"),"utf-8"));
                    }else if (item.getFieldName().equals("status")){
                       user.setStatus(Integer.parseInt(new String(item.getString().getBytes("ISO-8859-1"),"utf-8")));
                    }
                }
            }
            user.setImages(images.toString().substring(1,images.toString().length()-1));
            int result=userService.addUser(user,username);
            JsonResult<User> jsonResult=new JsonResult<>();
            jsonResult.setCode(200);
            response.getWriter().write(JSONObject.toJSONString(jsonResult));
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //修改用户
    public void modifyUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user1= (User) request.getSession().getAttribute("user");
        String username = user1.getUsername();
        List<String> images = new ArrayList<>();
        User user = new User();
        FileItemFactory f = new DiskFileItemFactory();//图片上传处理
        ServletFileUpload su = new ServletFileUpload(f);
        su.setHeaderEncoding("utf-8");
        try {
            List<FileItem> items = su.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    //文件名字
                    String filename = item.getName();
                    //文件后缀
                    String fExt = filename.substring(filename.lastIndexOf("."));
                    //为了避免重复 改名
                    String newName = UUID.randomUUID().toString().replaceAll("-", "") + fExt;

                    ServletContext application = this.getServletConfig().getServletContext();
                    String path = "D:\\DemoTest00\\DemoTest0\\target\\DemoTest0\\upload\\"+newName;
                    //把图片保存路径放到数据库以备下次使用
                    item.write(new File(path));
                    images.add("upload/" + newName);
                    System.out.println("upload/" + newName);
                } else {
                    if (item.getFieldName().equals("departmentId")) {
                        user.setDepartmentId(Integer.parseInt(new String(item.getString().getBytes("ISO-8859-1"), "utf-8")));
                    } else if (item.getFieldName().equals("name")) {
                        user.setName(new String(item.getString().getBytes("ISO-8859-1"), "utf-8"));
                    } else if (item.getFieldName().equals("username")) {
                        user.setUsername(new String(item.getString().getBytes("ISO-8859-1"), "utf-8"));
                    } else if (item.getFieldName().equals("password")) {
                        user.setPassword(new String(item.getString().getBytes("ISO-8859-1"), "utf-8"));
                    } else if (item.getFieldName().equals("email")) {
                        user.setEmail(new String(item.getString().getBytes("ISO-8859-1"), "utf-8"));
                    } else if (item.getFieldName().equals("status")) {
                        user.setStatus(Integer.parseInt(new String(item.getString().getBytes("ISO-8859-1"), "utf-8")));
                    } else if (item.getFieldName().equals("image")) {
                        images.add(item.getString());
                    } else if (item.getFieldName().equals("userId")) {
                        user.setUserId(Integer.parseInt(new String(item.getString().getBytes("ISO-8859-1"), "utf-8")));
                    }
                }
            }
            user.setImages(images.toString().substring(1, images.toString().length() - 1));
            if(user1.getUserId()==user.getUserId()&&user.getStatus()==0){
                JsonResult jsonResult=new JsonResult();
                jsonResult.setCode(201);
                jsonResult.setMsg("禁止操作！");
                response.getWriter().write(JSONObject.toJSONString(jsonResult));
            }else{
                int result = userService.modifyUser(user, username);
                JsonResult<User> jsonResult = new JsonResult<>();
                if (result != 0) {
                    jsonResult.setCode(200);
                }
                response.getWriter().write(JSONObject.toJSONString(jsonResult));
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void deleteLoginLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr=request.getParameter("idStr");//companyId
        int result=userService.deleteLoginLog(idStr);
        JsonResult<User> jsonResult=new JsonResult<>();
        if(result!=0){
            jsonResult.setCode(200);
        }
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }
    //人员统计：登录统计
    public void getLoginLogCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));;//每页几条
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));//当前页
        String start=request.getParameter("start") == null ? "" : request.getParameter("start");//开始时间
        String end=request.getParameter("end") == null ? "" : request.getParameter("end");//截止时间
        Integer departmentId=request.getParameter("departmentId")==null?-1:Integer.parseInt(request.getParameter("departmentId"));
        Integer companyId=request.getParameter("companyId")==null?-1:Integer.parseInt(request.getParameter("companyId"));
        String companyName="";
        String departmentName="";
        //获取：获取公司名称
        if(companyId!=-1){
            companyName=new CompanyServiceImpl().getCompanyNameById(companyId);
        }
        //获取：获取部门名称
        if(departmentId!=-1){
            departmentName=new DepartmentServiceImpl().getDepartmentNameById(departmentId);
        }
        String name=request.getParameter("name") == null ? "" : new String(request.getParameter("name").getBytes("ISO-8859-1"),"utf-8");
        String username=request.getParameter("username") == null ? "" : new String(request.getParameter("username").getBytes("ISO-8859-1"),"utf-8");
        //获取参数，调用userService的方法
        List<LoginLogCount> loginLogCountList=userService.getLoginLogCount(companyName,departmentName,name,username,start,end);
        Export.loginLogCountList=loginLogCountList;
        JsonResult<LoginLogCount> jsonResult=new JsonResult<>();
        int count=loginLogCountList.size();
        jsonResult.setCount(count);
        jsonResult.setCode(200);
        jsonResult.setData(loginLogCountList.subList(page * limit - limit, (page * limit) >= count ? count : (page * limit)));
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }

    //查询操作日志
    public void getOperationInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));;//每页几条
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));//当前页
        String start=request.getParameter("start") == null ? "" : request.getParameter("start");//开始时间
        String end=request.getParameter("end") == null ? "" : request.getParameter("end");//截止时间
        String type=request.getParameter("type") == null ? "" : new String(request.getParameter("type").getBytes("ISO-8859-1"), "UTF-8");//开始时间
        String operator=request.getParameter("operator") == null ? "" : new String(request.getParameter("operator").getBytes("ISO-8859-1"), "UTF-8");//截止时间
        String tableName=request.getParameter("tableName") == null ? "" : request.getParameter("tableName");//开始时间
        List<OperationInfo> operationInfoList=userService.getOperationInfo(tableName,type,operator,start,end);
        //导出excle
        Export.operationInfoList=operationInfoList;
        JsonResult<OperationInfo> jsonResult=new JsonResult<>();
        int count=operationInfoList.size();
        jsonResult.setCount(count);
        jsonResult.setCode(200);
        jsonResult.setData(operationInfoList.subList(page * limit - limit, (page * limit) >= count ? count : (page * limit)));
        response.getWriter().write(JSONObject.toJSONString(jsonResult));

    }

    //导出登录详情
    public void exportLoginLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("登录日志", "登录日志", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, LoginLogPOVO.class, Export.loginLogPOVOList).write(response.getOutputStream());
    }
    //导出登录统计
    public void exportLoginLogCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("登录统计", "登录统计", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, LoginLogCount.class, Export.loginLogCountList).write(response.getOutputStream());
    }
    //导出人员统计
    public void exportStaffCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("人员统计", "人员统计", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, StaffCount.class, Export.staffCountList).write(response.getOutputStream());
    }
    //导出操作日志
    public void exportOperationInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("操作日志", "操作日志", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, OperationInfo.class, Export.operationInfoList).write(response.getOutputStream());
    }

    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<UserPOVO> userPOVOList= userService.getUser("",-1,-1);
        System.out.println(userPOVOList.toString());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("用户信息", "导出测试", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, UserPOVO.class, userPOVOList).write(response.getOutputStream());
    }


}
