package com.feng.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feng.security.exception.KaptchaNotMatchException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

// 自定义 filter
public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {
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

        try {
            // 1. 获取请求中的验证码
            Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String verifyCode = userInfo.get(getKaptchaParameter());
            String username = userInfo.get(getUsernameParameter());
            String password = userInfo.get(getPasswordParameter());
            // 2. 获取session 中的验证码
            String sessionKaptchaCode = (String)request.getSession().getAttribute("kaptcha");
            if (!ObjectUtils.isEmpty(verifyCode) && !ObjectUtils.isEmpty(sessionKaptchaCode)
                    && verifyCode.equals(sessionKaptchaCode)) {
                // 3. 获取用户名和密码认证
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                setDetails(request, authenticationToken);
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       throw new KaptchaNotMatchException("验证码不匹配");
    }

}
