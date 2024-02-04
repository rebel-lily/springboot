package com.sqc.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName RoleMenu
 * @Description
 * @Author Administrator
 * @Date 2023/11/30 8:31
 * @Version V1.0
 */
@TableName("sys_role_menu")
@Data
public class RoleMenu {
    private Integer roleId;
    private Integer menuId;
}
