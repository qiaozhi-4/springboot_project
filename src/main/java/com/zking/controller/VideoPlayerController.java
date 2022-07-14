package com.zking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zking.dto.CommentDTO;
import com.zking.service.impl.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// TODO 后台测试，前后端分离测试，需要开启跨域
@Controller @Slf4j @RequiredArgsConstructor
@RequestMapping("/video")
@CrossOrigin(origins = {"http://localhost:63343"}, allowCredentials = "true", methods = {RequestMethod.POST, RequestMethod.GET})
public class VideoPlayerController
{
    private final WebSocketService socketService;
    // 测试用，实际从数据库中获取用户ID，这里100开始测试
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(100);

    /**
     * 访问后端视频页面：没有登录则表示用户无法发弹幕
     * 视频ID用于保存数据到对应的视频数据库条目中
     *
     * @param id 视频id
     //* @param userId 用户id
     * @param model 数据
     * @return 返回视频播放页面视图
     */
    @GetMapping("{id}")
    public String index(@PathVariable Integer id, HttpSession session, Model model)
    {
        id = id == null ? 0 : id;
        Object sid = session.getAttribute("userId");
        int userId = sid == null ? 0 : (int) sid;
        model.addAttribute("movieId", id);
        model.addAttribute("userId", userId);
        model.addAttribute("login", sid != null);
        return "test";
    }

    // TODO 登录模拟，自己定义API，一般是前后端分离，
    //  如果是登录后转发到视频页面，需要记录用户当前视频的播放时间
    @PostMapping("login")
    @ResponseBody
    public Map<String, Object> login(Model model, HttpSession session)
    {
        // 模拟登录
        int id = ATOMIC_INTEGER.getAndIncrement();
        model.addAttribute("userId", id);
        session.setAttribute("userId", id);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("userId", id);
        return result;
    }

    // TODO 发送消息，自己定义API，
    //  另外，你也可以判断是否为VIP付费会员，不是的话就不能有文字颜色，非VIP将颜色设置为默认色
    @PostMapping("{id}/message")
    @ResponseBody
    public Map<String, Object> send(@RequestBody CommentDTO message, @PathVariable Integer id, HttpSession session) throws JsonProcessingException
    {
        Integer userId = (Integer) session.getAttribute("userId");
        Map<String, Object> result = new HashMap<>();
        if (userId == null || userId == 0)
        {
            result.put("success", false);
            log.warn("【恶意API调用】收到来自{}的信息：{}", userId, message);
            return result;
        }
        if (! Objects.equals(message.getMovieId(), id))
        {
            result.put("success", false);
            log.warn("【恶意API调用】收到来自{}（视频：{}）的信息：{}", userId, id, message);
            return result;
        }
        // 发送前也需要做一些校验：
        // 比如:时间是未来的事件,超过现在了,恶意调用API导致,电影ID不存在也可能...
        socketService.sendMessage(message);
        // 保存到数据库
        // ..................SQL
        result.put("success", true);
        result.put("op", "消息发送成功");
        result.put("msg", 1);
        return result;
    }

    // TODO 获取所有数据库消息，自己定义API
    @GetMapping("{id}/barrages")
    @ResponseBody
    public List<CommentDTO> messages(@PathVariable Integer id)
    {
        // 从数据库中获取数据【当前视频】
        int movieId = id;
        return mockMessages(movieId);
    }

    // ===============================测试代码
    private final Random random = new Random();
    private List<CommentDTO> mockMessages(int movieId)
    {
        List<CommentDTO> messages = new ArrayList<>();
        //messages.add(BarrageMessage.to(new BarrageMessage(1, "第1条", random(), movieId, 1.1, "#fff123", "white", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(3, "第2条", random(), movieId, 2.2, "#fff123", "yellow", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(6, "第3条", random(), movieId, 1.1, "#faa123", "black", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(3, "第4条", random(), movieId, 3.1, "#11f123", "black", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(2, "第5条", random(), movieId, 2.1, "#ffd233", "white", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(4, "第7条", random(), movieId, 4.1, "#ffff23", "white", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(7, "第741321条", random(), movieId, 5.51, "#123123", "black", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(10, "第8111条", random(), movieId, 1.23, "#ee1123", "white", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(100, "第111条", random(), movieId, 4.4, "#121ff", "white", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(101, "第1323条", random(), movieId, 6.15, "#ffff23", "yellow", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(101, "第12321q1条", random(), movieId, 2.16, "#fff123", "white", type(), false)));
        //messages.add(BarrageMessage.to(new BarrageMessage(4, "第1aaaaaaaaaaa条", random(), movieId, 3.11, "#f12123", "yellow", type(), false)));
        return messages;
    }
    private String type()
    {
        int r = random.nextInt(100);
        return r < 50 ? "top" : (r < 75 ? "bottom" : "scroll");
    }
    private Timestamp random()
    {
        long now = new Date().getTime();
        long minus = 3600L * 1000 * random.nextInt(100) * 10;
        return new Timestamp(now - minus);
    }
}
