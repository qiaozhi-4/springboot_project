package com.zking.controller;

import com.zking.entity.GitOAuth2User;
import com.zking.entity.Result;
import com.zking.entity.User;
import com.zking.service.IUserService;
import com.zking.util.AliPayUtil;
import com.zking.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;

@PermitAll
@Controller
@RequiredArgsConstructor
public class MainController {

    private final AliPayUtil aliPayUtil;
    private final IUserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping({"/", "/index"})
    public String index(Model model,Result vipInfo) {
        //if (vipInfo.getBoo() != null) {
        //
        //    model.addAttribute("vipInfo",vipInfo);
        //}
        return "index";
    }

    // 自定义登入页面
    @GetMapping("/loginUser")
    public String login() {
        return "login";
    }

    // 播放页
    @GetMapping("/play")
    public String play(String movie, HttpSession session) {
        session.setAttribute("movie", movie);
        return "play";
    }

    //第三方gitee登录
    @RequestMapping("/login/gitee")
    public String auth(Model model,
                       @RegisteredOAuth2AuthorizedClient("gitee") OAuth2AuthorizedClient client,
                       @AuthenticationPrincipal OAuth2User user) {
        System.out.println("我来了");
        System.out.println(user);
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

    /**
     * 沙箱支付
     * @param user   用户
     *               账号：jcvqom6995@sandbox.com
     *               密码：111111
     */
    @RequestMapping(path = "vip/buyVip/{month}/{price}")
    public String buyGame(RedirectAttributes redirect,@PathVariable Integer month, @PathVariable Integer price, Model model, @ModelAttribute("user") User user) throws ParseException {

        if (user == null){
            redirect.addFlashAttribute("vipInfo", Result.fail("你还未登录"));
            return "redirect:/";
        }
        //获取redis缓存，绑定这个key
        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps("user::vip");
        //设置进redis
        ops.set(month);

        String id = aliPayUtil.orderId();
        String form = aliPayUtil.pay(id, price.toString(), "vip购买");

        model.addAttribute("form",form);


        return "alipayTest";
    }




    // 购买游戏同步url返回路径
    // 同步地址
    @RequestMapping(path = "/vip/alipayReturn")
    public String returnUrl(RedirectAttributes redirect,String out_trade_no, Model model,@ModelAttribute("user") User user) {

        //获取redis缓存，绑定这个key
        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps("user::vip");
        //设置进redis
        Integer month = (Integer) ops.get();
        if (month==null || month ==0){

            redirect.addFlashAttribute("vipInfo", Result.fail("vip购买失败"));
            return "redirect:/";
        }

        //大于0表示已有vip就加时间
        if (user.getVip() > 0){
            String vipTime = user.getVipTime();
            //调用方法给用户添加vip时间
            user.setVipTime(DateUtil.getAfterMonth(vipTime, month));
        }else {
            user.setVip(1);
            user.setVipTime(DateUtil.getAfterMonth(new Date(), month));
        }
        userService.updateById(user);


        redirect.addFlashAttribute("vipInfo", Result.fail("vip购买成功"));


        return "redirect:/";
    }

}
