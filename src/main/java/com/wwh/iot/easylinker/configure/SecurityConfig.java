package com.wwh.iot.easylinker.configure;

/**
 * Created by wwhai on 2017/7/27.
 */

import com.wwh.iot.easylinker.configure.security.*;
import com.wwh.iot.easylinker.service.AppUserDetailService;
import com.wwh.iot.easylinker.service.EasyLinkerOpenEntityManagerInViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.channel.ChannelProcessor;

/**
 * 安全机制配置入口
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    EasyLinkerOpenEntityManagerInViewFilter easyLinkerOpenEntityManagerInViewFilter;
    @Autowired
    AppUserDetailService appUserDetailService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/user/login/",
                "/logOut",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/loginPage",
                "/static/**",
                "/js/**",
                "/css/**",
                "/images/**",
                "/assets/**",
                "/new_index/**",
                "/qrcode/**",
                "/upload/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(
                "/user/signup",
                "/document",
                "/index",
                "/signupPage"
                ,"/loginFailed",
                "/device/*",
                "/apiv1/*",
                "/ifUserExist",
                "/qrcode/*").permitAll();
        http.authorizeRequests().anyRequest().authenticated()
                .and().formLogin().loginPage("/user/login")
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailedHandler())
                .usernameParameter("loginpara").passwordParameter("password")
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index")
                .permitAll()
                .and().rememberMe()
                .and().exceptionHandling()
                .authenticationEntryPoint(new DefaultAuthenticationEntry())
                .and().csrf().disable()
                .addFilterBefore(easyLinkerOpenEntityManagerInViewFilter,ChannelProcessingFilter.class);
    }

    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailService);
    }

    /**
     * 自定义UserDetailsService，从数据库中读取用户信息
     *
     * @return
     */
    @Bean
    public AppUserDetailService customUserDetailsService() {
        return new AppUserDetailService();
    }


}
