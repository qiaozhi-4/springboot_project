package com.zking.controller;

import com.zking.entity.GitOAuth2User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.PermitAll;

@PermitAll
@Controller
public class MainController
{
    @GetMapping({"/","/index"})
    public String index()
    {
        return "index";
    }

    // 自定义登入页面
    @GetMapping("/loginUser")
    public String login() {
        return "login";
    }

    // 播放页
    @GetMapping("/play")
    public String play() {
        return "play";
    }
    //第三方gitee登录
    @RequestMapping("/login/gitee")
    public String auth(Model model,
                       @RegisteredOAuth2AuthorizedClient("gitee") OAuth2AuthorizedClient client,
                       @AuthenticationPrincipal OAuth2User user) {
        System.out.println("我来了");
// 第三方的配置信息
        ClientRegistration reg = client.getClientRegistration();
        model.addAttribute("client", reg.getClientName());

        model.addAttribute("name", user.getName());
        model.addAttribute("attributes", user.getAttributes());
// 详细信息（自定义类）
        GitOAuth2User giteeUser = (GitOAuth2User) user;
        model.addAttribute("username", giteeUser.getLogin());

        System.out.println(giteeUser.getLogin());
        System.out.println(giteeUser.getName());
        System.out.println(giteeUser.getId());
        return "success";
    }

    // 登入成功后的页面
    @PreAuthorize("isAuthenticated()") // 必须登录后才能访问
    @GetMapping("/successUser")
    public String success() {
        return "success";
    }

    // 403错误页面（权限不足）
    @PreAuthorize("isAuthenticated()") // 必须登录后才能访问
    @GetMapping("/403")
    public String error() {
        return "/403";
    }
}
