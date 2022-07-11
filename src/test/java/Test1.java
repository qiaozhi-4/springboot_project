import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.App;
import com.zking.entity.User;
import com.zking.service.IActorService;
import com.zking.service.ICommentService;
import com.zking.service.IFilmService;
import com.zking.service.impl.FilmService;
import com.zking.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        System.out.println(encoder.encode("admin"));
    }

    @Test
    public void test3(){
        System.out.println(filmService.findAllActorByFilmId(1));
    }
}
