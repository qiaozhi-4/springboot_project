package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.File;
import com.zking.entity.Img;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IFileMapper extends BaseMapper<File> {

}
