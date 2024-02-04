package com.sqc.springboot.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sqc.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @ClassName UserDto
 * @Description
 * @Author Administrator
 * @Date 2023/11/18 18:19
 * @Version V1.0
 * 接收前端登录请求的参数
 */
@Data
@JsonIgnoreProperties(value = "password", allowSetters = true)
public class UserDto {
   private String id;
   private String username;
   private String password;
   private String nickname;
   private String avatar;
   private String token;
   private String role;
   private String headName;
   private List<Menu> menus;
}
