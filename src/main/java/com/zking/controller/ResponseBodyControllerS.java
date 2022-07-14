package com.zking.controller;

import com.zking.service.IFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@RestController
@RequiredArgsConstructor
public class ResponseBodyControllerS {

    private final IFilmService filmService;

    @GetMapping("/getTypeAndFilm")
    public List<Object> index() {
        return filmService.getTypeAndFilm();
    }
}
