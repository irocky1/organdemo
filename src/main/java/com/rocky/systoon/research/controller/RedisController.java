package com.rocky.systoon.research.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rocky on 2018/1/23.
 */
@RequestMapping("/redis")
@RestController
public class RedisController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/get")
    public String get(String k) {
        Object object = redisTemplate.opsForValue().get(k);
        return null != object ? object.toString() : "";
    }

    @RequestMapping("/del")
    public String del(String k) {
        redisTemplate.delete(k);
        return "ok!";
    }

    @RequestMapping("/put")
    public String put(String k, String v) {
        stringRedisTemplate.opsForValue().set(k, v);
        return "ok!";
    }

    @RequestMapping("/bit")
    public String bit(String k, int offset, boolean b) {
        stringRedisTemplate.opsForValue().setBit(k, offset, b);
        return "ok";
    }

    @RequestMapping("/bitcount")
    public Long bitcount(String k){
        return null;
    }

}
