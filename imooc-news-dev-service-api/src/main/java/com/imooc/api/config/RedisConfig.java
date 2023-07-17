package com.imooc.api.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Program: imooc-news-dev
 * @ClassName: RedisConfig
 * @Author: JH
 * @Date: 2023-07-17 14:31
 * @Description:
 */
@Configuration
public class RedisConfig {
    /**
     * retemplate相关配置
     * @param redisConnectionFactory
     * @return
     */
    @Primary
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // System.out.println(template.getConnectionFactory());

        // json 序列化配置
        Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY); // 识别所有字段 PropertyAccessor 识别任何字段 JsonAutoDetect
        // objectMapper.enableDefaultTyping();  // enableDefaultTyping 已过期
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jsonRedisSerializer.setObjectMapper(objectMapper);


        // String 的 序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key 采用 String的序列化
        template.setKeySerializer(stringRedisSerializer);
        // hash 的key采用String的序列化
        template.setHashKeySerializer(stringRedisSerializer);
        // hash 的 value 采用 String 的序列化
        template.setHashValueSerializer(jsonRedisSerializer);
        // value 序列化方式采用 Jackson
        template.setValueSerializer(stringRedisSerializer);

        template.afterPropertiesSet();

        return template;
    };
}
