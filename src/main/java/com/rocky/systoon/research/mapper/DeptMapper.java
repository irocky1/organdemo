package com.rocky.systoon.research.mapper;

import com.rocky.systoon.research.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by rocky on 2018/3/8.
 */
@Mapper
public interface DeptMapper {

    Dept selectByPK(Integer id);

    Dept selectByName(String name);

    void updateRightByInsert(Integer right);

    void updateLeftByInsert(Integer right);

    int insert(Dept addParam);

    int update(Dept updateParam);

    int maxRight();

    int updateRightByInsertParent();

    int updateLeftByInsertParent();

    void deleteSoft(@Param("left") Integer left,@Param("right") Integer right);

    void deleteSoftRight(@Param("right") Integer right,@Param("middle") int middle);

    void deleteSoftLeft(@Param("right") Integer right,@Param("middle") int middle);

    List<Dept> getChildren(Dept dept);

    Dept getRoot();

    List<Dept> getAllChildren();

    int updateById(Dept dept);
}
