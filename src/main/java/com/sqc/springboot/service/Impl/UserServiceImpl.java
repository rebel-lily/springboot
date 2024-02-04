package com.sqc.springboot.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqc.springboot.common.Constants;
import com.sqc.springboot.controller.dto.UserDto;
import com.sqc.springboot.entity.Menu;
import com.sqc.springboot.entity.User;
import com.sqc.springboot.exception.ServiceException;
import com.sqc.springboot.mapper.RoleMapper;
import com.sqc.springboot.mapper.RoleMenuMapper;
import com.sqc.springboot.mapper.UserMapper;
import com.sqc.springboot.service.MenuService;
import com.sqc.springboot.service.UserService;
import com.sqc.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author Administrator
 * @Date 2023/11/18 18:25
 * @Version V1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Log LOG = Log.get();

    @Autowired
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuService menuService;
    @Override
    public UserDto login(UserDto userDto) {
        User one = getUserInfo(userDto);
        if(one != null){//先判断one是否为空
            BeanUtil.copyProperties(one,userDto,true);
            String token = TokenUtils.getToken(one.getId().toString(),one.getPassword());//设置token
            userDto.setToken(token);

            String role = one.getRole();

            List<Menu> roleMenus = getRoleMenus(role);//设置用户菜单列表
            userDto.setMenus(roleMenus);
            return userDto;
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    @Override
    public User register(UserDto userDto) {
        User one = getUserInfo(userDto);
        if(one == null){
            one = new User();
            BeanUtil.copyProperties(userDto,one,true);//先判断one是否为空
            save(one);//存到数据库
        }else {
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return null;
    }



    private User getUserInfo(UserDto userDto){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username",userDto.getUsername());//设置查询条件
        queryWrapper.eq("password",userDto.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }


    private List<Menu> getRoleMenus(String roleFlag){ //获取当前角色的菜单列表
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);//当前角色所有菜单id
        List<Menu> menus = menuService.findMenus("");//查出系统所有的菜单
        List<Menu> roleMenus = new ArrayList<>();//最后筛选完成之后的list
        for (Menu menu : menus){//筛选当前用户角色的菜单
            if (menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getId()));//移除children里面不在menuIds集合中的元素
        }
        return roleMenus;
    }



}
