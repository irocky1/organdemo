package com.rocky.systoon.research.controller;

import com.rocky.systoon.research.entity.Dept;
import com.rocky.systoon.research.param.DeptParam;
import com.rocky.systoon.research.param.TreeNode;
import com.rocky.systoon.research.response.CommonResult;
import com.rocky.systoon.research.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocky on 2018/1/23.
 */
@RequestMapping("/dept")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 获取完整组织树json
     * @return
     */
    @RequestMapping("/treeJson")
    public String getChildrenJson(){
//        Dept root = deptService.getRoot();
//        List<DeptParam> list = deptService.getChildrenById(root.getId());
        List<DeptParam> list = deptService.getAllChildren();
        List<TreeNode> nodes = buildTree(list);
        return nodes.toString();
    }

    /**
     * 二次循环建树
     * @param list
     * @return
     */
    private List<TreeNode> buildTree(List<DeptParam> list) {
        List<TreeNode> nodeList = new ArrayList<>();
        for(DeptParam deptParam : list){
            TreeNode node = new TreeNode();
            node.setParentId(deptParam.getParentId());
            node.setParent(deptParam.getParent());
            node.setText(deptParam.getDeptName());
            node.setId(deptParam.getId());
            nodeList.add(node);
        }

        List<TreeNode> trees = new ArrayList<>();
        for (TreeNode treeNode : nodeList) {
            if (0==treeNode.getParentId()) {
                trees.add(treeNode);
            }
            for (TreeNode it : nodeList) {
                if (it.getParentId() == treeNode.getId()) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<TreeNode>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }

    /**
     * 增加部门
     * @param parentId
     * @param deptName
     * @param brotherId
     * @return
     */
    @RequestMapping("/add")
    public String addDept(@RequestParam(value = "pid",required = false) Integer parentId,@RequestParam("name") String deptName,@RequestParam(value = "bid",required = false) Integer brotherId){
        try {
            Dept dept = new Dept();
            dept.setDeptName(deptName);
            dept.setOrgId(111);
            int result = deptService.addDept(parentId, brotherId, dept, false);
            if (result > 0) {
                return CommonResult.success().toString();
            }
            return CommonResult.fail().toString();
        }catch (Exception e){
            return CommonResult.fail(e.getMessage()).toString();
        }
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @RequestMapping("/del")
    public String delDept(@RequestParam("id") Integer id){
        try {
            deptService.deleteDept(id);
            return CommonResult.success().toString();
        }catch (Exception e){
            return CommonResult.fail(e.getMessage()).toString();
        }
    }

    /**
     * 更新部门
     * @param id
     * @param deptName
     * @return
     */
    @RequestMapping("/update")
    public String updateDept(@RequestParam("id") Integer id,@RequestParam("name") String deptName){
        try {
            Dept dept = new Dept();
            dept.setId(id);
            dept.setDeptName(deptName);
            dept.setOrgId(111);
            deptService.updateDeptName(dept);
            return CommonResult.success().toString();
        }catch (Exception e){
            return CommonResult.fail().toString();
        }
    }


}
