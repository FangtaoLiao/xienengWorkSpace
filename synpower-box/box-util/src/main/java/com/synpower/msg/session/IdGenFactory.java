package com.synpower.msg.session;

import com.synpower.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Id生成器工厂
 * Created by SP0013 on 2017/10/25.
 */
@Service
public class IdGenFactory {
    @Value("#{propertyConfigurer4Config['workId']}")
    private String workId;
    @Value("#{propertyConfigurer4Config['datacenterId']}")
    private String datacenterId;

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(String datacenterId) {
        this.datacenterId = datacenterId;
    }
    public IdGenerator create() {
        return  new IdGenerator(Long.parseLong(StringUtils.isBlank(getWorkId())?"0":getWorkId()),Long.parseLong(StringUtils.isBlank(getDatacenterId())?"0":getDatacenterId()));
    }
}
