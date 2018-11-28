package com.synpower.lang;

/**
 * Created by SP0013 on 2017/10/12.
 */
public class WebsocketException extends Exception{
    public WebsocketException(String msg){
        super(msg);
    }
    public WebsocketException(Throwable cause, String msg){super(msg,cause);}
}
