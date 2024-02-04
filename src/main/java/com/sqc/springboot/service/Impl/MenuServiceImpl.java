package com.sqc.springboot.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqc.springboot.entity.Menu;
import com.sqc.springboot.mapper.MenuMapper;
import com.sqc.springboot.service.MenuService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hikktn
 * @since 2023-11-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("id");
        List<Menu> list = list(queryWrapper);//查询所有数据
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());//找出pid为null的一级菜单
        for (Menu menu : parentNodes){//找出一级菜单的子菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList())); //筛选所有数据中pid=父级id的数据就是二级菜单
        }
        return parentNodes;
    }
}
