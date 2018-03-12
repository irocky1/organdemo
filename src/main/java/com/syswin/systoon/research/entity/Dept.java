package com.syswin.systoon.research.entity;

import java.io.Serializable;

/**
 * Created by rocky on 2018/3/9.
 */
public class Dept implements Serializable{

    /**
     * 主键
     */
    private Integer id;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 组织id
     */
    private Integer orgId;
    /**
     * 左值
     */
    private Integer left;
    /**
     * 右值
     */
    private Integer right;
    /**
     * 逻辑删除 1是 0否
     */
    private Integer flag;
    /**
     * 层级
     */
    private Integer deep;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getRight() {
        return right;
    }

    public void setRight(Integer right) {
        this.right = right;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", deptName='" + deptName + '\'' +
                ", orgId=" + orgId +
                ", left=" + left +
                ", right=" + right +
                ", flag=" + flag +
                ", deep=" + deep +
                '}';
    }
}
