package com.shnk.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class LoginLogPOVO {
    private int id;
    @Excel(name="登录时间")
    private String loginTime;
    @Excel(name="退出时间")
    private String exitTime;
    @Excel(name="单位名称")
    private String companyName;
    @Excel(name="部门名称")
    private String departmentName;
    @Excel(name="姓名")
    private String name;
    @Excel(name="用户名")
    private String username;
    private int count;
}
