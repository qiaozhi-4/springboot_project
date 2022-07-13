import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.App;
import com.zking.dto.FilmDto;
import com.zking.dto.UserCount;
import com.zking.dto.UserDto;
import com.zking.entity.File;
import com.zking.entity.Film;
import com.zking.entity.Img;
import com.zking.entity.User;
import com.zking.service.IActorService;
import com.zking.service.ICommentService;
import com.zking.service.IFilmService;
import com.zking.service.impl.FileService;
import com.zking.service.impl.FilmService;
import com.zking.service.impl.ImgService;
import com.zking.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class Test1 {
    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ImgService imgService;
    @Autowired
    private FileService fileService;

    @Test
    public void test1(){
        com.zking.entity.User user =
                userService.getOne(new QueryWrapper<User>().eq("username", "admin"), false);
        System.out.println(user);
    }

    @Test
    public void test2(){
        System.out.println(encoder.encode("bbb"));
        System.out.println(encoder.encode("aaa"));
        System.out.println(encoder.encode("sss"));
    }

    @Test
    public void test3(){
//        System.out.println(filmService.findAllActorByFilmId(1));
        int vipCount = userService.list(new QueryWrapper<User>().eq("vip", 1)).size();
        int commonCount = userService.list(new QueryWrapper<User>().eq("vip", 0)).size();
        int bannedCount = userService.list(new QueryWrapper<User>().eq("vip", -1)).size();
        System.out.println(vipCount+"==="+commonCount+"==="+bannedCount);
    }

    @Test
    public void test4(){
        // 获取当前时间减去一个月
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        calendar.add(Calendar.DAY_OF_MONTH, -1); //当前时间减去一天
        calendar.add(Calendar.MONTH, -1); //当前时间减去一个月
//        calendar.add(Calendar.YEAR, -1);//当前时间减去一年
        String format = simpleDateFormat.format(calendar.getTime());
        System.out.println(format);
    }

    @Test
    public void test5(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1); //当前时间减去一个月
        //当前时间减去一个月后的时间
        String format = simpleDateFormat.format(calendar.getTime());
        List<User> users = userService.list(new QueryWrapper<User>().gt("last_login_time", format));
        ArrayList<String> imgs = new ArrayList<>();
        for (User u: users) {
            Img img = imgService.getOne(new QueryWrapper<Img>().eq("id", u.getImgId()));
            imgs.add(img.getUrl());
        }
        UserDto.userDtos(users, imgs).forEach(System.out::println);
    }

    @Test
    public void test6(){
        List<Film> films = filmService.list();
        ArrayList<String> files = new ArrayList<>();
        ArrayList<String> imgs = new ArrayList<>();
        ArrayList<String> filmRegions = new ArrayList<>();
        ArrayList<List<String>> filmTypes = new ArrayList<>();
        for (Film film: films) {
            File file = fileService.getOne(new QueryWrapper<File>().eq("id", film.getFileId()));
            files.add(file.getUrl());
            Img img = imgService.getOne(new QueryWrapper<Img>().eq("id", film.getImgId()));
            imgs.add(img.getUrl());
            String region = filmService.findAllRegionByFilmId(film.getId());
            filmRegions.add(region);
            List<String> filmType = filmService.findAllTypeByFilmId(film.getId());
            filmTypes.add(filmType);
        }
        FilmDto.filmDtos(films, files, imgs, filmRegions, filmTypes).forEach(System.out::println);
    }
}
