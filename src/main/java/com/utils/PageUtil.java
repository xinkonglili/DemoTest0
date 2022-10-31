package com.utils;

import java.util.List;

public class PageUtil<T> {
    private static int pageSize=5;
    public  List<T> get(List<T> list,int pageNum){
        int sumcount=list.size();//总条数
        return list.subList(pageNum*pageSize-pageSize,(pageNum*pageSize)>=sumcount?sumcount:(pageNum*pageSize));
    }
}
