package com.shnk.controller;

import com.alibaba.fastjson.JSONObject;
import com.shnk.entity.*;
import com.shnk.service.impl.DepartmentServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class DepartmentServlet extends BaseServlet{
    DepartmentServiceImpl departmentService=new DepartmentServiceImpl();
    //
    public void getDepartmentByCompanyId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer companyId=request.getParameter("companyId")==null?-1:Integer.parseInt(request.getParameter("companyId"));
        List<Department> departmentList= departmentService.getDepartmentByCompanyId(companyId);
        response.getWriter().write(JSONObject.toJSONString(departmentList));
    }
    //部门列表：获取部门列表
    public void getDepartment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer companyId=request.getParameter("companyId")==null?-1:Integer.parseInt(request.getParameter("companyId"));
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));;//每页几条
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));//当前页
        JsonResult<DepartmentPOVO> jsonResult=new JsonResult<>();
        //调用Service方法
        List<DepartmentPOVO> departmentPOVOList=departmentService.getDepartment(companyId);
        int count=departmentPOVOList.size();
        jsonResult.setData(departmentPOVOList.subList(page * limit - limit, (page * limit) >= count ? count : (page * limit)));
        jsonResult.setCode(200);
        jsonResult.setCount(count);
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }
    //添加部门
    public void addDepartment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user= (User) request.getSession().getAttribute("user");
        Department department=new Department();
        Integer companyId=request.getParameter("companyId")==null?-1:Integer.parseInt(request.getParameter("companyId"));
        Integer status=request.getParameter("status")==null?-1:Integer.parseInt(request.getParameter("status"));
        String departmentName=request.getParameter("departmentName");
        department.setDepartmentName(departmentName);
        department.setStatus(status);
        department.setCompanyId(companyId);
        Boolean result=departmentService.addDepartment(department,user);
        JsonResult<Department> jsonResult=new JsonResult<>();
        if(result){
            jsonResult.setCode(200);
        }
        response.getWriter().write(JSONObject.toJSONString(jsonResult));

    }
    //删除部门
    public void deleteDepartment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user= (User) request.getSession().getAttribute("user");
        String idStr=request.getParameter("idStr");
        int result=departmentService.deleteDepartment(idStr,user);
        JsonResult jsonResult=new JsonResult();
        if(result!=0){
            jsonResult.setCode(200);
        }
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }
    //修改部门
    public void modifyDepartment(HttpServletRequest request, HttpServletResponse response){
        User user= (User) request.getSession().getAttribute("user");
        Map<String, String[]> parameterMap = request.getParameterMap();
        Department department=new Department();
        JsonResult<Department> jsonResult = new JsonResult<>();
        int result=0;
        try {
            BeanUtils.populate(department,parameterMap);
            result=departmentService.modifyDepartment(department,user);
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
    //人员统计
    public void getStaffCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer departmentId=request.getParameter("departmentId")==null?-1:Integer.parseInt(request.getParameter("departmentId"));
        Integer companyId=request.getParameter("companyId")==null?-1:Integer.parseInt(request.getParameter("companyId"));
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));;//每页几条
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));//当前页
        JsonResult<StaffCount> jsonResult=new JsonResult<>();
        List<StaffCount> staffCountList=departmentService.getStaffCount(companyId,departmentId);
        //导出excle
        Export.staffCountList=staffCountList;
        int count=staffCountList.size();
        jsonResult.setCode(200);
        jsonResult.setCount(count);
        jsonResult.setData(staffCountList.subList(page * limit - limit, (page * limit) >= count ? count : (page * limit)));
        response.getWriter().write(JSONObject.toJSONString(jsonResult));
    }

    public String getDepartmentNameById(int departmentId) {
        String departmentName=departmentService.getDepartmentNameById(departmentId);
        return departmentName;
    }

}
