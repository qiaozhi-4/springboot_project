package com.zking.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zking.entity.GitOAuth2User;
import com.zking.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer customizer() {
        return web ->
                web.ignoring()
                        .antMatchers("/images/**", "/css/**", "/js/**", "/fonts/**", "/favicon.*");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.oauth2Login()
                .redirectionEndpoint()
                .baseUri("/login/gitee") // 和配置相 匹配
                .and()
                .userInfoEndpoint(u -> u.userService(oauth2UserService()));


        //http.formLogin()
        //        .loginPage("/loginUser")
        //        .loginProcessingUrl("/login")
        //        .failureUrl("/loginUser?error")
        //        .defaultSuccessUrl("/");
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/loginUser?logout");
        http.exceptionHandling().accessDeniedPage("/403");


        //异常处理配置
        http.exceptionHandling()
                //异常处理
                .authenticationEntryPoint((request, response, authException) -> {
                    // 和上面一致，用来处理ajax请求哪些没有权限的资源时：
                    String header = request.getHeader("X-Requested-With");
                    //判断是否是axios异步请求
                    if ("XMLHttpRequest".equals(header)) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                    } else {
                        response.sendRedirect("/");
                    }
                });

        //登录配置
        http.formLogin()
                //.loginPage("/loginUser")
                .loginProcessingUrl("/login")
                //.failureUrl("/loginUser?error")
                //.defaultSuccessUrl("/")


                //替换登录成功的页面
                .successHandler((request, response, authentication) -> {
                    // 和上面配置一致，处理成功后返回结果
                    String header = request.getHeader("x-requested-with");
                    //判断是否是axios异步请求
                    if ("XMLHttpRequest".equals(header)) {
                        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(user));
                        return;
                    }

                    response.sendRedirect("/");
                })

                //密码错误替换登录失败
                .failureHandler((request, response, e) -> {
                    // 和上面配置一致，处理成功后返回结果
                    String header = request.getHeader("x-requested-with");
                    //判断是否是axios异步请求
                    if ("XMLHttpRequest".equals(header)) {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.write(e.getMessage());
                        out.flush();
                        out.close();
                        return;
                    }
                    response.sendRedirect("/loginUser?error");
                })
                .and()
                .addFilterAt(new GenericFilter() {
                    //判断极验是否登录处理拦截器
                    @Override
                    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                            HttpServletRequest request = (HttpServletRequest) servletRequest;
                            HttpServletResponse response = (HttpServletResponse) servletResponse;
                            if (("/login").equals(request.getRequestURI())){
                                HttpSession session = request.getSession();
                                System.out.println(session.getAttribute("validate"));
                                //判断第三方登录
                                if(request.getParameter("manner") == null){
                                    filterChain.doFilter(servletRequest, servletResponse);
                                    return;
                                }

                                if(session.getAttribute("validate") == null ||session.getAttribute("validate").equals("false")){
                                    System.out.println("没有验证");
                                    response.sendRedirect("/loginUser?error");
                                    return;
                                }
                                session.removeAttribute("validate");
                                filterChain.doFilter(servletRequest, servletResponse);
                                return;
                            }
                            filterChain.doFilter(servletRequest, servletResponse);
                        }
                }, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return request -> {
            String url = request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION,
                    "Bearer " +
                            request.getAccessToken().getTokenValue()
            );
            HttpEntity<String> entity = new HttpEntity<>("", headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return mapToUser(response.getBody());
        };
    }

    private GitOAuth2User mapToUser(Map<String, Object> attributes) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(attributes, GitOAuth2User.class);
    }
}
