package com.synpower.mq;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

import java.io.Serializable;

/**
 * 消息中间件序列化类
 * Created by SP0013 on 2017/10/18.
 */
@DefaultSerializer(BeanSerializer.class)
public class MqMessage implements Serializable{
    @FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
    private String usrId;//用户名
    @FieldSerializer.Bind(DefaultSerializers.ClassSerializer.class)
    private Object body;
    @FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
