package com.zking.controller;

import com.zking.entity.Film;
import com.zking.entity.GitOAuth2User;
import com.zking.entity.Result;
import com.zking.entity.User;
import com.zking.service.IFilmService;
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
import java.util.List;

@PermitAll
@Controller
@RequiredArgsConstructor
public class MainController {

    private final AliPayUtil aliPayUtil;
    private final IUserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final IFilmService filmService;

    @GetMapping("fuzzyQuery")
    public String fuzzyQuery(String str,Model model){
        List<Film> films = filmService.fuzzyQuery(str);
        model.addAttribute("films",films);
        return "fuzzyQuery";
    }

    @GetMapping({"/", "/index"})
    public String index(Model model,Result vipInfo) {
        //if (vipInfo.getBoo() != null) {
        //
        //    model.addAttribute("vipInfo",vipInfo);
        //}
        return "index";
    }


    // ?????????????????????
    @GetMapping("/loginUser")
    public String login() {
        return "login";
    }

    // ?????????
    @GetMapping("/play")
    public String play(String movie, HttpSession session) {
        session.setAttribute("movie", movie);
        return "play";
    }

    //?????????gitee??????
    @RequestMapping("/login/oauth2/gitee")
    public String auth(Model model,
                       @RegisteredOAuth2AuthorizedClient("gitee") OAuth2AuthorizedClient client,
                       @AuthenticationPrincipal OAuth2User user) {
        System.out.println("?????????");
        System.out.println(user);
// ????????????????????????
        ClientRegistration reg = client.getClientRegistration();
        model.addAttribute("client", reg.getClientName());

        model.addAttribute("name", user.getName());
        model.addAttribute("attributes", user.getAttributes());
// ??????????????????????????????
        GitOAuth2User giteeUser = (GitOAuth2User) user;
        model.addAttribute("username", giteeUser.getLogin());

        System.out.println(giteeUser.getLogin());
        System.out.println(giteeUser.getName());
        System.out.println(giteeUser.getId());
        return "success";
    }

    // ????????????????????????
    @PreAuthorize("isAuthenticated()") // ???????????????????????????
    @GetMapping("/successUser")
    public String success() {
        return "success";
    }

    // 403??????????????????????????????
    @PreAuthorize("isAuthenticated()") // ???????????????????????????
    @GetMapping("/403")
    public String error() {
        return "/403";
    }

    /**
     * ????????????
     * @param user   ??????
     *               ?????????jcvqom6995@sandbox.com
     *               ?????????111111
     */
    @RequestMapping(path = "vip/buyVip/{month}/{price}")
    public String buyGame(RedirectAttributes redirect,@PathVariable Integer month, @PathVariable Integer price, Model model, @ModelAttribute("user") User user) throws ParseException {

        if (user == null){
            redirect.addFlashAttribute("vipInfo", Result.fail("???????????????"));
            return "redirect:/";
        }
        //??????redis?????????????????????key
        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps("user::vip");
        //?????????redis
        ops.set(month);

        String id = aliPayUtil.orderId();
        String form = aliPayUtil.pay(id, price.toString(), "vip??????");

        model.addAttribute("form",form);


        return "alipayTest";
    }




    // ??????????????????url????????????
    // ????????????
    @RequestMapping(path = "/vip/alipayReturn")
    public String returnUrl(RedirectAttributes redirect,String out_trade_no, Model model,@ModelAttribute("user") User user) {

        //??????redis?????????????????????key
        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps("user::vip");
        //?????????redis
        Integer month = (Integer) ops.get();
        if (month==null || month ==0){

            redirect.addFlashAttribute("vipInfo", Result.fail("vip????????????"));
            return "redirect:/";
        }

        //??????0????????????vip????????????
        if (user.getVip() > 0){
            String vipTime = user.getVipTime();
            //???????????????????????????vip??????
            user.setVipTime(DateUtil.getAfterMonth(vipTime, month));
        }else {
            user.setVip(1);
            user.setVipTime(DateUtil.getAfterMonth(new Date(), month));
        }
        userService.updateById(user);


        redirect.addFlashAttribute("vipInfo", Result.success("vip????????????"));


        return "redirect:/";
    }

}
