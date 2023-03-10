package com.zking.controller;

import com.zking.entity.Comment;
import com.zking.entity.User;
import com.zking.repository.ICommentMapper;
import com.zking.repository.IUserMapper;
import com.zking.service.IFilmService;
import com.zking.service.ITypeService;
import com.zking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PreAuthorize("hasRole('user')")
    @GetMapping("/userIndex")
    public String userInfo(@ModelAttribute("user") User user, Model model){
        model.addAttribute("adminGetUser",user);
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

}
