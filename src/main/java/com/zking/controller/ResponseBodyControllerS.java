package com.zking.controller;

import com.zking.entity.Film;
import com.zking.entity.User;
import com.zking.service.IFilmService;
import com.zking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@PermitAll
@RestController
@RequiredArgsConstructor
public class ResponseBodyControllerS {

    private final IFilmService filmService;
    private final IUserService userService;

    //用户修改头像
    @PostMapping("/userUpdateHead")
    public Boolean userUpdateHead(Integer userId, MultipartFile file) throws IOException {
        if (file != null) {
            User user = new User();
            user.setId(userId);
            //把文件传入本地
            String path = "/img/" + UUID.randomUUID() + file.getOriginalFilename();
            File dest = new File("D:\\springboot", path);
            file.transferTo(dest);
            user.setHeadImg(path);
            return userService.updateById(user);
        }
        return false;
    }

    //用户修改头像
    @PostMapping("/userUpdateInfo")
    public Boolean userUpdateInfo(User user){
            return userService.updateById(user);
    }

    //获取电影和类型
    @GetMapping("/getTypeAndFilm")
    public List<Object> index() {
        return filmService.getTypeAndFilm();
    }

    //用户收藏电影
    @GetMapping("/addLikeFilm")
    public Boolean addLikeFilm(Integer userId,Integer filmId) {
        return filmService.addLikeFilm(userId,filmId);
    }

    //查询用户喜欢的电影
    @GetMapping("/userLikeFilm")
    public List<Film> userLikeFilm(@ModelAttribute("user") User user) {
        return filmService.userLikeFilm(user.getId());
    }

    //查询用看过的电影
    @GetMapping("/userHistory")
    public List<Film> userHistory(@ModelAttribute("user") User user) {
        return filmService.userHistory(user.getId());
    }
}
