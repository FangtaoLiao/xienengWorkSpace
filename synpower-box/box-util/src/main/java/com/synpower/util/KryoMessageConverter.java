package com.synpower.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.synpower.bean.SysOrg;
import com.synpower.mq.MqMessage;
import com.synpower.util.serializer.SerializerFactory;
import com.synpower.util.serializer.SynpowerSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

/**
 * mq自定义转换器
 * Created by SP0013 on 2017/10/18.
 */
@Component
public class KryoMessageConverter extends AbstractJsonMessageConverter {
    private Logger logger = Logger.getLogger(KryoMessageConverter.class);
    private static KryoPool pool = null;
    static {
        KryoFactory factory = () -> {
            Kryo kryo = new Kryo();
            return kryo;
        };
        pool = new KryoPool.Builder(factory).build();
    }
    @Override
    protected Message createMessage(Object o, MessageProperties messageProperties) {
       String body =  getSerializer().serialization(o,o.getClass());
       return  new Message(body.getBytes(),messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        byte[] b = message.getBody();
        ByteArrayInputStream bais = null;
        Input input = null;
        if(b==null||b.length==0) {
            throw new MessageConversionException("没有任何消息");
        }
        Map<String,Object> map = message.getMessageProperties().getHeaders();
        String clazz = (String)map.get("clazz");
        if(StringUtils.isBlank(clazz)) {
            throw  new MessageConversionException("头文件里没有clazz信息，请在发消息时设置");
        }
        Kryo kryo = pool.borrow();
        Object object = null;
        try {
            if(!"java.util.ArrayList".equals(map.get("collection"))){
                kryo.setReferences(false);
                kryo.register(Class.forName(clazz), new JavaSerializer());
                bais = new ByteArrayInputStream(message.getBody());
                input = new Input(bais);
                object =  kryo.readClassAndObject(input);
            } else {
                kryo.setReferences(false);
                kryo.setRegistrationRequired(true);
                CollectionSerializer serializer = new CollectionSerializer();
                serializer.setElementsCanBeNull(true);
                serializer.setElementClass(MqMessage.class, new JavaSerializer());
                kryo.register(ArrayList.class, serializer);
                bais = new ByteArrayInputStream(message.getBody());
                input = new Input(bais);
                object =  kryo.readObject(input,ArrayList.class,serializer);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally {
            try {
                input.close();
                bais.close();
                pool.release(kryo);
            } catch (IOException e) {
                logger.error("关闭ByteArrayInputStream"+e.getMessage(),e);
            }
        }
        return object;
    }
    /**
     *session信息序列化
     *@author SP0013
     *@method  SynpowerSerializer
     *@param
     *@return
     *@Date 2017/10/26
     */
    private SynpowerSerializer getSerializer() {
        SerializerFactory factory = new SerializerFactory();
        return factory.createKryo();
    }
}
