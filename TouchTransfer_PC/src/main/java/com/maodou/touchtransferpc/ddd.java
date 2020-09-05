package com.maodou.touchtransferpc;

import cn.hutool.core.date.DateUtil;

/**
 * @Description TODO
 * @Author Wang Yucui
 * @Date 9/4/2020 3:22 PM
 */
public class ddd {
    public static void main(String[] args) {

        //当前时间字符串，格式：yyyy-MM-dd HH:mm:ss
        String now = DateUtil.now();
        String replace = now.replace("-", "").replace(" ", "").replace(":", "");
        System.out.println(replace);

    }
}
