package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Address;
import com.zking.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IAddressMapper extends BaseMapper<Address> {
    List<Address> findByDeep(int deep);
    List<Address> findByParentId(int pid);


}
