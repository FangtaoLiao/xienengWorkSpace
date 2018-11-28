package com.synpower.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.synpower.lang.SessionException;
import com.synpower.msg.session.IdGenFactory;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.SessionFactroy;

/**
 * Created by SP0013 on 2017/10/23.
 */
public class BaseHandler {
    @Autowired
    protected SessionFactroy sessionFactroy;
    @Autowired
    protected IdGenFactory idGenFactory;
    protected Long generateId(){
        return idGenFactory.create().nextId();
    }
    protected Session getSession() throws SessionException {
        return  sessionFactroy.createSession();
    }
}
