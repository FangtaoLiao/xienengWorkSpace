package com.synpower.mq;

import java.io.Serializable;
import java.util.List;

/**
 * 消息中间件序列化演示类
 * Created by SP0013 on 2017/10/18.
 */
public class Demo implements Serializable{
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
