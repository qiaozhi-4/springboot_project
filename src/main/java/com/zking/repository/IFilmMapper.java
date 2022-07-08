package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Actor;
import com.zking.entity.Authority;
import com.zking.entity.Film;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IFilmMapper extends BaseMapper<Film> {
    // 根据电影id查询电影的类型
    List<String> findAllTypeByFilmId(@Param("fid") int id);
    // 根据电影id查询电影的地区
    List<String> findAllRegionByFilmId(@Param("fid") int id);
    // 根据电影id查询电影的演员
    List<Actor> findAllActorByFilmId(@Param("fid") int id);

}
