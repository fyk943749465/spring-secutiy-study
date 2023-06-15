package com.feng.security.filter;

import com.feng.exception.KaptchaNotMatchException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KaptchaFilter extends UsernamePasswordAuthenticationFilter {


    public static final String SPRING_SECURITY_CAPTCHA_KEY = "kaptcha";

    private String kaptchaParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 1. 从请求中获取验证码
        // 2. 与 Session 中的验证码进行比较
        String verifyCode = request.getParameter(getKaptchaParameter());  // 用户输入的验证码
        Object sessionKaptchaCode = request.getSession().getAttribute("kaptcha");  // session 中的验证码
        if (!ObjectUtils.isEmpty(verifyCode) && !ObjectUtils.isEmpty(sessionKaptchaCode)
            && verifyCode.equals(sessionKaptchaCode)) {
            return super.attemptAuthentication(request, response);
        }
        throw new KaptchaNotMatchException("验证码不匹配");
    }
}
