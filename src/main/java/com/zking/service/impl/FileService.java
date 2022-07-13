package com.zking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.entity.File;
import com.zking.entity.Img;
import com.zking.repository.IFileMapper;
import com.zking.repository.IImgMapper;
import com.zking.service.IFileService;
import com.zking.service.IImgService;
import org.springframework.stereotype.Service;
//@RequiredArgsConstructor

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
@Service
public class FileService extends ServiceImpl<IFileMapper, File> implements IFileService {

}
