package com.zking.dto;

import com.zking.entity.Address;
import lombok.*;



@Data @AllArgsConstructor @NoArgsConstructor
public class AddressDto
{
    /*
    前端仅仅需要这几个字段即可！
    前端只需要：label展示，value值
    前后端映射：
        id => value
        name => value或者label
        extName => label
     */
    private Integer id;
    private String name;
    private String extName;
    
    public static AddressDto from(Address address)
    {
        if (address == null)
        {
            return null;
        }
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setName(address.getName());
        addressDto.setExtName(address.getExtName());
        return addressDto;
    }
}
