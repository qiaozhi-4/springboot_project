package com.zking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zking.dto.CommentDTO;
import com.zking.entity.Comment;
import com.zking.entity.Film;
import com.zking.entity.User;
import com.zking.service.ICommentService;
import com.zking.service.IFilmService;
import com.zking.service.impl.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// TODO 后台测试，前后端分离测试，需要开启跨域
@Controller @Slf4j @RequiredArgsConstructor
@RequestMapping("/video")
@CrossOrigin(origins = {"http://localhost:8081"}, allowCredentials = "true", methods = {RequestMethod.POST, RequestMethod.GET})
public class VideoPlayerController
{
    private final WebSocketService socketService;
    // 测试用，实际从数据库中获取用户ID，这里100开始测试
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(100);
    private final RestTemplate restTemplate;
    private final ICommentService commentService;
    private final IFilmService filmService;

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

        //获取用户点击的电影
        Film film = filmService.getById(id);
        model.addAttribute("film", film);

        //获取用户认证信息
        SecurityContext context = SecurityContextHolder.getContext(); // 上下文
        Authentication authentication = context.getAuthentication(); // 认证信息
        if (!"anonymousUser".equals(authentication.getPrincipal())){

            User user = (User) authentication.getPrincipal(); // 唯一用户对象，一般是UserDetails
            Object sid = user.getId();
            int userId = sid == null ? 0 : (int) sid;
            model.addAttribute("movieId", id);
            model.addAttribute("userId", userId);
            model.addAttribute("login", true);
        }else {
            model.addAttribute("movieId", id);
            model.addAttribute("userId", 0);
            model.addAttribute("login", false);
        }

        return "nplayer_vue";
    }

    // TODO 登录模拟，自己定义API，一般是前后端分离，
    //  如果是登录后转发到视频页面，需要记录用户当前视频的播放时间
    @PostMapping("login")
    @ResponseBody
    public Map<String, Object> login(String username,String password,Model model, String _csrf , HttpSession session)
    {
        //后端发送登录请求
        // 请求头设置,x-www-form-urlencoded格式的数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //提交参数设置
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("username", username);
        paramMap.add("password", password);
        paramMap.add("_csrf", _csrf);

        // 组装请求体
        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(paramMap, headers);
        // 1、使用postForObject请求接口

    String r = restTemplate.postForObject("http://localhost:8081/login", request, String.class);

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
        Integer userId = 0;
        //获取用户认证信息
        SecurityContext context = SecurityContextHolder.getContext(); // 上下文
        Authentication authentication = context.getAuthentication(); // 认证信息
        if (!"anonymousUser".equals(authentication.getPrincipal())){

            User user = (User) authentication.getPrincipal(); // 唯一用户对象，一般是UserDetails
            userId = user.getId();
        }

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
        commentService.save(Comment.from(message));
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
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        List<Comment> comments = commentService.findAllCommentByFilmId(id);
        // 获取当前视频所有评论
        for (Comment comment : comments) {
            commentDTOS.add(Comment.to(comment));
        }
        return commentDTOS;
    }

}
