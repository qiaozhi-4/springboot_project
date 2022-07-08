import com.zking.App;
import com.zking.service.IActorService;
import com.zking.service.ICommentService;
import com.zking.service.IFilmService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class Test1 {
    @Autowired
    private  IActorService actorService;
    @Autowired
    private  ICommentService commentService;
    @Autowired
    private  IFilmService filmService;

    @Test
    public void test1(){
        System.out.println(filmService.getOne(null,false));
        System.out.println(commentService.getOne(null,false));
        System.out.println(actorService.getOne(null,false));
    }
}
