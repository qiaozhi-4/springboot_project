package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.dto.FilmDTO;
import com.zking.entity.Film;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IFilmService extends IService<Film>{

    //<!--用户收藏电影-->
    boolean addLikeFilm(@Param("userId") int userId, @Param("filmId") int filmId);
    //<!--用户观看电影-->
    boolean addLookFilm(@Param("userId") int userId,@Param("filmId") int filmId);
    //<!--查询用户是否收藏这个电影-->
    boolean isLikeFilm(@Param("userId") int userId,@Param("filmId") int filmId);
    //<!--查询用户是否看过这个电影-->
    boolean isLookFilm(@Param("userId") int userId,@Param("filmId") int filmId);
    //<!--查询用户的收藏电影-->
    List<Film> userLikeFilm(int id);
    //<!--查询用户的观看历史-->
    List<Film> userHistory(int id);

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
