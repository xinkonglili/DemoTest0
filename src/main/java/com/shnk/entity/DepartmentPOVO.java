package com.shnk.entity;


import lombok.Data;

@Data
public class DepartmentPOVO {
    private int departmentId;
    private String departmentName;
    private int companyId;
    private int status;
    private String companyName;
}
