package com.zking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.dto.UserCount;
import com.zking.entity.*;
import com.zking.service.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DenyAll
@Controller
@RequiredArgsConstructor
public class adminController {
    private final UserService userService;
    private final FilmService filmService;
    private final FileService fileService;
    private final TypeService typeService;
    private final ActorService actorService;

    @RolesAllowed("admin")
    @GetMapping("adminIndex")
    public String adminIndex(){
        return "/admin/adminIndex";
    }

    @RolesAllowed("admin")
    @GetMapping("adminFilm")
    public String adminFilm(){
        return "/admin/adminFilm";
    }

    @RolesAllowed("admin")
    @GetMapping("adminUser")
    public String adminUser(){
        return "/admin/adminUser";
    }

    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/userCount")
    @ResponseBody
    public List<UserCount> userCount() {
        int vipCount = userService.list(new QueryWrapper<User>().eq("vip", 1)).size();
        UserCount vip = new UserCount(vipCount, "VIP用户");
        int commonCount = userService.list(new QueryWrapper<User>().eq("vip", 0)).size();
        UserCount common = new UserCount(commonCount, "普通用户");
        int bannedCount = userService.list(new QueryWrapper<User>().eq("vip", -1)).size();
        UserCount banned = new UserCount(bannedCount, "封禁用户");
        List<UserCount> userCounts = new ArrayList<>();
        userCounts.add(vip);
        userCounts.add(common);
        userCounts.add(banned);
        return userCounts;
    }

    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/recentLoginUser")
    @ResponseBody
    public List<User> recentLoginUser() {
        // 获取当前时间减去一个月
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1); //当前时间减去一个月
        //当前时间减去一个月后的时间
        String format = simpleDateFormat.format(calendar.getTime());
        return userService.list(new QueryWrapper<User>().gt("last_login_time", format));
    }
}
