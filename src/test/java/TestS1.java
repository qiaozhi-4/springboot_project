import com.zking.App;
import com.zking.entity.Comment;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        String path = "D:\\springboot\\videolook\\videolookshort";        //要遍历的路径
        File file = new File(path);        //获取其file对象
        File[] fs = file.listFiles();    //遍历path下的文件和目录，放在File数组中
        int id = 1;
        for (File f : fs) {                    //遍历File[]数组
            if (!f.isDirectory()) {    //若非目录(即文件)，则打印
                if (id > 26) {
                    return;
                }
                System.out.println("/videolook/videolookshort/" + f.getName());
            }

        }
    }

    @Test
    public void test8(){
        Comment comment = new Comment();
        comment.setUserId(1);
        comment.setFilmId(5);
        comment.setConten("555");
        comment.setTime(new Date());
        comment.setFilmTime(1.2);
        comment.setColor("#fff");
        comment.setBgColor("#fff");
        comment.setType("top");
        comment.setForced(true);
        System.out.println(commentService.save(comment));
    }



    @Test
    public void test9(){
        String month = getAfterMonth(new Date(), 6);
        System.out.println(month);
        System.out.println(getAfterMonth(month, 6));
    }

    public static String  getAfterMonth(Date inputDate,int number) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = inputDate;
        c.setTime(date);//设置日历时间
        c.add(Calendar.MONTH,number);//在日历的月份上增加6个月
        String strDate = sdf.format(c.getTime());//的到你想要得6个月后的日期
        return strDate;
    }
    public static String  getAfterMonth(String inputDate,int number) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(inputDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        c.setTime(date);//设置日历时间
        c.add(Calendar.MONTH,number);//在日历的月份上增加6个月
        String strDate = sdf.format(c.getTime());//的到你想要得6个月后的日期
        return strDate;
    }
}
