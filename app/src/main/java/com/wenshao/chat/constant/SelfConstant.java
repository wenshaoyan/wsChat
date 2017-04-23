package com.wenshao.chat.constant;

import com.wenshao.chat.bean.UserBean;

/**
 * Created by wenshao on 2017/4/10.
 * 用户的信息
 */

public class SelfConstant {
    private static UserBean userBean;


    public static UserBean getUserBean() {
        return userBean;
    }

    public static void setUserBean(UserBean userBean) {
        SelfConstant.userBean = userBean;
    }
}
