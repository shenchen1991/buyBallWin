package com.shenchen.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;


public class RedisPoolJava {
    private static JedisPool pool;
    //静态代码初始化池配置
    static {
        try{
            Properties props = new Properties();
            props.load(RedisPoolJava.class.getClassLoader().getResourceAsStream("redis.properties"));
            //创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();
            //设置池配置项值
            config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));
            config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));
            config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));
            //根据配置实例化jedis池
            pool = new JedisPool(config, props.getProperty("redis.ip"), Integer.valueOf(props.getProperty("redis.port")));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得jedis对象
     * @return
     */
    public static Jedis getJedisObject(){
        return pool.getResource();
    }

    /**
     * 归还jedis对象
     * @param jedis
     */
    public static void recycleJedisOjbect(Jedis jedis){
        jedis.close();

    }

    /**
     * 存储数据
     * @param key
     * @param value
     */
    public static void setValue(String key,String value){
        Jedis jedis = null;
        try{
            jedis = getJedisObject();//获得jedis实例
            jedis.setex(key,600,value);
        }finally {
            if(jedis != null){
                recycleJedisOjbect(jedis);
            }

        }

    }

    /**
     * 获取数据
     * @param key
     * @return
     */
    public static String getValue(String key){
        Jedis jedis = null;
        try{
            jedis = getJedisObject();//获得jedis实例
            String resultStr = jedis.get(key);
            return resultStr;
        }finally {
            if(jedis != null){
                recycleJedisOjbect(jedis);
            }
        }
    }


}