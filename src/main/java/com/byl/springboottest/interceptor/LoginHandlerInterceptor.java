package com.byl.springboottest.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Object username = request.getSession().getAttribute("username");//存入session
//        if (username == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return false;//拦截
//        } else {
            return true;//放行
//        }
    }

}
