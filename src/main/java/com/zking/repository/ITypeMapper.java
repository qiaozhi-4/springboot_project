package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Film;
import com.zking.entity.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ITypeMapper extends BaseMapper<Type> {
    List<Film> findAllFilmByTypeId(@Param("types") List<Type> types);
}
