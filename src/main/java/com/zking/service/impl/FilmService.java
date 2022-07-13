package com.zking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.entity.*;

import com.zking.repository.IFilmMapper;

import com.zking.repository.IUserMapper;
import com.zking.service.IFilmService;
import com.zking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
//@RequiredArgsConstructor

@Service
@RequiredArgsConstructor
public class FilmService extends ServiceImpl<IFilmMapper, Film> implements IFilmService {
    private final IFilmMapper filmMapper;

    public List<String> findAllTypeByFilmId(int id){
        return filmMapper.findAllTypeByFilmId(id);
    }

    public String findAllRegionByFilmId(int id){
        return filmMapper.findAllRegionByFilmId(id);
    }

    public List<Actor> findAllActorByFilmId(int id){
        return filmMapper.findAllActorByFilmId(id);
    }



}
