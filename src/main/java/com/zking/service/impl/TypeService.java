package com.zking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.entity.Type;
import com.zking.repository.ITypeMapper;
import com.zking.service.ITypeService;
import org.springframework.stereotype.Service;
//@RequiredArgsConstructor

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
@Service
public class TypeService extends ServiceImpl<ITypeMapper, Type> implements ITypeService {

}
