package com.zking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.dto.FilmDTO;
import com.zking.dto.UserCount;
import com.zking.entity.Actor;
import com.zking.entity.Film;
import com.zking.entity.Type;
import com.zking.entity.User;
import com.zking.service.IActorService;
import com.zking.service.IFilmService;
import com.zking.service.ITypeService;
import com.zking.service.IUserService;
import com.zking.util.Ffmpeg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@DenyAll
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final IUserService userService;
    private final IFilmService filmService;
    private final IActorService actorService;
    private final ITypeService typeService;

    @RolesAllowed("admin")
    @GetMapping("adminIndex")
    public String adminIndex() {
        return "/admin/adminIndex";
    }

    @RolesAllowed("admin")
    @GetMapping("adminFilm")
    public String adminFilm() {
        return "/admin/adminFilm";
    }

    @RolesAllowed("admin")
    @GetMapping("adminUser")
    public String adminUser() {
        return "/admin/adminUser";
    }

    @RolesAllowed("admin")
    @GetMapping("adminUserIndex/{id}")
    public String adminUserIndex(Model model, @PathVariable String id) {
        User user = userService.getById(id);
        model.addAttribute("adminGetUser",user);
        return "/admin/adminUserIndex";
    }


    //获取所有用户
    @RolesAllowed("admin") // 必须admin角色才能访问
    @GetMapping("/findAllUser")
    @ResponseBody
    public List<User> findAllUser() {
        return userService.list();
    }


    //获取所有类型
    @RolesAllowed("admin") // 必须admin角色才能访问
    @GetMapping("/findAllType")
    @ResponseBody
    public List<Type> findAllType() {
        return typeService.list();
    }


    //admin需要获取电影以及他的所有类型
    @RolesAllowed("admin") // 必须admin角色才能访问
    @GetMapping("/findAllFilm")
    @ResponseBody
    public List<FilmDTO> findAllFilm() {
        return filmService.getAllFilms();
    }


    //获取所有演员
    @RolesAllowed("admin") // 必须admin角色才能访问
    @GetMapping("/findAllActor")
    @ResponseBody
    public List<Actor> findAllActor() {
        return actorService.list();
    }


    //更改用户信息
    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/updateUser")
    @ResponseBody
    public boolean updateUser(User user, MultipartFile file) throws IOException {
        if (file != null) {
            //把文件传入本地
            String path = "/img/" + UUID.randomUUID() + file.getOriginalFilename();
            File dest = new File("D:\\springboot", path);
            file.transferTo(dest);
            user.setHeadImg(path);
        }
        return userService.updateById(user);
    }

    //用户封禁
    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/bannedUser")
    @ResponseBody
    public boolean bannedUser(Integer id, Integer vip) {
        User user = new User();
        user.setId(id);
        user.setVip(vip);
        return userService.updateById(user);
    }

    // 模糊查询用户
    @RolesAllowed("admin") // 必须admin角色才能访问
    @ResponseBody
    @GetMapping ("fuzzyQueryUser")
    public List<User> fuzzyQueryUser(String selectName) {
        return userService.list(new QueryWrapper<User>().like("name", selectName));
    }

    // 模糊查询视频
    @RolesAllowed("admin") // 必须admin角色才能访问
    @ResponseBody
    @GetMapping ("fuzzyQueryFilm")
    public List<FilmDTO> fuzzyQueryFilm(String selectName, String selectType) {
        if (selectType.equals("type")) {
            List<Type> types = typeService.list(new QueryWrapper<Type>().like("name", selectName));
            if (types.size() > 0) {
                return typeService.findAllFilmByTypeId(types);
            }
            return null;
        }
        return filmService.selectFilm(selectName, selectType);
    }

    //电影修改
    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/updateFilm")
    @ResponseBody
    public String updateFilm(Film film, Integer[] types) {
        if (types.length >0 && filmService.updateFilmType(film.getId(), types)) {
            filmService.updateById(film);
            StringBuilder type = new StringBuilder();
            int count = 1;
            for (int i: types) {
                if (count++ < types.length) {
                    type.append(typeService.getById(i).getName()).append(",");
                }else {
                    type.append(typeService.getById(i).getName());
                }
            }
            return type.toString();
        }
        return null;
    }

    //电影下架
    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/bannedFilm")
    @ResponseBody
    public boolean bannedFilm(Film film) {
        film.setVip(-1);
        return filmService.updateById(film);
    }

    //电影添加
    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/addFilm")
    @ResponseBody
    public FilmDTO addFilm(Film film, MultipartFile img, MultipartFile file, Integer[] actors, Integer[] types) throws IOException {
        if (file != null && img != null) {
            //把封面传入本地
            String imgPath = "/videolook/videolookimg/" + UUID.randomUUID() + img.getOriginalFilename();
            File imgFile = new File("D:\\springboot", imgPath);
            img.transferTo(imgFile);
            film.setImgSrc(imgPath);//存入数据库的路径

            //把电影传入本地
            String filmName = UUID.randomUUID() + file.getOriginalFilename();
            String filePath = "/videolook/" + filmName;
            File filmFile = new File("D:\\springboot", filePath);
            file.transferTo(filmFile);
            film.setMp4Src(filePath);//存入数据库的路径

            //剪切视频工具
            String cope = Ffmpeg.cope(filmName);

            //插入数据库
            film.setCoverSrc(cope);
        }
        film.setTime(new Date());
        filmService.addFilms(film, actors, types);
        return filmService.findAllTypeByFilmId(film);
    }

    //电影更改类型
    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/updateFilmType")
    @ResponseBody
    public boolean updateFilmType(Integer filmId, Integer[] types) throws IOException {
        filmService.updateFilmType(filmId,types);
        return false;
    }


    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/userCount")
    @ResponseBody
    public List<UserCount> userCount() {
        int vipCount = userService.list(new QueryWrapper<User>().eq("vip", 1)).size();
        UserCount vip = new UserCount(vipCount, "VIP用户");
        int commonCount = userService.list(new QueryWrapper<User>().eq("vip", 0)).size();
        UserCount common = new UserCount(commonCount, "普通用户");
        int bannedCount = userService.list(new QueryWrapper<User>().eq("vip", -1)).size();
        UserCount banned = new UserCount(bannedCount, "封禁用户");
        List<UserCount> userCounts = new ArrayList<>();
        userCounts.add(vip);
        userCounts.add(common);
        userCounts.add(banned);
        return userCounts;
    }

    @RolesAllowed("admin") // 必须admin角色才能访问
    @PostMapping("/recentLoginUser")
    @ResponseBody
    public List<User> recentLoginUser() {
        // 获取当前时间减去一个月
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1); //当前时间减去一个月
        //当前时间减去一个月后的时间
        String format = simpleDateFormat.format(calendar.getTime());
        List<User> users = userService.list(new QueryWrapper<User>().gt("last_login_time", format));
        return users;
    }
}
