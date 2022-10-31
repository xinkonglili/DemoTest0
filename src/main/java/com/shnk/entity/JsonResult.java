package com.shnk.entity;

import lombok.Data;

import java.util.List;

@Data
public class JsonResult<T> {
    private Integer code;
    private String msg;
    private int count;
    private List<T> data;
}
