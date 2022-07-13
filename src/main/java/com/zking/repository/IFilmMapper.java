package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Actor;
import com.zking.entity.Film;
import com.zking.entity.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IFilmMapper extends BaseMapper<Film> {

    //查询所有电影类型
    List<Type> findAllType();
    //根据类型查询电影
    List<Film> findByType(int id);

    // 根据电影id查询电影的类型
    List<String> findAllTypeByFilmId(@Param("fid") int id);
    // 根据电影id查询电影的地区
    List<String> findAllRegionByFilmId(@Param("fid") int id);
    // 根据电影id查询电影的演员
    List<Actor> findAllActorByFilmId(@Param("fid") int id);

    //模糊查询根据电影名查电影
    List<Film> selectFilm(String selectInput);
}
