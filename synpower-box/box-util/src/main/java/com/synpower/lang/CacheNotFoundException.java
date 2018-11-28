package com.synpower.lang;

/**
 * 缓存不存在异常
 * Created by SP0013 on 2017/10/23.
 */
public class CacheNotFoundException extends Exception{
    public CacheNotFoundException(){
        super();
    }


    public CacheNotFoundException(String msg){
        super(msg);
    }
    public CacheNotFoundException(Throwable cause, String msg){super(msg,cause);}
}
