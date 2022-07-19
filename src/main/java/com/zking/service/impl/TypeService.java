package com.zking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.dto.FilmDTO;
import com.zking.entity.Film;
import com.zking.entity.Type;
import com.zking.repository.IFilmMapper;
import com.zking.repository.ITypeMapper;
import com.zking.service.IFilmService;
import com.zking.service.ITypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
//@RequiredArgsConstructor

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
@Service
@RequiredArgsConstructor
public class TypeService extends ServiceImpl<ITypeMapper, Type> implements ITypeService {
    private final IFilmMapper filmMapper;

//    根据类型id查询所有的电影
    @Override
    public List<FilmDTO> findAllFilmByTypeId(List<Type> types) {
        List<FilmDTO> filmDTOS = new ArrayList<>();
        //模糊查询电影
        List<Film> films = getBaseMapper().findAllFilmByTypeId(types);
        for (Film film : films) {
            StringBuilder stringBuilder = new StringBuilder();
            //查询电影所有类型
            filmMapper.findAllTypeByFilmId(film.getId()).forEach(type -> stringBuilder.append(type).append(","));
            filmDTOS.add( FilmDTO.getFilmDTO(film,stringBuilder.toString()));
        }
        return filmDTOS;
    }
}
