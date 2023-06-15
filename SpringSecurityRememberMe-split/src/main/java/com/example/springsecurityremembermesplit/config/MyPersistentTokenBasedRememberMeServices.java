package com.example.springsecurityremembermesplit.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

public class MyPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {



    public MyPersistentTokenBasedRememberMeServices(String key,
                                                    UserDetailsService userDetailsService,
                                                    PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    /**
     * 自定义前后端分离，获取 remember-me 的请求方式。如果不重写，默认是从 request 中获取请求参数的。
     * 由于是前后端分离的系统，我们需要改写成从 json 中获取请求参数
     * @param request the request submitted from an interactive login, which may include
     * additional information indicating that a persistent login is desired.
     * @param parameter the configured remember-me parameter name.
     * @return
     */
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        String paramValue = request.getAttribute(parameter).toString();
        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                return true;
            }
        }
        return false;
    }
}
