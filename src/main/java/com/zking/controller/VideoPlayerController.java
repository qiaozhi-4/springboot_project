package com.zking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zking.entity.Comment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@PermitAll
// TODO 后台测试
@Controller
@RequestMapping("/video")
public class VideoPlayerController {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(100);

    /**
     * 访问后端视频页面：没有登录则表示用户无法发弹幕
     * 视频ID用于保存数据到对应的视频数据库条目中
     *
     * @param id     视频id
     * @param userId 用户id
     * @param model  数据
     * @return 返回视频播放页面视图
     */
    @GetMapping("{id}")
    public String axios(@PathVariable Integer id, HttpSession session, Model model) {
        id = id == null ? 0 : id;
        Object sid = session.getAttribute("userId");
        int userId = sid == null ? 0 : (int) sid;
        model.addAttribute("movieId", id);
        model.addAttribute("userId", userId);
        model.addAttribute("login", sid != null);
        return "play";
    }

    // TODO 登录模拟，自己定义API
    @PostMapping("login")
    @ResponseBody
    public Map<String, Object> login(Model model, HttpSession session) {
        // 模拟登录
        int id = ATOMIC_INTEGER.getAndIncrement();
        model.addAttribute("userId", id);
        session.setAttribute("userId", id);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("userId", id);
        return result;
    }

    // TODO 发送消息，自己定义API
    @PostMapping("{id}/message")
    @ResponseBody
    public Boolean send(@RequestBody Comment message, @PathVariable Integer id, HttpSession session) throws JsonProcessingException {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || userId == 0 /*|| !Objects.equals(message.getUserId(), userId)*/) {
            return false;
        }
        if (!Objects.equals(message.getFilmId(), id)) {
            return false;
        }
        // 异步发送
        WebSocketController.sendMessageToAll(message);
        // 保存到数据库
        return true;
    }

    // TODO 获取所有数据库消息，自己定义API
    @GetMapping("{id}/barrages")
    @ResponseBody
    public List<Comment> messages(@PathVariable Integer id) {
        // 从数据库中获取数据【当前视频】
        List<Comment> messages = new ArrayList<>();
        int movieId = id;
        //messages.add(new Comment(1, 1, movieId, "第1条", new Date(), 1.1, "#fff123"));
        //messages.add(new Comment(2, 1, movieId, "第2条", new Date(), 2.2, "#fff123"));
        //messages.add(new Comment(3, 1, movieId, "第3条", new Date(), 3.3, "#fff123"));
        //messages.add(new Comment(4, 1, movieId, "第4条", new Date(), 4.4, "#fff123"));

        return messages;
    }

    private Date random() {
        long now = new Date().getTime();
        long minus = (long) (3600 * 1000 * Math.random() * 10);
        return new Date(now - minus);
    }
}
