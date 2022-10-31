package com.shnk.controller;

import com.alibaba.fastjson.JSONObject;
import com.shnk.entity.Company;
import com.shnk.entity.JsonResult;
import com.shnk.entity.User;
import com.shnk.service.impl.CompanyServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class CompanyServlet extends BaseServlet {

    private CompanyServiceImpl companyService = new CompanyServiceImpl();

    public void getAllCompany(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Company> companyList = companyService.getAllCompany();
        response.getWriter().write(JSONObject.toJSONString(companyList));
    }

    //前台直接请求过来的，后台方法之前没有相互调用
    public void deleteCompanyById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        String idStr = request.getParameter("idStr");
        int result = companyService.deleteCompanyById(idStr,user);
        JsonResult<Company> jsonResult=new JsonResult<>();
        if(result!=0){
            jsonResult.setCode(200);
        }
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }

    public void getCompanyByName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String companyName=new String(request.getParameter("companyName").getBytes("ISO-8859-1"), "UTF-8");
        List<Company> companyList = companyService.getCompanyByName(companyName);
        int sumcount = companyList.size();//总条数
        int pagesize = 3;//每页几条
        int pagecount = sumcount % pagesize == 0 ? sumcount / pagesize : sumcount / pagesize + 1;//总页数
        int currpage = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));//当前页

        request.setAttribute("companyList", companyList.subList(currpage * pagesize - pagesize, (currpage * pagesize) >= sumcount ? sumcount : (currpage * pagesize)));
        request.setAttribute("currpage", currpage);//当前页
        request.setAttribute("pagecount", pagecount);//总页数
        request.getRequestDispatcher("companyList.jsp").forward(request, response);
    }
    public void addCompany(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        User user = (User) request.getSession().getAttribute("user");
        JsonResult<Company> jsonResult=new JsonResult<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Company company=new Company();
        try {
            BeanUtils.populate(company,parameterMap);
            companyService.addCompany(company,user);
            jsonResult.setCode(200);
            response.getWriter().write(JSONObject.toJSONString(jsonResult));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //单位管理
    public void getCompany(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String companyName=request.getParameter("companyName");
        if(companyName==null){
            companyName="";
        }else{
            companyName=new String(companyName.getBytes("ISO-8859-1"), "UTF-8");
        }
        //获取公司的所有字段，通过公司名称
        List<Company> companyList = companyService.getCompanyByName(companyName);
        int sumcount = companyList.size();//总条数
        int pagesize = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));;//每页几条
        int currpage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));//当前页
        JsonResult<Company> jsonResult=new JsonResult<>();
        jsonResult.setCount(companyList.size());
        //返回给定的动态数组截取的部分，currpage * pagesize - pagesize（开始数据的条数）
        jsonResult.setData(companyList.subList(currpage * pagesize - pagesize, (currpage * pagesize) >= sumcount ? sumcount : (currpage * pagesize)));
        jsonResult.setCode(200);
        String result=JSONObject.toJSONString(jsonResult);
        response.getWriter().write(result);
    }
    public void getCompanyById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id=Integer.parseInt(request.getParameter("id"));
        //通过公司的id来得到公司完整信息
        List<Company> companyList=companyService.getCompanyById(id);
        JsonResult<Company> jsonResult=new JsonResult<>();
        jsonResult.setCode(200);
        jsonResult.setData(companyList);
        jsonResult.setCount(companyList.size());
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }
    public void modifyCompany(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        User user = (User) request.getSession().getAttribute("user");
        Map<String, String[]> parameterMap = request.getParameterMap();
        Company company = new Company();
        JsonResult<Company> jsonResult = new JsonResult<>();
        try {
            BeanUtils.populate(company, parameterMap);
            //
            int result = companyService.modifyCompany(company,user);
            if(result!=0){
                jsonResult.setCode(200);
            }
            response.getWriter().write(JSONObject.toJSONString(jsonResult));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getCompanyNameById(int companyId) {
        String companyName=companyService.getCompanyNameById(companyId);
        return companyName;
    }


}
