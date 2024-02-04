package com.sqc.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName File
 * @Description
 * @Author Administrator
 * @Date 2023/11/23 9:32
 * @Version V1.0
 */
@Data
@TableName("sys_file")
public class Files {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String type;
    private long size;
    private String url;
    private Boolean isDelete;
    private Boolean enable;
    private String md5;
}
