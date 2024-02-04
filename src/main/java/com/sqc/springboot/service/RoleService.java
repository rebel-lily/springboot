package com.sqc.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqc.springboot.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hikktn
 * @since 2023-11-28
 */
public interface RoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
