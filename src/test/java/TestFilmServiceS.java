import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.App;
import com.zking.entity.Film;
import com.zking.entity.Type;
import com.zking.service.IFilmService;
import com.zking.service.ITypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class TestFilmServiceS {

    @Autowired
    private IFilmService filmService;
    @Autowired
    private ITypeService typeService;

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

    @Test
    public void test4(){
//        filmService.selectFilm("1", "name").forEach(System.out::println);
//        filmService.list(new QueryWrapper<Film>().like("name", "1").or().like("region", "中")).forEach(System.out::println);
//        Type type = typeService.getOne(new QueryWrapper<Type>().eq("name", "动漫"));
//        System.out.println(type);
        List<Type> types = typeService.list(new QueryWrapper<Type>().like("name", "动漫"));
        typeService.findAllFilmByTypeId(types).forEach(System.out::println);
    }
}
