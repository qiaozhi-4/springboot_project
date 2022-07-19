package com.zking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.dto.AddressDto;
import com.zking.entity.Address;
import com.zking.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final IAddressService addressService;
    /**
     * 使用了Restful格式，可以自己改为/deep?deep=1参数模式
     * 路径为：/api/address/deep/1
     * 注：<b>方法没有被用到</b>
     *
     * @param deep 深度，0表示为全国的省市一级行政区
     * @return 返回所有同一深度的地区
     */
    @GetMapping("/deep/{deep}")
    public List<AddressDto> byDeep(@PathVariable int deep)
    {
        return addressService.getAddressByDeep(deep);
    }

    @GetMapping("/parent/{pid}")
    public List<AddressDto> byParentId(@PathVariable("pid") int parentId)
    {
        return addressService.getAddressByParentId(parentId);
    }

}
