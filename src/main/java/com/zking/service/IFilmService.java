package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.dto.FilmDTO;
import com.zking.entity.Film;

import java.util.List;

public interface IFilmService extends IService<Film>{

    //用户模糊查询电影名
    List<Film> fuzzyQuery(String s);

    //主页需要获取分类,以及这个分类的所有电影
    List<Object> getTypeAndFilm();

    List<FilmDTO> getAllFilms();

    FilmDTO findAllTypeByFilmId(Film film);

    //模糊查询根据电影名查电影
    List<FilmDTO> selectFilm(String selectInput, String selectType);

    //查询前五热度电影
    List<Film> selectHeat();

    //电影添加
    boolean addFilms(Film film,Integer[] actors, Integer[] types);

    //电影更改类型
    boolean updateFilmType(Integer filmId, Integer[] types);

    //电影更改演员
    boolean updateFilmActor(Integer filmId, Integer[] actors);

}
