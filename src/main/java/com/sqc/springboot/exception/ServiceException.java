package com.sqc.springboot.exception;

import lombok.Getter;

/**
 * @ClassName ServiceException
 * @Description
 * @Author Administrator
 * @Date 2023/11/20 15:54
 * @Version V1.0
 * 自定义异常
 */
@Getter
public class ServiceException extends RuntimeException{
    private String code;

    public ServiceException(String code,String msg){
        super(msg);
        this.code = code;
    }
}
