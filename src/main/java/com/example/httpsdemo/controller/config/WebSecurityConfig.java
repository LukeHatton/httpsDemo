package com.example.httpsdemo.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>Spring Security配置
 *
 * @author lizhao 2023/3/10
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  /**
   * 定义哪些URL应该被spring security保护，哪些不需要。在这个例子中，只有"/"和"/home"是不需要保护的
   * <p>
   * 当用户成功登录后，他会被重定向会登录前查看的页面。我们通过{@code loginPage()}配置了一个login页面，这个页面
   * 允许任何用户访问
   * <p>
   * spring security已经有一个预设的login controller了，因此使用时只需要添加登录页面"/login"即可
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    HttpSecurity security = httpSecurity.authorizeHttpRequests(registry -> registry
        .regexMatchers("/", "/home")
        .permitAll()
        .anyRequest()
        .authenticated())
      .formLogin(form -> form
        .loginPage("/login")
        .permitAll())
      .logout(LogoutConfigurer::permitAll);

    return security.build();
  }

  /**
   * 配置一个内存中的用户信息表，用来进行用户认证。
   * 当前的配置只初始化了一个用户user，他的用户名、密码、角色都是预设的
   */
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()    // 生产环境下使用这个方法是不安全的
      .username("user")
      .password("password")
      .roles("USER")
      .build();

    return new InMemoryUserDetailsManager(user);
  }
}
