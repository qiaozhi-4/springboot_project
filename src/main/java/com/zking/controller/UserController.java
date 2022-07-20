package com.zking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.dto.FilmDTO;
import com.zking.entity.Comment;
import com.zking.entity.Film;
import com.zking.entity.Type;
import com.zking.entity.User;
import com.zking.repository.ICommentMapper;
import com.zking.repository.IFilmMapper;
import com.zking.repository.IUserMapper;
import com.zking.service.IFilmService;
import com.zking.service.ITypeService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.zking.service.IUserService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final IUserService userSevice;
    private final IUserMapper userMapper;
    private final IFilmService filmService;
    private final ITypeService typeService;
    private final ICommentMapper commentMapper;
    @Value("${upload.locationImg}")
    private String location;
    private final PasswordEncoder encoder;


    //测试页面
    @GetMapping("/test")
    public String updateUser(){
        return "test";
    }

    @GetMapping("/userIndex")
    public String userInfo(){
        return "user/userIndex";
    }

    //视频页面
    @GetMapping("/find")
    public String findVideo(){
        return "findVideo";
    }

    // 模糊查询视频

    //查评论
    @ResponseBody
    @GetMapping("findC")
    public Map<String,Object> find(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        List<Comment> comment = commentMapper.findAllCommentByFilmId(id);
        Map<String,Object> p = new HashMap<>();
        p.put("comment",comment);
        return p;
    }

    //修改用户
    @RequestMapping("/address")
    public String updateUse(){
        return "countryStateCity";
    }
    //修改用户信息
    @PostMapping("/updateUsers")
    public String updateUser(HttpServletRequest request, MultipartFile file, String name, String address, String email , String password) throws IOException, ServletException, ServletException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User principal =(User) authentication.getPrincipal();
        if (!Objects.equals(file.getOriginalFilename(), "")) {
            String imgname = UUID.randomUUID() + file.getOriginalFilename();
            String path = "/" + imgname;
            File dest = new File(location, path);
            file.transferTo(dest);
            principal.setHeadImg(path);
        }
        //密码加密
        String pass = encoder.encode(password);
        principal.setPassword(pass);
        principal.setName(name);
        principal.setAddress(address);
        principal.setEmail(email);
        //修改
        userSevice.saveOrUpdate(principal);
        request.logout(); // 强制登出
        return "redirect:index";
    }


    //用户的封号
    @GetMapping("deleteUser")
    public String delete(){
        //当前登录的用户
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User principal =(User) authentication.getPrincipal();
        principal.setVip(-1);
        userMapper.updateById(principal);
        return "redirect:test";
    }

}
