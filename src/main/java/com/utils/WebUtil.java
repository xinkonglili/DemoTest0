package com.utils;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class WebUtil {

    /**
     * 把Map中的值注入到bean对象中
     * @param value
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T copyParamToBean(Map value, T bean){
        try {
            System.out.println("注入之前："+bean);
            BeanUtils.populate(bean, value);
            System.out.println("注入之后："+bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 将字符串转换成int类型，如果为空则返回默认值
     * @param value
     * @param defaultValue
     * @return 返回int值
     */
    public static int parseInt(String value,int defaultValue){
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
        }

        return defaultValue;
    }

}
