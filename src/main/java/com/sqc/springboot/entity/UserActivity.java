package com.sqc.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName UserActivity
 * @Description
 * @Author Administrator
 * @Date 2024/1/31 15:34
 * @Version V1.0
 */
@TableName("sys_user_activity")
@Data
public class UserActivity {
    private Integer userId;
    private Integer activityId;
}
