import com.zking.App;
import com.zking.entity.Film;
import com.zking.entity.User;
import com.zking.service.IActorService;
import com.zking.service.ICommentService;
import com.zking.service.IFilmService;
import com.zking.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class TestS1 {
    @Autowired
    private IActorService actorService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IFilmService filmService;
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder encoder;

    @Test
    public void test1() {
        System.out.println(actorService.getById(1));
    }

    @Test
    public void test2() {
        System.out.println(commentService.getById(1));
    }

    @Test
    public void test3() {
        System.out.println(filmService.getById(1));
    }

    @Test
    public void test4() {
        System.out.println(userService.getById(1));
    }

    @Test
    public void test5() {
        String path = "D:\\springboot\\videolook\\videolookimg";        //要遍历的路径
        File file = new File(path);        //获取其file对象
        File[] fs = file.listFiles();    //遍历path下的文件和目录，放在File数组中
        int id = 1;
        for (File f : fs) {                    //遍历File[]数组
            if (!f.isDirectory()) {    //若非目录(即文件)，则打印
                if (id > 26) {
                    return;
                }
                System.out.println("/videolook/videolookimg/" + f.getName());
                Film film = new Film();
                film.setId(id++);
                film.setImgSrc("/videolook/videolookimg/" + f.getName());
                filmService.updateById(film);
            }

        }
    }

    @Test
    public void test6() {
        for (int i = 1; i <= 50; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setPassword(encoder.encode("123"));
            user.setHeadImg("/img/head.jpg");
            user.setName("用户" + i);
            user.setSex(Math.random() > 0.5 ? '男' : '女');
            user.setVip(0);
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            user.setBirthday( dateFormat.format(date));
            user.setAddress("您还未填写住址");
            user.setEmail("未填写");
            user.setTime(dateFormat.format(date));
            user.setLastLoginTime(dateFormat.format(date));
            userService.save(user);
        }
    }



    @Test
    public void test7() throws FileNotFoundException {

    }
}
