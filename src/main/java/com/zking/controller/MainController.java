package com.zking.controller;

import com.zking.entity.GitOAuth2User;
import com.zking.entity.User;
import com.zking.service.IUserService;
import com.zking.util.AliPayUtil;
import com.zking.util.DateUtil;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@PermitAll
@Controller
@RequiredArgsConstructor
public class MainController {

    private final AliPayUtil aliPayUtil;
    private final IUserService userService;

    @GetMapping({"/", "/index"})
    public String index() {
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
     *
     * @param model
     * @param user   用户
     * @return
     */
    @RequestMapping(path = "vip/buyVip")
    public String buyGame(Integer month, Model model, @SessionAttribute("user") User user) throws ParseException {

        String id = aliPayUtil.orderId();
        /*String form = alipayUtil.pay(id, price, subject, String.valueOf(gid));*/
        // 操作 把订单信息放入redis缓存
        if (user.getVip() > 0){
            String vipTime = user.getVipTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(vipTime);
            user.setTime(DateUtil.getAfterMonth(date, month));
        }else {
            user.setVip(1);
            user.setTime(DateUtil.getAfterMonth(new Date(), month));
        }
        userService.updateById(user);

        return "alipayTest";
    }


    // 购买游戏同步url返回路径
    // 同步地址
    @RequestMapping(path = "vip/alipayReturn")
    public String returnUrl(String out_trade_no, Model model, @SessionAttribute("uInfo") User uInfo) {


        //Integer userId = uInfo.getUserId();
        //
        //// 支付成功 支付成功就去redis缓存获取订单并插入数据库
        //UserBuy userBuy = userBuyService.getUserBuy(userId);
        //System.out.println(userBuy);


        return "alipayTest";


        // 支付失败 清除redis缓存
        // userBuyService.cachedInvalidation(userId);
        //return "alipayTest";
    }
}
