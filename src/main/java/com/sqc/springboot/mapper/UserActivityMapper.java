package com.sqc.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqc.springboot.entity.UserActivity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName UserActivityMapper
 * @Description
 * @Author Administrator
 * @Date 2024/1/31 15:38
 * @Version V1.0
 */
public interface UserActivityMapper extends BaseMapper<UserActivity> {
    @Select("select * from sys_user_activity where user_id=#{userId} and activity_id=#{activityId}")
    Integer selectUserByActivity(@Param("userId") Integer userId,@Param("activityId")Integer activityId);

    @Delete("delete from sys_user_activity where activity_id=#{activityId} and user_id=#{userId}")
    boolean removeUserIdAndActId(@Param("activityId")Integer activityId,@Param("userId") Integer userId);
}
