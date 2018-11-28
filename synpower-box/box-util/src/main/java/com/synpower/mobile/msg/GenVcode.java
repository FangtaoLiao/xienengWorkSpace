package com.synpower.mobile.msg;

import java.util.Random;

/**
 * 生成验证码
 * Created by SP0013 on 2017/11/2.
 */
public class GenVcode {
    public String randomCode() {
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (new Random().nextInt(10));
        }
        return vcode;
    }
}
