package com.zking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.entity.Actor;
import com.zking.entity.Film;
import com.zking.entity.Type;
import com.zking.repository.IFilmMapper;
import com.zking.service.IFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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


    public List<Actor> findAllActorByFilmId(int id){
        return getBaseMapper().findAllActorByFilmId(id);
    }


}
