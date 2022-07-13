package com.zking.dto;

import com.zking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private String userImg;
    private String name;
    private String sex;
    private int vip;
    private Date birthday;
    private String address;
    private String email;
    private Date time;
    private Date lastLoginTime;

    public static UserDto userDto(User user, String img) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setUserImg(img);
        userDto.setName(user.getName());
        userDto.setSex(user.getSex());
        userDto.setVip(user.getVip());
        userDto.setBirthday(user.getBirthday());
        userDto.setAddress(user.getAddress());
        userDto.setEmail(user.getEmail());
        userDto.setTime(user.getTime());
        userDto.setLastLoginTime(user.getLastLoginTime());
        return userDto;
    }

    public static List<UserDto> userDtos(List<User> users, List<String> imgs) {
        List<UserDto> list = new ArrayList<>();
        for (User user : users){
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setName(user.getName());
            userDto.setSex(user.getSex());
            userDto.setVip(user.getVip());
            userDto.setBirthday(user.getBirthday());
            userDto.setAddress(user.getAddress());
            userDto.setEmail(user.getEmail());
            userDto.setTime(user.getTime());
            userDto.setLastLoginTime(user.getLastLoginTime());
            list.add(userDto);
        }
        int count = 0;
        for (String s : imgs){
            list.get(count).setUserImg(s);
            count++;
        }
        return list;
    }
}
