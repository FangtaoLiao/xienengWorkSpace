package com.synpower.util.annotation;

import java.lang.annotation.*;

/**
 * 验证参数是否为空
 * Created by SP0013 on 2017/10/31.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
}
