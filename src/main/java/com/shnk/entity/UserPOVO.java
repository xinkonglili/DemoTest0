package com.shnk.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class UserPOVO {
    private int userId;
    @Excel(name="姓名")
    private String name;
    @Excel(name="用户名")
    private String username;
    @Excel(name="密码")
    private String password;
    private int departmentId;
    @Excel(name="创建时间")
    private String createTime;
    @Excel(name="邮箱")
    private String email;
    @Excel(name="图片")
    private String images;
    @Excel(name="状态")
    private int status;
    @Excel(name="单位")
    private String companyName;
    @Excel(name="部门")
    private String departmentName;
    private int companyId;

}
