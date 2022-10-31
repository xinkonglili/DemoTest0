package com.shnk.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    private int departmentId;
    private String departmentName;
    private int companyId;
    private int status;

}
