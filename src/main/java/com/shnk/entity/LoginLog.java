package com.shnk.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog {
    private int id;
    private int sessionId;
    private String username;
    private String loginTime;
    private String exitTime;
    private String name;
}
