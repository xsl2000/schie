/* 
 * 创建日期 2019年4月29日
 *
 * 四川健康久远科技有限公司
 * 电话： 
 * 传真： 
 * 邮编： 
 * 地址：成都市武侯区
 * 版权所有
 */
package org.schic.common.redis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.schic.common.config.Global;
import org.schic.common.security.MD5Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * 
 * @Description: redicache 工具类
 * @author Caiwb
 * @date 2019年4月29日 下午3:10:41
 */
@SuppressWarnings("unchecked")
@Component
public class RedisUtils {
	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

	public static final String SHIRO_REDIS = "shiro_redis_cache";
	public static final String SHIRO_REDIS_OBJECT = "org.apache.shiro.subject.SimplePrincipalCollection";
	public static final String GET_ID = ".get id=";
	public static final String TOTAL_KEY = ".total key=";
	public static final String FIND_LIST_KEY = ".findList key=";
	public static final String FIND_LIST_FIRST_KEY = ".findListFirst key=";
	public static final String FIND_PAGE_KEY = ".findPage key=";
	public static final String FIND_LIST_KEY_PATTERN = ".findList key=*";
	public static final String FIND_LIST_FIRST_KEY_PATTERN = ".findListFirst key=*";
	public static final String FIND_PAGE_KEY_PATTERN = ".findPage key=*";
	private static final String SPRING_REDIS_RUN = "spring.redis.run";
	public static final String KEY_PREFIX = Global
			.getConfig("spring.redis.keyPrefix");
	public static final Long EXPIRE_TIME = Long
			.parseLong(Global.getConfig("spring.redis.expireTime"));
	public static final Long EXPIRE_TIME_SHIRO = Long
			.parseLong(Global.getConfig("spring.redis.expireTimeShiro"));
	public static final String RUN_MESSAGE = "请开启Redis服务,还有Redis密码配置是否有误，而且密码不能为空（为空时Redis服务会连接不上）。";
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public static String getExpire() {
		return String.valueOf(EXPIRE_TIME / 60) + "分钟";
	}

	public static String getExpireShiro() {
		return String.valueOf(EXPIRE_TIME_SHIRO / 60) + "分钟";
	}

	public static String getKey(String className, String keyName,
			String keyId) {
		if (className == null) {
			className = "";
		}
		if (keyId == null) {
			keyId = "";
		}
		return RedisUtils.KEY_PREFIX + className + keyName + keyId;
	}

	public static String getIdKey(String className, String keyId) {
		if (className == null) {
			className = "";
		}
		if (keyId == null) {
			keyId = "";
		}
		return RedisUtils.KEY_PREFIX + className + RedisUtils.GET_ID + keyId;
	}

	public static String getTotalKey(String className, String keyId) {
		if (className == null) {
			className = "";
		}
		if (keyId == null) {
			keyId = "";
		}
		return RedisUtils.KEY_PREFIX + className + RedisUtils.TOTAL_KEY
				+ MD5Tools.MD5(keyId).substring(0, 20);
	}

	public static String getFindListKey(String className, String keyId) {
		if (className == null) {
			className = "";
		}
		if (keyId == null) {
			keyId = "";
		}
		return RedisUtils.KEY_PREFIX + className + RedisUtils.FIND_LIST_KEY
				+ MD5Tools.MD5(keyId).substring(0, 20);
	}

	public static String getFindListFirstKey(String className, String keyId) {
		if (className == null) {
			className = "";
		}
		if (keyId == null) {
			keyId = "";
		}
		return RedisUtils.KEY_PREFIX + className
				+ RedisUtils.FIND_LIST_FIRST_KEY
				+ MD5Tools.MD5(keyId).substring(0, 20);
	}

	public static String getFindPageKey(String className, String keyId) {
		if (className == null) {
			className = "";
		}
		if (keyId == null) {
			keyId = "";
		}
		return RedisUtils.KEY_PREFIX + className + RedisUtils.FIND_PAGE_KEY
				+ MD5Tools.MD5(keyId).substring(0, 20);
	}

	public static String getFindListKeyPattern(String className) {
		if (className == null) {
			className = "";
		}
		return RedisUtils.KEY_PREFIX + className + FIND_LIST_KEY_PATTERN;
	}

	public static String getFinPageKeyPattern(String className) {
		if (className == null) {
			className = "";
		}
		return RedisUtils.KEY_PREFIX + className + FIND_PAGE_KEY_PATTERN;
	}

	/**
	 * 获取token的有效期（秒）
	 * @param key
	 */
	public long getExpireTime(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 批量删除对应的value
	 *
	 * @param keys
	 */
	public void remove(final String... keys) {
		if (!run()) {
			return;
		}
		try {
			for (String key : keys) {
				remove(key);
			}
		} catch (Exception e) {
			logger.error("RedisUtils remove:{} {}", RUN_MESSAGE,
					e.getMessage());
		}
	}

	/**
	 * 批量删除key
	 *
	 * @param pattern
	 */
	public void removePattern(String pattern) {
		if (!run()) {
			return;
		}
		if (!listFlush()) {
			return;
		}
		try {
			if (pattern == null) {
				pattern = "";
			}
			Set<String> keys = getKyes(pattern);
			if (!keys.isEmpty()) {
				redisTemplate.delete(keys);
			}
		} catch (Exception e) {
			logger.error("RedisUtils removePattern:{} {}", RUN_MESSAGE,
					e.getMessage());
		}
	}

	public void removePatternShiroReids(String pattern) {
		if (!run()) {
			return;
		}
		if (!listFlush()) {
			return;
		}
		try {
			if (pattern == null) {
				pattern = "";
			}
			Set<String> keys = getKyesShiroReids(pattern);
			if (!keys.isEmpty()) {
				stringRedisTemplate.delete(keys);
				redisTemplate.delete(keys);
			}
		} catch (Exception e) {
			logger.error("RedisUtils removePattern:{} {}", RUN_MESSAGE,
					e.getMessage());
		}
	}
	/**
	 * 获取keys
	 *
	 * @param pattern
	 */
	public Set<String> getKyes(String pattern) {
		if (!run()) {
			return new HashSet<>();
		}
		try {
			if (pattern == null) {
				pattern = "";
			}
			Set<String> keys = stringRedisTemplate.keys("*" + pattern);
			Set<String> keysnew = new HashSet<>();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				keysnew.add(it.next().substring(7));
			}
			return keysnew;
		} catch (Exception e) {
			logger.error("RedisUtils getKyes:{} {}", RUN_MESSAGE,
					e.getMessage());
			return new HashSet<>();
		}
	}

	public Set<String> getKyesShiroReids(String pattern) {
		if (!run()) {
			return new HashSet<>();
		}
		try {
			if (pattern == null) {
				pattern = "";
			}
			Set<String> keys = stringRedisTemplate.keys("*" + pattern);
			Set<String> keysnew = new HashSet<>();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String tr = it.next();
				if (tr.contains(SHIRO_REDIS)) {
					keysnew.add(tr);
				} else if (tr.contains(SHIRO_REDIS_OBJECT)) {
					keysnew.add(tr.substring(8));
				}
			}
			return keysnew;
		} catch (Exception e) {
			logger.error("RedisUtils getKyes:{} {}", RUN_MESSAGE,
					e.getMessage());
			return new HashSet<>();
		}
	}

	public Set<String> getKyesAll() {
		if (!run()) {
			return new HashSet<>();
		}
		try {
			Set<String> keys = stringRedisTemplate.keys("*");
			Set<String> keysnew = new HashSet<>();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				keysnew.add(it.next());
			}
			return keysnew;
		} catch (Exception e) {
			logger.error("RedisUtils getKyes:{} {}", RUN_MESSAGE,
					e.getMessage());
			return new HashSet<>();
		}
	}

	/**
	 * 获取Count
	 *
	 */
	public int getCount() {
		if (!run()) {
			return 0;
		}
		try {
			Set<String> keys = stringRedisTemplate.keys("*");
			return keys.size();
		} catch (Exception e) {
			logger.error("RedisUtils getCount:{}{}", RUN_MESSAGE,
					e.getMessage());
			return 0;
		}
	}

	public int getCountShiro() {
		if (!run()) {
			return 0;
		}
		try {
			Set<String> keys = stringRedisTemplate.keys(SHIRO_REDIS + "*");
			return keys.size();
		} catch (Exception e) {
			logger.error("RedisUtils getCount:{}{}", RUN_MESSAGE,
					e.getMessage());
			return 0;
		}
	}
	/**
	 * 删除对应的value
	 *
	 * @param key
	 */
	public void remove(final String key) {
		if (!run()) {
			return;
		}
		try {
			if (key.contains(SHIRO_REDIS)) {
				stringRedisTemplate.delete(key);
			} else {
				redisTemplate.delete(key);
			}
		} catch (Exception e) {
			logger.error("RedisUtils exists:{}{}", RUN_MESSAGE, e.getMessage());
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 *
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		if (!run()) {
			return false;
		}
		boolean retuslt = false;
		try {
			if (key.contains(SHIRO_REDIS)) {
				retuslt = stringRedisTemplate.hasKey(key);
			} else {
				retuslt = redisTemplate.hasKey(key);
			}
		} catch (Exception e) {
			logger.error("RedisUtils exists:{}{}", RUN_MESSAGE, e.getMessage());
		}
		return retuslt;
	}

	/**
	 * 读取缓存
	 *
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		if (!run()) {
			return null;
		}
		Object result = null;
		try {
			if (key.contains(SHIRO_REDIS)) {
				ValueOperations<String, String> operationsString = stringRedisTemplate
						.opsForValue();
				result = operationsString.get(key);
			} else {
				ValueOperations<Serializable, Object> operations = redisTemplate
						.opsForValue();
				result = operations.get(key);
			}
			return result;
		} catch (Exception e) {
			logger.error("RedisUtils get:{}{}", RUN_MESSAGE, e.getMessage());
		}
		return result;
	}

	/**
	 * 写入缓存
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		if (!run()) {
			return false;
		}
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, EXPIRE_TIME, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			logger.error("RedisUtils set:{}{}", RUN_MESSAGE, e.getMessage());
		}
		return result;
	}

	/**
	 * 写入缓存
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		if (!run()) {
			return false;
		}
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			logger.error("RedisUtils set:{}{}", RUN_MESSAGE, e.getMessage());
		}
		return result;
	}

	public boolean set(final String key, Object value, Long expireTime,
			TimeUnit unit) {
		if (!run()) {
			return false;
		}
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, unit);
			result = true;
		} catch (Exception e) {
			logger.error("RedisUtils set:{}{}", RUN_MESSAGE, e.getMessage());
		}
		return result;
	}

	private boolean run() {
		return "true".equals(Global.getConfig(SPRING_REDIS_RUN));
	}

	public static boolean isRun() {
		return "true".equals(Global.getConfig(SPRING_REDIS_RUN));
	}

	public static boolean isShireRedis() {
		if (!"true".equals(Global.getConfig(SPRING_REDIS_RUN))) {
			return false;
		}
		return "true".equals(Global.getConfig("shiro.redis"));
	}

	private boolean listFlush() {
		return "true".equals(Global.getConfig("spring.redis.listFlush"));
	}
}