import com.zking.App;
import com.zking.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class TestUserServiceS {

    @Autowired
    private IUserService userService;

    @Test
    public void test1(){
        System.out.println(userService.findAllRoleByUserId(1));
        System.out.println(userService.findAllAuthoritySByUserId(1));
    }

    //测试主页拿数据
    @Test
    public void test2(){
        System.out.println();
    }
}
