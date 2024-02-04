package com.sqc.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqc.springboot.common.Result;
import com.sqc.springboot.entity.Activity;
import com.sqc.springboot.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 橘子
 * @since 2024-01-24
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;


    @PostMapping
    public Result save(@RequestBody Activity activity) {
        return Result.success(activityService.saveOrUpdate(activity));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String activityName){
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();


        if (!"".equals(activityName)){
            queryWrapper.like("activityName",activityName);
        }

        return Result.success(activityService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(activityService.removeById(id));
    }

    @DeleteMapping("/delAct/{id}/{userId}")
    public Result delAct(@PathVariable Integer id,@PathVariable Integer userId){
        return Result.success(activityService.removeActAndUser(id,userId));
    }

    @PostMapping("/del/batch/")
    public Result deleteSome(@RequestBody List<Integer> ids){
        return Result.success(activityService.removeByIds(ids));
    }

    @PostMapping("/attend/{id}")
    public Result attend(@PathVariable Integer id,@RequestBody Integer userId){
        activityService.setUserActivity(id,userId);
        return Result.success();
    }

    @PostMapping("/isAttended/{userId}")
    public Result isAttended(@PathVariable Integer userId,@RequestBody Integer activityId){
        Integer count = activityService.isUserAttended(userId,activityId);
        if (count != null){
            return Result.success();
        }else {
            return Result.error();
        }
    }

    @PostMapping("/countAct/{activityId}")
    public Result countAct(@PathVariable Integer activityId){
        Integer count = activityService.selectCountActivity(activityId);
        System.out.println("查的纪录条数" + count);
        if (count != null){
            return Result.success(count);
        }else {
            return Result.error();
        }
    }
}
