package com.sqc.springboot.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.sqc.springboot.common.Constants;
import com.sqc.springboot.entity.User;
import com.sqc.springboot.exception.ServiceException;
import com.sqc.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName JwtInterceptor
 * @Description
 * @Author Administrator
 * @Date 2023/11/21 9:59
 * @Version V1.0
 */
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = request.getHeader("token");
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        if (StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401,"无token,请重新登录");
        }

        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException J){
            throw new ServiceException(Constants.CODE_401,"token验证失败");
        }

        User user = userService.getById(userId);
        if (user == null){
            throw new ServiceException(Constants.CODE_401,"用户不存在，请重新登录");
        }

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        }catch (JWTDecodeException J){
            throw new ServiceException(Constants.CODE_401,"token验证失败");
        }
        return true;
    }
}
