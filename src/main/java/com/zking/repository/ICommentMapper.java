package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Actor;
import com.zking.entity.Authority;
import com.zking.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICommentMapper extends BaseMapper<Comment> {

}
