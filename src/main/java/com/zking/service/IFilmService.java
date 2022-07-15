package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.dto.FilmDTO;
import com.zking.entity.Film;

import java.util.List;

public interface IFilmService extends IService<Film>{
    //主页需要获取分类,以及这个分类的所有电影
    List<Object> getTypeAndFilm();

    List<FilmDTO> getAllFilms();

    //查询前五热度电影
    List<Film> selectHeat();
}
