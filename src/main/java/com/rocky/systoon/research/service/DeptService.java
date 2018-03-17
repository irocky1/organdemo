package com.rocky.systoon.research.service;

import com.rocky.systoon.research.entity.Dept;
import com.rocky.systoon.research.fault.BusinessException;
import com.rocky.systoon.research.mapper.DeptMapper;
import com.rocky.systoon.research.param.DeptParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * 左右值无限级分类，也称为预排序树无限级分类，是一种有序的树状结构，
 * 位于这些树状结构中的每一个节点都有一个“左值”和“右值”，
 * 其规则是：每一个后代节 点的左值总是大于父类，右值总是小于父级，右值总是大于左值。
 * 处于这些结构中的每一个节点，都可以轻易的算出其祖先或后代节点。
 * 因此，可以用它来实现无限分类。
 * 优点：通过一条SQL就可以获取所有的祖先或后代，这在复杂的分类中非常必要，
 * 通过简单的四则运算就可以得到后代的数量.
 * 由于这种方法不使用递归查询算法，有更高的查询效率,采用左右值编码的设计方案，
 * 在进行树的遍历时，由于只需进行2次查询，消除了递归，再加上查询条件都为数字比较，效率极高。
 * 这种算法比较高端，是mysql官方推荐的算法
 *
 * Created by rocky on 2018/3/8.
 * @author rocky
 *
 */
@Service
@EnableTransactionManagement
public class DeptService {

    /**
     * mapper
     */
    @Autowired
    private DeptMapper deptMapper;

    /**
     * 通过主键查询部门详细信息
     * @param id
     * @return
     */
    public Dept selectByPK(Integer id){
        return deptMapper.selectByPK(id);
    }

    /**
     * 根据部门名称查询部门详细信息
     * @param name
     * @return
     */
    public Dept selectByName(String name){
        return deptMapper.selectByName(name);
    }

    /**
     * 获取根部门
     * @return
     */
    public Dept getRoot(){
        return deptMapper.getRoot();
    }

    /**
     * 插入一个部门
     * @param parentId 父部门id
     * @param brotherId 下一个兄弟部门id（如果没有兄弟部门，可不传）
     * @param deptParam 新插入的部门信息
     * @param isUpdate 是否是更新插入
     * @return
     */
    public int addDept(Integer parentId, Integer brotherId, Dept deptParam, boolean isUpdate){

        if(StringUtils.isBlank(deptParam.getDeptName())){
            throw new BusinessException("部门名称不能为空");
        }

        Dept dept =this.selectByName(deptParam.getDeptName());
        if(null!=dept){
            throw new BusinessException("部门名称已存在");
        }

        int result = 0;
        /**
         * 1.有父部门，没有兄弟部门（最小的子节点，父节点的最后一个儿子）
         */
        if(null != parentId && null == brotherId){
            Dept parentDept = this.selectByPK(parentId);
            if(null==parentDept){
                throw new BusinessException("父部门不存在");
            }
            //新节点
            Dept newDept = new Dept();
            newDept.setDeptName(deptParam.getDeptName());
            newDept.setLeft(parentDept.getRight());
            newDept.setRight(parentDept.getRight()+1);
            newDept.setOrgId(deptParam.getOrgId());
            if(isUpdate){
                //更新
                deptMapper.updateRightByInsert(parentDept.getRight());
                deptMapper.updateLeftByInsert(parentDept.getRight());
                result = deptMapper.update(newDept);
            }else{
                //插入
                deptMapper.updateRightByInsert(parentDept.getRight());
                deptMapper.updateLeftByInsert(parentDept.getRight());
                deptMapper.insert(newDept);
                result = newDept.getId();
            }
        }

        /**
         * 2.有父部门，也有兄弟部门
         */
        if(null != parentId && null != brotherId){
            Dept brother = this.selectByPK(brotherId);
            //新节点
            Dept newDept = new Dept();
            newDept.setLeft(brother.getLeft());
            newDept.setRight(brother.getLeft()+1);
            newDept.setDeptName(deptParam.getDeptName());
            newDept.setOrgId(deptParam.getOrgId());

            if(isUpdate){
                //更新
                deptMapper.updateRightByInsert(brother.getLeft());
                deptMapper.updateLeftByInsert(brother.getLeft());
                result = deptMapper.update(newDept);
            }else{
                //插入
                deptMapper.updateRightByInsert(brother.getLeft());
                deptMapper.updateLeftByInsert(brother.getLeft());
                deptMapper.insert(newDept);
                result = newDept.getId();
            }
        }

        /**
         * 3.无父部门，也无兄弟部门（大佬，根节点）
         */
        if(null == parentId && null == brotherId){
            Integer maxRight = deptMapper.maxRight();
            //新节点
            Dept newDept = new Dept();
            newDept.setLeft(1);
            newDept.setRight(maxRight+2);
            newDept.setDeptName(deptParam.getDeptName());
            newDept.setOrgId(deptParam.getOrgId());

            if(isUpdate){
                //更新
                deptMapper.updateRightByInsertParent();
                deptMapper.updateLeftByInsertParent();
                result = deptMapper.update(newDept);
            }else{
                //插入
                deptMapper.updateRightByInsertParent();
                deptMapper.updateLeftByInsertParent();
                deptMapper.insert(newDept);
                result = newDept.getId();
            }
        }
        return result;
    }


    /**
     * 删除一个部门,默认都是逻辑删除
     * @param id
     * @return
     */
    public void deleteDept(Integer id){
        if(null == id){
            throw new BusinessException("id不能为空");
        }
        Dept dept = this.selectByPK(id);
        if(null == dept){
            throw new BusinessException("部门不存在");
        }
        if(dept.getRight()!=dept.getLeft()+1){
            throw new BusinessException("不是叶子节点");
        }

        int middle = dept.getRight() - dept.getLeft() + 1;

        deptMapper.deleteSoft(dept.getLeft(),dept.getRight());
        deptMapper.deleteSoftRight(dept.getRight(),middle);
        deptMapper.deleteSoftLeft(dept.getRight(),middle);
    }

    /**
     * 获取指定部门的所有子部门
     * @param deptId 部门id
     * @return
     */
    public List<DeptParam> getChildrenById(Integer deptId){

        if(null == deptId) {
            throw new BusinessException("部门id不能为空");
        }
        Dept thisDept = this.selectByPK(deptId);
        if(null == thisDept){
            throw new BusinessException("部门不存在");
        }

        List<DeptParam> result = new LinkedList<>();
        List<Count> counts = new LinkedList<>(); //计数器
        List<Dept> deptList =  deptMapper.getChildren(thisDept);

        for(Dept dept : deptList){
            if(counts.size()>0){
                //检查是否应该移出堆栈
                for(int i=0; i<counts.size(); i++){
                    if(counts.get(i).getRight() < dept.getRight()){
                        counts.remove(i);
                    }
                }
            }

            String parent = counts.size()>0?counts.get(counts.size()-1).getPrev():"";
            Integer parentId = counts.size()>0?counts.get(counts.size()-1).getPrevId():0;
            DeptParam param = new DeptParam();
            param.setId(dept.getId());
            param.setDeptName(dept.getDeptName());
            param.setLevel(counts.size());
            param.setParent(parent);
            param.setParentId(parentId);
            result.add(param);

            Count c = new Count();
            c.setRight(dept.getRight());
            c.setPrev(dept.getDeptName());
            c.setPrevId(dept.getId());
            counts.add(c);
        }

        return result;
    }

    /**
     * 获取所有部门
     * @return
     */
    public List<DeptParam> getAllChildren(){

        List<DeptParam> result = new LinkedList<>();
        List<Count> counts = new LinkedList<>(); //计数器
        List<Dept> deptList =  deptMapper.getAllChildren();

        for(Dept dept : deptList){
            if(counts.size()>0){
                //检查是否应该移出堆栈
                for(int i=counts.size()-1; i>0; i--){
                    if(counts.get(i).getRight() < dept.getRight()){
                        counts.remove(i);
                    }
                }
            }

            String parent = counts.size()>0?counts.get(counts.size()-1).getPrev():"";
            Integer parentId = counts.size()>0?counts.get(counts.size()-1).getPrevId():0;
            DeptParam param = new DeptParam();
            param.setId(dept.getId());
            param.setDeptName(dept.getDeptName());
            param.setLevel(counts.size());
            param.setParent(parent);
            param.setParentId(parentId);
            result.add(param);

            Count c = new Count();
            c.setRight(dept.getRight());
            c.setPrev(dept.getDeptName());
            c.setPrevId(dept.getId());
            counts.add(c);
        }

        return result;
    }


    /**
     * 更新部门名称
     * @param dept
     */
    public int updateDeptName(Dept dept) {
        return deptMapper.updateById(dept);
    }
}

/**
 * 计数器数据结构
 */
class Count{
    private Integer right;
    private String prev;
    private Integer prevId;

    public Integer getRight() {
        return right;
    }

    public void setRight(Integer right) {
        this.right = right;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public Integer getPrevId() {
        return prevId;
    }

    public void setPrevId(Integer prevId) {
        this.prevId = prevId;
    }
}
