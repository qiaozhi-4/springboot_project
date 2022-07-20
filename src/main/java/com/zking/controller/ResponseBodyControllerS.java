package com.zking.controller;

import com.zking.entity.Film;
import com.zking.entity.User;
import com.zking.service.IFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@RestController
@RequiredArgsConstructor
public class ResponseBodyControllerS {

    private final IFilmService filmService;

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
