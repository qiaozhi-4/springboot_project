package com.zking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.dto.FilmDTO;
import com.zking.entity.Actor;
import com.zking.entity.Film;
import com.zking.entity.Type;
import com.zking.repository.IFilmMapper;
import com.zking.service.IFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
//@RequiredArgsConstructor

@Service
@RequiredArgsConstructor
public class FilmService extends ServiceImpl<IFilmMapper, Film> implements IFilmService {




    //主页需要获取分类,以及这个分类的所有电影
    @Override
    public List<Object> getTypeAndFilm() {
        List<Object> list = new LinkedList<>();
        //查询所有的分类
        List<Type> types = getBaseMapper().findAllType();
        for (Type type : types) {
            Map<String, Object> map = new HashMap<>();
            map.put("type",type);
            //查询每个分类的所有电影
            map.put("films",getBaseMapper().findFilmsByTypeId(type.getId()));
            list.add(map);
        }
        return list;
    }


    //admin需要获取电影以及他的所有类型
    @Override
    public List<FilmDTO> getAllFilms() {
        List<FilmDTO> filmDTOS = new ArrayList<>();
        //查询所有电影
        List<Film> list = list();
        for (Film film : list) {
            StringBuilder stringBuilder = new StringBuilder();
            //查询电影所有类型
            getBaseMapper().findAllTypeByFilmId(film.getId()).forEach(type -> stringBuilder.append(type).append(","));
            filmDTOS.add( FilmDTO.getFilmDTO(film,stringBuilder.toString()));
        }
        return filmDTOS;
    }

    @Override
    public FilmDTO findAllTypeByFilmId(Film film){
        StringBuilder stringBuilder = new StringBuilder();
        //查询电影所有类型
        getBaseMapper().findAllTypeByFilmId(film.getId()).forEach(type -> stringBuilder.append(type).append(","));
        return FilmDTO.getFilmDTO(film,stringBuilder.toString());
    }

    //模糊查询根据电影名查电影
    @Override
    public List<FilmDTO> selectFilm(String selectInput, String selectType) {
        List<FilmDTO> filmDTOS = new ArrayList<>();
        //模糊查询电影
        List<Film> films;
        if (selectType.equals("")) {
            films = list(new QueryWrapper<Film>().like("name", selectInput).or().like("region", selectInput));
        }else {
            films = list(new QueryWrapper<Film>().like(selectType,selectInput));
        }
        for (Film film : films) {
            StringBuilder stringBuilder = new StringBuilder();
            //查询电影所有类型
            getBaseMapper().findAllTypeByFilmId(film.getId()).forEach(type -> stringBuilder.append(type).append(","));
            filmDTOS.add( FilmDTO.getFilmDTO(film,stringBuilder.toString()));
        }
        return filmDTOS;
    }

    @Override
    public List<Film> selectHeat() {
        return getBaseMapper().selectHeat();
    }

    //电影添加
    @Override
    public boolean addFilms(Film film, Integer[] actors, Integer[] types) {
        boolean save = save(film);
        Integer filmId = film.getId();
        getBaseMapper().addFilmType(filmId,types);
        getBaseMapper().addFilmActor(filmId,actors);
        return save;
    }

    //电影更改类型
    @Override
    public boolean updateFilmType(Integer filmId, Integer[] types) {
        getBaseMapper().deleteFilmType(filmId);
        return getBaseMapper().addFilmType(filmId, types) != 0;
    }

    //电影演员更改
    @Override
    public boolean updateFilmActor(Integer filmId, Integer[] actors) {
        getBaseMapper().deleteFilmActor(filmId);
        return getBaseMapper().addFilmActor(filmId,actors) != 0;
    }






    public List<Actor> findAllActorByFilmId(int id){
        return getBaseMapper().findAllActorByFilmId(id);
    }


}
