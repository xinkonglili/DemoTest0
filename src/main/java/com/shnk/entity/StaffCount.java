package com.shnk.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class StaffCount {
    @Excel(name="单位")
    private String companyName;
    @Excel(name="部门")
    private String departmentName;
    @Excel(name="人数")
    private Integer count;
}
