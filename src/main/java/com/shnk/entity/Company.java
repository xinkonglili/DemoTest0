package com.shnk.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private int companyId;
    private String companyName;
    private String address;
    private String phone;
    //传真
    private String fax;
    //状态
    private int status;
}
