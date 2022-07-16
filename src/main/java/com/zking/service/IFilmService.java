package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.dto.FilmDTO;
import com.zking.entity.Film;

import java.util.List;

public interface IFilmService extends IService<Film>{
    //主页需要获取分类,以及这个分类的所有电影
    List<Object> getTypeAndFilm();

    List<FilmDTO> getAllFilms();

    FilmDTO findAllTypeByFilmId(Film film);

    //查询前五热度电影
    List<Film> selectHeat();

    //电影添加
    boolean addFilms(Film film,Integer[] actors, Integer[] types);

    //电影更改类型
    boolean updateFilmType(Integer filmId, Integer[] types);

    //电影更改演员
    boolean updateFilmActor(Integer filmId, Integer[] actors);

}
