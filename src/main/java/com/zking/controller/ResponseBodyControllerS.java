package com.zking.controller;

import com.zking.service.IFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@Controller
@RequiredArgsConstructor
public class ResponseBodyControllerS {

    private final IFilmService filmService;

    @GetMapping("getTypeAndFilm")
    public List<Object> index() {
        return filmService.getTypeAndFilm();
    }
}
