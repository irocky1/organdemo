package com.rocky.me.research.service;

import com.rocky.me.research.entity.Dept;
import com.rocky.me.research.param.DeptParam;
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
public class DeptServiceTest {

    Logger logger = LoggerFactory.getLogger(DeptServiceTest.class);

    @Autowired
    DeptService deptService;

    @Test
    public void testSelect(){
        Dept dept = deptService.selectByPK(1);
        System.out.println(dept);
    }

    @Test
    public void insertOne(){
        Integer parentId = null;
        Integer nextBrotherId = null;
        Dept param = new Dept();
        param.setDeptName("大佬22");
        param.setOrgId(111);
        boolean isUpdate = false;
        int result = deptService.addDept(parentId,nextBrotherId,param,isUpdate);
        System.out.println(result);
    }

    @Test
    public void deleteOneAll(){
        Integer id = 1;
        deptService.deleteDept(id);
    }

    @Test
    public void getRoot(){
        Dept dept = deptService.getRoot();
        System.out.println(dept);
    }

    @Test
    public void getAllList(){
        Integer deptId = 28;
        List<DeptParam> list = deptService.getChildrenById(deptId);
        System.out.println(list);
    }

    @Test
    public void getchildren(){
       List<DeptParam> list =  deptService.getAllChildren();
       System.out.print(list);
    }


}
