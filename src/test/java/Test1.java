import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.App;
import com.zking.entity.User;
import com.zking.service.impl.FilmService;
import com.zking.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class Test1 {
    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private PasswordEncoder encoder;

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
}
