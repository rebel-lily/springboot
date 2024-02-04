package com.sqc.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName Dict
 * @Description
 * @Author Administrator
 * @Date 2023/11/29 14:30
 * @Version V1.0
 */
@TableName("sys_dict")
@Data
public class Dict {
    private String name;
    private String value;
    private String type;
}
