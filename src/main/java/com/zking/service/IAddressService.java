package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.dto.AddressDto;
import com.zking.entity.Actor;
import com.zking.entity.Address;

import java.util.List;

public interface IAddressService extends IService<Address>{

    List<AddressDto> getAddressByDeep(int deep);
    List<AddressDto> getAddressByParentId(int parentId);


}
