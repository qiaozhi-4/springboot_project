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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
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

        //第三方登录配置
        http.oauth2Login()
                .redirectionEndpoint()
                .baseUri("/login/gitee") // 和配置相 匹配
                .and()
                .userInfoEndpoint(u -> u.userService(oauth2UserService()));

        //登录配置
        http.formLogin()
                .loginPage("/loginUser")
                .loginProcessingUrl("/login")
                .failureUrl("/loginUser?error")
                .defaultSuccessUrl("/");

        //登出配置
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/loginUser?logout");

        //异常处理配置
        http.exceptionHandling().accessDeniedPage("/403")
                .and()

                //异常处理
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    // 和上面一致，用来处理ajax请求哪些没有权限的资源时：
                    String header = request.getHeader("X-Requested-With");
                    if (header != null) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                    } else {
                        response.sendRedirect("/login.html");
                    }
                });

        //表单登录配置
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/aaa.html")
                .loginProcessingUrl("/login")
                .usernameParameter("name")
                .passwordParameter("passwd")

                //登录成功
                .successHandler((request, response, authentication) -> {
                    // 和上面配置一致，处理成功后返回结果
                    String header = request.getHeader("x-requested-with");
                    if (header != null) {
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(new User()));
                        return;
                    }
                    response.sendRedirect("/");
                })

                //密码错误
                .failureHandler((req, res, e) -> {
                    res.setContentType("application/json;charset=utf-8");
                    PrintWriter out = res.getWriter();
                    out.write(e.getMessage());
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .csrf().disable() ;

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
