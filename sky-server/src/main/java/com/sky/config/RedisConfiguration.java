package com.sky.config;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author wyj
 * @version 1.0
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建redis模板对象");
        RedisTemplate redisTemplate = new RedisTemplate();

        //设置连接工厂对象

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置 redis 的key序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());


        return redisTemplate;


    }

}
