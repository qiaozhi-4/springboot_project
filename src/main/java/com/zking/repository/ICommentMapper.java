package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Actor;
import com.zking.entity.Authority;
import com.zking.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ICommentMapper extends BaseMapper<Comment> {
    //通过电影查询所有评论
    List<Comment> findAllCommentByFilmId(@Param("fid") int id);
}
