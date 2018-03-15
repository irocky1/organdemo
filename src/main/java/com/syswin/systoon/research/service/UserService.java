package com.syswin.systoon.research.service;

import com.alibaba.fastjson.JSON;
import com.syswin.systoon.research.entity.User;
import com.syswin.systoon.research.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by rocky on 2018/3/13.
 */
@Service
@EnableTransactionManagement
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String SINGLE_USER_PREFIX = "tob-single-user:";
    public User selectByPK(Integer id){
        if(null == redisTemplate.opsForValue().get(SINGLE_USER_PREFIX+id)){
            User user = userMapper.selectByPK(id);
            redisTemplate.opsForValue().set(SINGLE_USER_PREFIX+id,user);
            return user;
        }else{
            return  (User)redisTemplate.opsForValue().get(SINGLE_USER_PREFIX+id);
        }
    }

    public User selectByName(String name){
        return userMapper.selectByName(name);
    }

    /**
     * 增加用户
     * @param user
     * @return
     */
    public int insertUser(User user){

        return userMapper.insert(user);
    }

    /**
     * 查询所有用户
     * @return
     */
    private final static String ALL_USER_PREFIX ="tob-list-user";
    public List<User> getAllUser(){
        if(null == redisTemplate.opsForValue().get(ALL_USER_PREFIX)){
            List<User> userList = userMapper.queryAllUser();
            redisTemplate.opsForValue().set(ALL_USER_PREFIX,userList);
            return userList;
        }else{
            return  (List<User>)redisTemplate.opsForValue().get(ALL_USER_PREFIX);
        }
    }

    /**
     * 设置全部可见
     */
    public void setFullReleation() {
        List<User> userList = getAllUser();
        for(User user : userList){
            //设置每个用户的关系
            setUserColleage(user,userList);
        }

    }

    /**
     * 设置单个用户的关系
     * @param user 当前用户
     * @param userList 可见用户
     */
    private final static String USER_PREFIX = "tob-cm-user:";
    private void setUserColleage(User user, List<User> userList) {
        String userKey = USER_PREFIX + user.getId();

        //TODO:批量执行redis 提高性能,改成lua脚本方式
        for(User thisUser : userList){
            //当前用户，可见用户，是否可见
            redisTemplate.opsForValue().setBit(userKey,thisUser.getId(),true);
        }

    }

    /**
     * 解除所有关系
     */
    public void unbindReleation() {
        List<User> userList = getAllUser();
        List<String> keys = new ArrayList<>();
        for(User user : userList){
            //设置每个用户的关系
            keys.add(USER_PREFIX+user.getId());
            redisTemplate.delete(keys);
        }
    }

    /**
     * 获取可见同事列表
     * @param id
     * @return
     */
    public List<User> getColleage(Integer id) {

        List<User> result = new ArrayList<>();
        String key = USER_PREFIX+id;

        for (int offset=0;offset<35;offset++){
            boolean isColleage = redisTemplate.opsForValue().getBit(key,offset);
            //如果是同事
            if(isColleage){
                User user = selectByPK(offset);
                if(null!=user){
                    result.add(user);
                }
            }
        }
        return result;
    }
}
