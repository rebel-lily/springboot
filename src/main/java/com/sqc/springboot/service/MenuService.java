package com.sqc.springboot.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqc.springboot.entity.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hikktn
 * @since 2023-11-28
 */
public interface MenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
