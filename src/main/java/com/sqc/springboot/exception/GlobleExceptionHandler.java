package com.sqc.springboot.exception;

import com.sqc.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ExceptionHandler
 * @Description
 * @Author Administrator
 * @Date 2023/11/20 15:50
 * @Version V1.0
 */
@ControllerAdvice
public class GlobleExceptionHandler {

   @ExceptionHandler(ServiceException.class)
   @ResponseBody
   public Result handle(ServiceException se){
       return Result.error(se.getCode(),se.getMessage());
   }
}
