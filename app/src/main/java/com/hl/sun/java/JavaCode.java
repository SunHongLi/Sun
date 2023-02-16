package com.hl.sun.java;

import com.hl.sun.bean.GroupByBean;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Function:测试java api
 * Date:2022/6/6
 * Author: sunHL
 */
public class JavaCode {
    private void method(){
    }
    /**
     * 使用java正则表达式去掉多余零，先去0再去.
     * @param doubleStr
     * @return
     */
    public static String subZeroAndDot(String doubleStr){
        if(null != doubleStr && doubleStr.indexOf(".") > 0){
            doubleStr = doubleStr.replaceAll("0+?$", "");//去掉多余的0
            doubleStr = doubleStr.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return doubleStr;
    }
}
