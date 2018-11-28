package com.synpower.util;

import com.synpower.lang.CacheNotFoundException;
import com.synpower.lang.ServiceException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/*****************************************************************************
 * @Package: com.synpower.util ClassName: CacheUtil3
 * @Description: 数据曲线
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2018年3月30日下午4:53:34 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@Component
public class CacheUtil3 {// 和官方命令一样的格式，方便查询，具体请查询http://doc.redisfans.com/，没有写完，需要时再补充
    protected static JedisPool cachePool3;

    @Autowired
    public void setCachePool3(JedisPool cachePool3) {
        CacheUtil3.cachePool3 = cachePool3;
    }

    public JedisPool getCachePool3() {
        return cachePool3;
    }

    private static Logger logger = LoggerFactory.getLogger(CacheUtil3.class);

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。 如果 key 已经存在， SETEX 命令将覆写旧值。
     *
     * @param key          key
     * @param expireSecond 超时时间,以秒为单位
     * @param value        值
     * @throws ServiceException
     */

    public void setEx(String key, int expireSecond, String value) throws ServiceException {// 这里似乎可以AOP
        logger.error(System.currentTimeMillis() + "KEY :" + key);
        Jedis jedis = null;
        try {
            jedis = cachePool3.getResource();
            jedis.setex(key, expireSecond, value);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ServiceException("缓存出错，请查看错误日志");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回 key 所关联的字符串值。
     * <p>
     * 如果 key 不存在那么返回特殊值 nil 。
     * <p>
     * 假如 key 储存的值不是字符串类型，返回一个错误，因为 GET 只能用于处理字符串值。
     *
     * @param key
     * @return
     * @throws ServiceException
     */
    public boolean exist(String key) throws ServiceException {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = cachePool3.getResource();
            result = jedis.exists(key);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ServiceException("缓存出错，请查看错误日志");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;

    }

    /**
     * 删除指定KEY
     *
     * @param key
     * @return 不等于0删除成功
     * @throws ServiceException
     */
    public Long del(String key) throws ServiceException {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = cachePool3.getResource();
            result = jedis.del(key);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ServiceException(ex, "缓存出错，请查看错误日志");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public Object setNx(String key, String value) {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = cachePool3.getResource();
            result = jedis.setnx(key, value);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public String setHmset(String key, Map<String, String> value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = cachePool3.getResource();
            result = jedis.hmset(key, value);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public String getEx(String key, int expireSecond) throws ServiceException, CacheNotFoundException {
        Jedis jedis = null;
        String msg;
        try {
            if (StringUtils.isBlank(key)) {
                throw new ServiceException("key is not exist!");
            }
            if (expireSecond == 0) {
                throw new ServiceException("key must be set expireTime or not be set '0'");
            }
            jedis = cachePool3.getResource();
            msg = jedis.get(key);
            if (StringUtils.isBlank(msg)) {
                throw new CacheNotFoundException("没有在缓存下找到该值,可能已经过期");
            }
            jedis.expire(key, expireSecond);
        } catch (CacheNotFoundException ex) {
            throw new CacheNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ServiceException("缓存出错，请查看错误日志");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return msg;
    }

    /**
     * @Title: getString
     * @Description: 获取String值
     * @param: key
     * @return: String
     * @lastEditor: Taylor
     * @lastEdit: 2017年7月27日上午2:55:15
     */
    public String getString(String key) {
        Jedis jedis = null;
        String valueString;
        try {
            jedis = cachePool3.getResource();
            if (jedis == null) {
                valueString = null;
            } else {
                valueString = jedis.get(key);
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return valueString;
    }

    /**
     * @Title: getMap
     * @Description: 获取map
     * @param: @param key
     * @param: @return
     * @return: Map{<String,String>}
     * @lastEditor: Taylor
     * @lastEdit: 2017年7月27日上午3:29:03
     */
    public static Map<String, String> getMap(String key) {
        Jedis jedis = null;
        Map<String, String> map = null;
        try {
            jedis = cachePool3.getResource();
            if (jedis == null) {
                map = null;
            } else {
                map = jedis.hgetAll(key);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        if (map != null && map.isEmpty()) {
            map = null;
        }
        return map;
    }

    /**
     * @Title: getAllKeys
     * @Description: 获取jedis中所有的key
     * @return: Set<String>
     * @lastEditor: Taylor
     * @lastEdit: 2017年9月22日上午12:18:25
     */
    public Set<String> getAllKeys() {
        Jedis jedis = null;
        try {
            jedis = cachePool3.getResource();
            if (jedis == null) {
                return null;
            } else {
                return jedis.keys("*");
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static Map<String, String> getMapByDb(String key, int i) {
        Jedis jedis = null;
        Map<String, String> map = null;
        try {
            jedis = cachePool3.getResource();
            if (jedis == null) {
                map = null;
            } else {
                jedis.select(i);
                map = jedis.hgetAll(key);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        if (map != null && map.isEmpty()) {
            map = null;
        }
        return map;
    }
}
