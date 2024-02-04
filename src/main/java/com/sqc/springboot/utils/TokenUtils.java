package com.sqc.springboot.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sqc.springboot.entity.User;
import com.sqc.springboot.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName TokenUtils
 * @Description
 * @Author Administrator
 * @Date 2023/11/21 9:39
 * @Version V1.0
 * 生成token
 */
@Component
public class TokenUtils {

   private static UserService staticUserService;

   @Resource
   private UserService userService;

   @PostConstruct
   public void setUserService(){
      staticUserService = userService;
   }
   public static String getToken(String userId,String sign){
      return JWT.create().withAudience(userId).//将userid保存到token,作为数据
                    withExpiresAt(DateUtil.offsetHour(new Date(),2)).//2小时后token过期
                    sign(Algorithm.HMAC256(sign));//以password作为token的密钥
   }

   public static User getCurrentUser(){ //通过token获取用户信息
        try {
           HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
           String token = request.getHeader("token");
           if (StrUtil.isNotBlank(token)){
              String userId = JWT.decode(token).getAudience().get(0);
              return staticUserService.getById(Integer.valueOf(userId));
           }
        }catch (Exception e){
           return null;
        }
        return null;
   }
}
