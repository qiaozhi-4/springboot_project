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
    List<Film> findFilmsByTypeId(int id);

    // 根据电影id查询电影的类型
    List<String> findAllTypeByFilmId(@Param("fid") int id);
    // 根据电影id查询电影的地区
    String findAllRegionByFilmId(@Param("fid") int id);
    // 根据电影id查询电影的演员
    List<Actor> findAllActorByFilmId(@Param("fid") int id);

    //模糊查询根据电影名查电影
    List<Film> selectFilm(@Param("name") String selectInput);

    //查询电影前五的热度
    List<Film> selectHeat();

    //将电影ID与类型ID插入数据库
    int insert1(@Param("film") int film ,@Param("type") int type);
    //将电影ID与演员ID插入数据库
    int insert2(@Param("film") int film , @Param("actor")int actor);


    //设置电影类型
    int addFilmType(@Param("filmId")Integer filmId,  @Param("types")Integer[] types);

    //删除电影的类型
    int deleteFilmType(Integer filmId);


    //设置电影演员
    int addFilmActor(@Param("filmId")Integer filmId,  @Param("actors")Integer[] actors);

    //删除电影的演员
    int deleteFilmActor(Integer filmId);
}
