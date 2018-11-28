package com.synpower.util.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import org.apache.log4j.Logger;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;


/**
 * kryo序列化工具类
 * Created by SP0013 on 2017/10/23.
 */
public class KryoSerizilzer implements SynpowerSerializer {
    private Logger logger = org.apache.log4j.Logger.getLogger(KryoSerizilzer.class);
    private static KryoPool pool = null;

    static {
        KryoFactory factory = () -> {
            Kryo kryo = new Kryo();
            return kryo;
        };
        pool = new KryoPool.Builder(factory).build();
    }

    @Override
    public String serialization(Object o, Class clazz) {
        FileOutputStream files = null;
        BufferedOutputStream baos = null;
        Output output = null;
        Kryo kryo = pool.borrow();
        try {
            if (o instanceof ArrayList) {//不建议集合序列化，这个是JAVA原生方式
                kryo.setReferences(false);
                kryo.setRegistrationRequired(true);
                CollectionSerializer serializer = new CollectionSerializer();
                serializer.setElementsCanBeNull(true);
                serializer.setElementClass(clazz, new JavaSerializer());
                kryo.register(clazz, new JavaSerializer());
                kryo.register(ArrayList.class, serializer);
                files = new FileOutputStream("file.bin");
                baos = new BufferedOutputStream(files);
                output = new Output(baos);
                kryo.writeObject(output, o);
                return new String(Base64.getEncoder().encode(output.getBuffer()));
            } else {
                kryo.setReferences(false);
//                FieldSerializer fieldSerializer = new FieldSerializer(kryo,clazz);
//                kryo.addDefaultSerializer(clazz,fieldSerializer);
//                kryo.register(o.getClass(), fieldSerializer);
                files = new FileOutputStream("file.bin");
                baos = new BufferedOutputStream(files);
                output = new Output(baos);
                kryo.writeClassAndObject(output, o);
//                return new String(Base64.getEncoder().encode(baos.toByteArray()));
                return new String(Base64.getEncoder().encode(output.getBuffer()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
           this.releaseOut(baos,output,kryo,files);
        }
        return null;
    }

    @Override
    public Object deserialization(String obj, Class clazz,boolean isCollection) {
        byte [] b = obj.getBytes();
        ByteArrayInputStream bais = null;
        Input input = null;
        if(b==null||b.length==0) {
            throw new MessageConversionException("没有任何消息");
        }

        Kryo kryo = pool.borrow();
        Object object = null;
        try {
                kryo.setReferences(false);
                bais = new ByteArrayInputStream( Base64.getDecoder().decode(obj.getBytes()));
                input = new Input(bais);
                object =  kryo.readClassAndObject(input);
//            else {
//                kryo.setReferences(false);
//                kryo.setRegistrationRequired(true);
//                CollectionSerializer serializer = new CollectionSerializer();
//                serializer.setElementsCanBeNull(true);
//                serializer.setElementClass(User.class, new JavaSerializer());
//                kryo.register(ArrayList.class, serializer);
//                bais = new ByteArrayInputStream(obj.getBytes());
//                input = new Input(bais);
//                object =  kryo.readObject(input,ArrayList.class,serializer);
//            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally {
            releaseIn(bais,input,kryo);
        }
        return object;
    }
    private void releaseOut(BufferedOutputStream baos,Output output,Kryo kryo, FileOutputStream files) {
        try {
            output.flush();
            output.close();
            baos.flush();
            baos.close();
            files.close();
            pool.release(kryo);
        } catch (IOException e) {
            logger.error("关闭ByteArrayOutputStream" + e.getMessage(), e);
        }
    }
    private void releaseIn(ByteArrayInputStream bais,Input input,Kryo kryo) {
        try {
            input.close();
            bais.close();
            pool.release(kryo);
        } catch (IOException e) {
            logger.error("关闭ByteArrayOutputStream" + e.getMessage(), e);
        }
    }
}
