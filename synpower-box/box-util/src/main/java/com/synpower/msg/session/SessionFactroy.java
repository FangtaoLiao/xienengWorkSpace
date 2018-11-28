package com.synpower.msg.session;

import com.synpower.lang.SessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * session工厂
 *
 * @author SP0013
 * @date 2017/10/23
 */
@Component
public class SessionFactroy {
    @Autowired
    private Session session;
    public Session createSession() throws SessionException {
        if(session == null) {
            throw new SessionException("session为空，请检查配置");
        }
        return session;
    }
}
