package com.zking.advice;

import com.zking.entity.GitOAuth2User;
import com.zking.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// @Component // 这里不用@Component这些注解！
@ControllerAdvice(/*basePackages = {"com.zking.*"}, // 指定包*/annotations = {Controller.class})  // 指定被增强注解类型
// @ResponseBody
public class MyAdviceController {

    // 控制层全局获取用户信息 @ModelAttribute
    @ModelAttribute
    public void model(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User uInfo = null;
        if (principal instanceof User) {
            uInfo = (User) principal;
        }
        if (principal instanceof GitOAuth2User){
            uInfo = (User) principal;
        }
        model.addAttribute("user" ,uInfo);
    }


}
