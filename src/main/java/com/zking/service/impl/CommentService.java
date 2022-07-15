package com.zking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.entity.Comment;
import com.zking.repository.ICommentMapper;
import com.zking.service.ICommentService;
import org.springframework.stereotype.Service;

import java.util.List;

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
//@RequiredArgsConstructor

@Service
public class CommentService extends ServiceImpl<ICommentMapper, Comment> implements ICommentService {

    //通过电影查询所有评论
    @Override
    public List<Comment> findAllCommentByFilmId(int id) {
        return getBaseMapper().findAllCommentByFilmId(id);
    }
}
