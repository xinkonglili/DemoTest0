package com.shnk.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class LoginLogCount {
    private int id;
    @Excel(name="单位名称")
    private String companyName;
    @Excel(name="部门名称")
    private String departmentName;
    @Excel(name="姓名")
    private String name;
    @Excel(name="用户名")
    private String username;
    @Excel(name="登陆次数")
    private int count;
}
