import com.zking.App;
import com.zking.service.IFilmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class TestFilmServiceS {

    @Autowired
    private IFilmService filmService;

    //测试主页拿数据
    @Test
    public void test1(){
        System.out.println(filmService.getTypeAndFilm());
    }

    @Test
    public void test2(){
        System.out.println();
    }

    @Test
    public void test3(){
        Integer[] integers = {1,7,6,12,17};
        filmService.updateFilmType(1,integers);

        filmService.updateFilmActor(1,integers);
    }
}
