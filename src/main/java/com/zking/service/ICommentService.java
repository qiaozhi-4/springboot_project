package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICommentService extends IService<Comment>{
    //通过电影查询所有评论
    List<Comment> findAllCommentByFilmId(@Param("fid") int id);
}
