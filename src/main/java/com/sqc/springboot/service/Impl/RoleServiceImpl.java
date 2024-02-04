package com.sqc.springboot.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqc.springboot.entity.Menu;
import com.sqc.springboot.entity.RoleMenu;
import com.sqc.springboot.mapper.RoleMenuMapper;
import com.sqc.springboot.service.MenuService;
import com.sqc.springboot.service.RoleService;
import org.springframework.stereotype.Service;
import com.sqc.springboot.entity.Role;
import com.sqc.springboot.mapper.RoleMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hikktn
 * @since 2023-11-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;
    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        roleMenuMapper.deleteByRoleId(roleId);//先删除当前角色id所有绑定关系
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);//再把前端传过来的菜单id绑定到当前这个角色id上去
        for (Integer menuId : menuIds){
            Menu menu = menuService.getById(menuId);
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())){//二级菜单并且传过来的menuId数组里面没有它的父级id
                RoleMenu roleMenu = new RoleMenu();//那么我们要补充一个父级id
                roleMenu.setMenuId(menu.getPid());
                roleMenu.setRoleId(roleId);
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
