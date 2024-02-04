package com.sqc.springboot.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqc.springboot.entity.UserActivity;
import com.sqc.springboot.mapper.ActivityMapper;
import com.sqc.springboot.mapper.UserActivityMapper;
import com.sqc.springboot.service.ActivityService;
import org.springframework.stereotype.Service;
import com.sqc.springboot.entity.Activity;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 橘子
 * @since 2024-01-24
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Resource
    private UserActivityMapper userActivityMapper;


    @Transactional
    @Override
    public void setUserActivity(Integer activityId, Integer userId) {

        UserActivity userActivity = new UserActivity();//创建一个新的对象存放活动id和用户id
        userActivity.setActivityId(activityId);
        userActivity.setUserId(userId);
        userActivityMapper.insert(userActivity); //存放对象
    }

    @Override
    public Integer isUserAttended(Integer userId, Integer activityId) {
        return userActivityMapper.selectUserByActivity(userId,activityId);
    }

    @Override
    public boolean removeActAndUser(Integer activityId, Integer userId) {
        return userActivityMapper.removeUserIdAndActId(activityId,userId);
    }

    @Override
    public Integer selectCountActivity(Integer activityId) {
        QueryWrapper<UserActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id",activityId);
        return userActivityMapper.selectCount(queryWrapper);
    }


}
