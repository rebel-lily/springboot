package com.sqc.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.sqc.springboot.common.Result;
import com.sqc.springboot.entity.User;
import com.sqc.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EchartsController
 * @Description
 * @Author Administrator
 * @Date 2023/11/27 20:21
 * @Version V1.0
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private UserService userService;

    @GetMapping("/example")
    public Result get(){
        Map<String,Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon","Tue","Wed","Thu","Fri","Sat","Sun"));
        map.put("y", CollUtil.newArrayList(150,230,225,218,143,269,147));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result members(){
        List<User> users = userService.list();
        //一年四个季度
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for (User user : users){
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter){
                case Q1 : q1 += 1;break;
                case Q2 : q2 += 1;break;
                case Q3 : q3 += 1;break;
                case Q4 : q4 += 1;break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }
}
