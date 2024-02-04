package com.sqc.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqc.springboot.entity.Activity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 橘子
 * @since 2024-01-24
 */
public interface ActivityService extends IService<Activity> {
    void setUserActivity(Integer activityId, Integer userId);

    Integer isUserAttended(Integer userId,Integer activityId);

    boolean removeActAndUser(Integer activityId,Integer userId);

    Integer selectCountActivity(Integer activityId);
}
