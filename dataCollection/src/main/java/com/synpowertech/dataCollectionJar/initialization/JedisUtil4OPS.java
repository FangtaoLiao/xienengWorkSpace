package com.synpowertech.dataCollectionJar.initialization;

import com.alibaba.fastjson.JSON;
import com.synpowertech.dataCollectionJar.dao.CollModelMapper;
import com.synpowertech.dataCollectionJar.dao.YxInfoMapper;
import com.synpowertech.dataCollectionJar.domain.CollModel;
import com.synpowertech.dataCollectionJar.domain.DataYx;
import com.synpowertech.dataCollectionJar.domain.Device;
import com.synpowertech.dataCollectionJar.domain.YxInfo;
import com.synpowertech.dataCollectionJar.utils.TimeUtil;
import com.synpowertech.dataCollectionJar.utils.Util4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.*;

@Component
public class JedisUtil4OPS extends JedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(JedisUtil4OPS.class);

    //@Autowired 不能自动注入，会报异常,static保证初始化注入后，shardedJedisPool有有效值不为空（类变量共享）
    private static ShardedJedisPool shardedJedisPool;

    private static YxInfoMapper yxInfoMapper;

    @Autowired
    public void setYxInfoMapper(YxInfoMapper yxInfoMapper) {
        JedisUtil4OPS.yxInfoMapper = yxInfoMapper;
    }

    private static final long ONE_DAY = 1000 * 60 * 60 * 24;

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        JedisUtil4OPS.shardedJedisPool = shardedJedisPool;
        logger.info("shardedJedisPool is set successfully！");
    }


    /**
     * @Title: getJedis
     * @Description: 获取shardedJedis实例
     * @return: ShardedJedis
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月9日下午3:45:00
     */
    private static ShardedJedis getShardedJedis() {
        ShardedJedis shardedJedis = null;
        try {
            if (shardedJedisPool != null) {
                shardedJedis = shardedJedisPool.getResource();
            }
        } catch (Exception e) {
            logger.error("[JedisUtil] get shardedJedis false:" + e);
        }
        return shardedJedis;
    }


    /**
     * @param shardedJedis: void
     * @Title: releaseResource
     * @Description: 释放shardedJedis资源
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月9日下午3:46:58
     */
    public static void releaseResource(ShardedJedis shardedJedis) {
        if (shardedJedis != null && shardedJedisPool != null) {
            // 最新版弃用shardedJedisPool.returnResource，用shardedJedis.close()替代
            shardedJedis.close();
        }
    }

    /**
     * @param key
     * @param value: void
     * @Title: setString
     * @Description: 写入String
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月9日下午3:49:43
     */
    public static void setString(String key, String value) {
        ShardedJedis shardedJedis = getShardedJedis();
        try {
            value = Util4j.null2String(value);

            //可加上事务
            shardedJedis.set(key, value);
        } catch (Exception e) {
            logger.error("set string into redis error:" + e);
        } finally {
            releaseResource(shardedJedis);
        }
    }

    /**
     * @param key
     * @param map: void
     * @Title: setMap
     * @Description: 写入map
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月9日下午3:50:52
     */
    public static void setMap(String key, Map<String, String> map) {
        ShardedJedis shardedJedis = getShardedJedis();
        try {
            if (shardedJedis != null && !map.isEmpty()) {
                shardedJedis.hmset(key, map);
            }
        } catch (Exception e) {
            logger.error("set map into redis error:" + e);
        } finally {
            releaseResource(shardedJedis);
        }
    }

    /**
     * @param key
     * @param map: void
     * @Title: setMapPipelined
     * @Description: 批量循环插入时使用
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月19日下午2:47:35
     */
    public static void setMapPipelined(String key, Map<String, String> map) {
        ShardedJedis shardedJedis = getShardedJedis();
        try {
            if (shardedJedis != null && !map.isEmpty()) {
                shardedJedis.pipelined().hmset(key, map);
            }
        } catch (Exception e) {
            logger.error("set map into redis error:" + e);
        } finally {
            releaseResource(shardedJedis);
        }
    }

    /**
     * @Title: getString
     * @Description: 获取String值
     * @param: key
     * @return: String
     * @lastEditor: Taylor
     * @lastEdit: 2017年7月27日上午2:55:15
     */
    public static String getString(String key) {
        ShardedJedis shardedJedis = getShardedJedis();
        String valueString;
        try {
            if (shardedJedis == null) {
                valueString = null;
            } else {
                valueString = shardedJedis.get(key);
            }
        } finally {
            releaseResource(shardedJedis);
        }
        return valueString;
    }

    /**
     * @Title: getMap
     * @Description: 获取map
     * @param: @param
     * key
     * @param: @return
     * @lastEdit: 2017年7月27日上午3:29:03
     */
    public static Map<String, String> getMap(String key) {
        ShardedJedis shardedJedis = getShardedJedis();
        Map<String, String> map = null;
        try {
            if (shardedJedis != null) {
                // 处理设备掉线后，设备map被置空
                Map<String, String> mapTemp = shardedJedis.hgetAll(key);
                map = mapTemp.isEmpty() ? null : mapTemp;
            }
        } catch (Exception e) {
            logger.error("shardedJedis.hkeys:{},get a exception：{}", key, e);
        } finally {
            releaseResource(shardedJedis);
        }
        return map;
    }

    /**
     * @Title: getStringInMap
     * @Description: 获取map中key的值
     * @param: key
     * @param: field
     * @param: @return
     * @return: String
     * @lastEditor: Taylor
     * @lastEdit: 2017年7月27日上午3:49:36
     */
    public static String getStringInMap(String key, String field) {
        ShardedJedis shardedJedis = getShardedJedis();
        String value = null;
        try {
            if (shardedJedis == null || !shardedJedis.exists(key)) {
                value = null;
            } else {
                value = shardedJedis.hmget(key, field).get(0);
            }
        } finally {
            releaseResource(shardedJedis);
        }
        return value;
    }

    /**
     * @param key
     * @param field
     * @param value: void
     * @Title: setStringInMap
     * @Description: 设置key对应map中字段和值
     * @lastEditor: SP0010
     * @lastEdit: 2018年3月19日下午4:40:46
     */
    public static void setStringInMap(String key, String field, String value) {
        ShardedJedis shardedJedis = getShardedJedis();
        try {
//			if (shardedJedis != null && shardedJedis.exists(key)) {
            if (shardedJedis != null) {
                shardedJedis.hset(key, field, value);
            }
        } finally {
            releaseResource(shardedJedis);
        }
    }

    /**
     * @Title: exist
     * @Description: 验证是否存在key
     * @param: @param
     * key
     * @param: @return
     * @return: boolean
     * @lastEditor: Taylor
     * @lastEdit: 2017年7月27日上午3:46:29
     */
    public static boolean exists(String key) {
        ShardedJedis shardedJedis = getShardedJedis();
        boolean exist = false;
        try {
            if (shardedJedis == null || !shardedJedis.exists(key)) {
                exist = false;
            } else {
                exist = true;
            }
        } finally {
            releaseResource(shardedJedis);
        }
        return exist;
    }

    /**
     * @Title: getAllKeys
     * @Description: shardedJedis不支持模糊查询，不能获取所有key,只能用jedis实现
     * @return: Set<String>
     * @lastEditor: Taylor
     * @lastEdit: 2017年9月22日上午12:18:25
     */
    public static Set<String> getAllKeys() {

        // ShardedJedis shardedJedis = getShardedJedis();
        // try {
        // if (shardedJedis == null) {
        // return null;
        // } else {
        // return shardedJedis.hkeys("*");
        // }
        // } finally {
        // releaseResource(shardedJedis);
        // }
        return null;
    }

    /**
     * @param key
     * @Title: deleteData
     * @Description: 删除数据
     * @return: boolean
     * @lastEditor: Taylor
     * @lastEdit: 2017年9月22日上午9:46:42
     */
    public static boolean deleteData(String key) {
        ShardedJedis shardedJedis = getShardedJedis();
        boolean exist = false;
        try {
            if (shardedJedis == null || !shardedJedis.exists(key)) {
                return true;
            } else {
                shardedJedis.del(key);
            }
        } finally {
            releaseResource(shardedJedis);
        }
        return exist;
    }

    /**
     * @param key
     * @param expireSecond
     * @param value
     * @Title: setEx
     * @Description: 设置key过期时间
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月2日上午10:56:17
     */
    public static void setEx(String key, int expireSecond, String value) {
        ShardedJedis shardedJedis = getShardedJedis();
        try {
            shardedJedis.setex(key, expireSecond, value);
        } catch (Exception e) {
            logger.error("set data to redis unsuccessfully:{}", e);
        } finally {
            if (shardedJedis != null) {
                releaseResource(shardedJedis);
            }
        }
    }

    /**
     * @param key
     * @return
     * @Title: exist
     * @Description: 判断某个key是否存在
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月2日上午10:58:32
     */
    public static boolean exist(String key) {
        ShardedJedis shardedJedis = getShardedJedis();
        boolean result = false;
        try {
            result = shardedJedis.exists(key);
        } catch (Exception e) {
            logger.error("judge data of redis unsuccessfully:{}", e);
        } finally {
            if (shardedJedis != null) {
                releaseResource(shardedJedis);
            }
        }
        return result;

    }

    /**
     * @Author lz
     * @Description: s设置过期时间
     * @param: [key, expireSecond]
     * @Date: 2018/11/28 11:16
     **/
    public static void setEx(String key, int expireSecond) {
        ShardedJedis shardedJedis = getShardedJedis();
        try {
            shardedJedis.expire(key, expireSecond);
        } catch (Exception e) {
            logger.error("set expire to redis unsuccessfully:{}", e);
        } finally {
            if (shardedJedis != null) {
                releaseResource(shardedJedis);
            }
        }
    }


    public static void addAlarm(DataYx dataYx) {
        Long changeTime = dataYx.getChangeTime();
        if (changeTime < TimeUtil.getDay0()) {
            logger.info("过时的告警:{}", dataYx);
            return;
        }
        Map<String, String> redisMap = new HashMap<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("status_value", dataYx.getStatusValue());
        map.put("data_time", changeTime + "");
        map.put("data_time_f", Util4j.longToDateString(changeTime, "yy-MM-dd hh:mm:ss"));
        map.put("sys_time", System.currentTimeMillis() + "");
        map.put("task_status", "0");
        map.put("yx_id", dataYx.getYxId()+"");
        Integer yxId = dataYx.getYxId();
        map.put("level", DeviceCache.alramLevelMap.get(yxId));
        //添加设备名称，遥信名称，电站id，电站名称
        Map<String,Object > Yx_map = new HashMap<>();
        //根据设备id和yxid找出遥信名称Signal_name、设备名称device_name、电站id plant_id，电站名plant_name
        List<YxInfo> yxInfoList = new ArrayList<>();
        yxInfoList = yxInfoMapper.getInfo();
        for(YxInfo yxInfo: yxInfoList){
               if(yxInfo.getDevice_id().equals(dataYx.getDeviceId()) && yxInfo.getYx_id().equals(dataYx.getYxId())){
                   map.put("signal_name",yxInfo.getSignal_name());
                   map.put("device_name",yxInfo.getDevice_name());
                   map.put("plant_name",yxInfo.getPlant_name());
                   map.put("plant_id",String.valueOf(yxInfo.getPlant_id()));
                   break;
               }
        }
        map.put("device_id",dataYx.getDeviceId()+"");
        redisMap.put(yxId + "", JSON.toJSONString(map));
        setMap(dataYx.getDeviceId() + "",redisMap);
    }




    public static void updateTaskStatus(DataYx dataYx) {
        setStringInMap(dataYx.getDeviceId() + "", "task_status", dataYx.getStatus());
    }

    //将告警设置为当日12点过期
    public static void updateAlarmEx(DataYx dataYx) {
        int exTime = (int) ((ONE_DAY - System.currentTimeMillis() + TimeUtil.getDay0()) / 1000);
        setEx(dataYx.getDeviceId() + "", exTime);
    }

}
