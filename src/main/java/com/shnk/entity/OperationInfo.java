package com.shnk.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationInfo {
    private int id;
    @Excel(name="表名")
    private String tableName;
    @Excel(name="contentBefore")
    private String contentBefore;
    @Excel(name="contentAfter")
    private String contentAfter;
    @Excel(name="时间")
    private String time;
    @Excel(name="操作人")
    private String operator;
    @Excel(name="操作类型")
    private String type;
    @Excel(name="操作结果")
    private String result;

    public OperationInfo(String tableName, String contentBefore, String contentAfter, String operator, String type, String result) {
        this.tableName = tableName;
        this.contentBefore = contentBefore;
        this.contentAfter = contentAfter;
        this.operator = operator;
        this.type = type;
        this.result = result;
    }

    public OperationInfo() {
    }
}
