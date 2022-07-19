package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.dto.FilmDTO;
import com.zking.entity.Film;
import com.zking.entity.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ITypeService extends IService<Type> {
    //    根据类型id查询所有的电影
    List<FilmDTO> findAllFilmByTypeId(List<Type> types);

}
