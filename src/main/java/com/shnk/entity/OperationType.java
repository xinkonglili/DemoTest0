package com.shnk.entity;

public enum OperationType {
    ADD("新增"),
    MODIFY("修改"),
    Delete("删除");

    private String name;
    OperationType(String name) {
        this.name = name;
    }
}
