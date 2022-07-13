package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Img;
import com.zking.entity.Type;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ITypeMapper extends BaseMapper<Type> {

}
