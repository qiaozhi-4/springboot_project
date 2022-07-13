package com.zking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zking.entity.Comment;
import com.zking.entity.Film;
import com.zking.entity.User;
import com.zking.repository.ICommentMapper;
import com.zking.repository.IFilmMapper;
import com.zking.repository.IUserMapper;
import com.zking.service.IFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.zking.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final IUserService userSevice;
    private final IFilmMapper filmMapper;
    private final IFilmService filmService;
    private final IUserMapper userMapper;
    private final ICommentMapper commentMapper;
    private final PasswordEncoder encoder;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    //测试页面
    @GetMapping("/test")
    public String updateUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println(authentication.getPrincipal());
        return "test";
    }

    //视频页面
    @GetMapping("/find")
    public String findVideo(){
        return "findVideo";
    }

    // 模糊查询视频
    @ResponseBody
    @GetMapping ("findVideo")
    public Map<String,Object> findVideos(@RequestParam String selectInput) {
        List<Film> data = filmMapper.selectFilm(selectInput);
        System.out.println(data);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        return res;
    }
    @ResponseBody
    @GetMapping("findC")
    public Map<String,Object> find(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        List<Comment> comment = commentMapper.findAllCommentByFilmId(id);
        Map<String,Object> p = new HashMap<>();
        p.put("comment",comment);
        return p;
    }

    //修改用户信息
    @GetMapping("/updateUser")
    public String updateUser(Model model, Integer imgId, String name, String address, String email , String password){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User principal =(User) authentication.getPrincipal();
        if (imgId == null) {
            return "updateUser";
        }
        //密码加密
        String pass = encoder.encode(password);
        principal.setPass(pass);
        principal.setImgId(imgId);
        principal.setName(name);
        principal.setAddress(address);
        principal.setEmail(email);
        //修改
        userSevice.saveOrUpdate(principal);
        model.addAttribute("info","修改成功");
        return "updateUser";
    }
}
