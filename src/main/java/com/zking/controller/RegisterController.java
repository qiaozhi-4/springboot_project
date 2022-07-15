package com.zking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.zking.entity.Film;
import com.zking.entity.User;
import com.zking.service.IFilmService;
import com.zking.service.IUserService;
import com.zking.service.impl.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@PermitAll
@Controller
@RequiredArgsConstructor
public class RegisterController {

    @Value("${upload.locationImg}")
    private String location;

    private final IUserService userService;
    private final PasswordEncoder encoder;
    private final IFilmService filmService;


    //注册页面
    @GetMapping("register")
    public String register() {
        return "register";
    }

    @PostMapping("registerForm")
    public String registerForm(MultipartFile file, String username, String password, String name, Character sex, Model model) throws IOException, ParseException {
        model.addAttribute("info", "");
        if (file.getOriginalFilename().equals("")) {
            model.addAttribute("info", "头像不为空");
            return "register";
        }
        if (username == null && password == null && name == null) {
            model.addAttribute("info", "请填写必要信息");
            return "register";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pass = encoder.encode(password);
        String parse = dateFormat.format(new Date());
        String imgname = UUID.randomUUID() + file.getOriginalFilename();
        String path = "/" + imgname;
        File dest = new File(location, path);
        file.transferTo(dest);
        System.out.println("path => " + path);


        User user = new User(0, username, pass, "/img" + path, name, sex, 0, null, null, null, parse, null, null);
        boolean save = userService.save(user);
        if (!save) {
            model.addAttribute("info", "注册失败请重试");
            return "register";
        }
        return "login";
    }


    //查看用户名是否存在
    @ResponseBody
    @GetMapping("/isusers")
    public boolean isusers(String name) {
        User user = userService.getOne(new QueryWrapper<User>().eq("userName", name), false);
//        System.out.println(user.toString());
        //user为空，说明没有找到该用户，可以注册。反之。
        return user == null;
    }


    //查询电影前五热度
    @ResponseBody
    @GetMapping("/selectHeat")
    public List<Film> selectHeat() {
        return filmService.selectHeat();
    }

}
