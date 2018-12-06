package com.synpowertech.dataCollectionJar.initialization;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.synpowertech.dataCollectionJar.utils.Util4j;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.utils
 * @ClassName: JedisUtil
 * @Description: jedi缓存工具类
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月2日上午10:52:18 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
@Component
public class JedisUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);
	
	//@Autowired 不能自动注入，会报异常,static保证初始化注入后，shardedJedisPool有有效值不为空（类变量共享）
	private static  ShardedJedisPool shardedJedisPool;
	
	
	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}


	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		JedisUtil.shardedJedisPool = shardedJedisPool;
		logger.info("shardedJedisPool is set successfully！");
	}

	static JedisUtil jedisUtil;
	
	/**
	  * @Title:  initMethod 
	  * @Description:  生成实例变量，供静态方法调用(内部是sharedJedisPool实现，可以不用这个实例)
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月10日上午9:25:28
	 */
	public void initMethod() {
		jedisUtil = new JedisUtil();
		logger.info("jedisUtilCache inits succefully！");

	}
	
	
	/**
	 * @Title: getJedis
	 * @Description: 获取shardedJedis实例
	 * @return: ShardedJedis
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月9日下午3:45:00
	 */
	private static  ShardedJedis getShardedJedis() {
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
	 * @Title: releaseResource
	 * @Description: 释放shardedJedis资源
	 * @param shardedJedis:
	 *            void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月9日下午3:46:58
	 */
	public static  void releaseResource(ShardedJedis shardedJedis) {
		if (shardedJedis != null && shardedJedisPool != null) {
			// 最新版弃用shardedJedisPool.returnResource，用shardedJedis.close()替代
			shardedJedis.close();
		}
	}

	/**
	 * @Title: setString
	 * @Description: 写入String
	 * @param key
	 * @param value:
	 *            void
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
	 * @Title: setMap
	 * @Description: 写入map
	 * @param key
	 * @param map:
	 *            void
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
	  * @Title:  setMapPipelined 
	  * @Description:  批量循环插入时使用
	  * @param key
	  * @param map: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月19日下午2:47:35
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
	public static  String getString(String key) {
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
	 *             key
	 * @param: @return
	 * @return: Map<Integer,String>
	 * @lastEditor: Taylor
	 * @lastEdit: 2017年7月27日上午3:29:03
	 */
	public static Map<String, String> getMap(String key) {
		ShardedJedis shardedJedis = getShardedJedis();
		Map<String, String> map = null ;
		try {
			if (shardedJedis != null) {
				// 处理设备掉线后，设备map被置空
				Map<String,String> mapTemp = shardedJedis.hgetAll(key);
				map = mapTemp.isEmpty() ? null : mapTemp;
			}
		}catch (Exception e) {
			logger.error("shardedJedis.hkeys:{},get a exception：{}",key,e);
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
	  * @Title:  setStringInMap 
	  * @Description:  设置key对应map中字段和值
	  * @param key
	  * @param field
	  * @param value: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年3月19日下午4:40:46
	 */
	public static void setStringInMap(String key, String field,String value) {
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
	 *             key
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
	 * @Title: deleteData
	 * @Description: 删除数据
	 * @param key
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
	 * @Title: setEx
	 * @Description: 设置key过期时间
	 * @param key
	 * @param expireSecond
	 * @param value
	 * @throws ServiceException:
	 *             void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月2日上午10:56:17
	 */
	public static void setEx(String key, int expireSecond, String value){
		ShardedJedis shardedJedis = getShardedJedis();
		try {
			shardedJedis.setex(key, expireSecond, value);
		} catch (Exception e) {
			logger.error("set data to redis unsuccessfully:{}",e);
		} finally {
			if (shardedJedis != null) {
				releaseResource(shardedJedis);
			}
		}
	}

	/**
	 * @Title: exist
	 * @Description: 判断某个key是否存在
	 * @param key
	 * @return
	 * @throws ServiceException:
	 *             boolean
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月2日上午10:58:32
	 */
	public static boolean exist(String key){
		ShardedJedis shardedJedis = getShardedJedis();
		boolean result = false;
		try {
			result = shardedJedis.exists(key);
		} catch (Exception e) {
			logger.error("judge data of redis unsuccessfully:{}",e);
		} finally {
			if (shardedJedis != null) {
				releaseResource(shardedJedis);
			}
		}
		return result;

	}


}
