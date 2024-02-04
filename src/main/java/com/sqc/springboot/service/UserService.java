package com.sqc.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqc.springboot.controller.dto.UserDto;
import com.sqc.springboot.entity.User;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description
 * @Author Administrator
 * @Date 2023/11/14 15:12
 * @Version V1.0
 */
@Service
public interface UserService extends IService<User> {

    UserDto login(UserDto userDto);

    User register(UserDto userDto);


}
