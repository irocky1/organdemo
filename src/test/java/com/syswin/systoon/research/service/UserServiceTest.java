package com.syswin.systoon.research.service;

import com.syswin.systoon.research.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by rocky on 2018/1/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class UserServiceTest {

    Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void batchInsert(){
        for (int i = 0;i<3000;i++){
            User user  = new User();
            user.setOrgId(111);
            user.setUserName("rocky"+i);
            userService.insertUser(user);
        }
    }

    @Test
    public void queryuser(){
        List<User> list = userService.getAllUser();
        System.out.println(list);
    }

    @Test
    public void getByPK(){
        User user =  userService.selectByPK(1);
        System.out.print(user);
    }

    @Test
    public void getColleage(){
       List<User> list = userService.getColleage(1);
       System.out.println(list);
    }
}
