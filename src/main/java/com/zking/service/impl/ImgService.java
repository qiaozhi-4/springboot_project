package com.zking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.entity.Img;
import com.zking.entity.User;
import com.zking.repository.IImgMapper;
import com.zking.repository.IUserMapper;
import com.zking.service.IImgService;
import com.zking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
@RequiredArgsConstructor
@Service
public class ImgService extends ServiceImpl<IImgMapper, Img> implements IImgService {



}
