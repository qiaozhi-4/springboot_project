package com.zking.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.entity.Film;
import com.zking.entity.Result;
import com.zking.entity.User;
import com.zking.service.IFilmService;
import com.zking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@PermitAll
// @RestController = @ResponseBody + @Controller
@Controller
//自动生成无参带参构造
@RequiredArgsConstructor
//标注共享的属性名
@SessionAttributes({"validate"})
public class RegisterController {

    @Value("${upload.locationImg}")
    private String location;

    private final IUserService userService;
    private final PasswordEncoder encoder;
    private final IFilmService filmService;


    //注册页面
    @GetMapping("register")
    public String register() {
        return "register";
    }

    @PostMapping("registerForm")
    @ResponseBody
    public Result<String> registerForm(@ModelAttribute("validate") boolean validate,MultipartFile file, String username, String password, String name, Character sex, Model model) throws IOException, ParseException {
//        model.addAttribute("info", "");
        if (!validate) {
            return Result.fail("登录失败你还没有通过验证信息");
        }
        if (file.getOriginalFilename().equals("")) {
            model.addAttribute("info", "头像不为空");
            return Result.fail("登录失败你还没有通过验证信息");
        }
        if (username == null && password == null && name == null) {
            model.addAttribute("info", "请填写必要信息");
            return Result.fail("登录失败你还没有通过验证信息");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pass = encoder.encode(password);
        String parse = dateFormat.format(new Date());
        String imgname = UUID.randomUUID() + file.getOriginalFilename();
        String path = "/" + imgname;
        File dest = new File(location, path);
        file.transferTo(dest);
        System.out.println("path => " + path);


        User user = new User(0, username, pass, "/img" + path, name, sex, 0, null, null, null, parse, null,null, null);
        boolean save = userService.save(user);
        if (!save) {
            model.addAttribute("info", "注册失败请重试");
            return Result.fail("登录失败你还没有通过验证信息");
        }
        return Result.fail("登录失败你还没有通过验证信息");
    }

    //极简验证
    @RequestMapping("/validate")
    @ResponseBody
    public Result<Void> validate(@RequestParam("lot_number") String lotNumber,
                                 @RequestParam("captcha_output") String captchaOutput,
                                 @RequestParam("pass_token") String passToken,
                                 @RequestParam("gen_time") String genTime, HttpSession session) {
        // 3.使用用户当前完成验证的流水号生成签名
        String signToken = new HmacUtils(HmacAlgorithms.HMAC_SHA_256,
                "f5e7386d87aa510914a7d42fd113007e").hmacHex(lotNumber);
        // 4.上传校验参数到极验二次验证接口, 校验用户验证状态
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("lot_number", lotNumber);
        queryParams.add("captcha_output", captchaOutput);
        queryParams.add("pass_token", passToken);
        queryParams.add("gen_time", genTime);
        queryParams.add("sign_token", signToken);
        // captcha_id参数建议放在url后面, 方便请求异常时可以在日志中根据id快速定位到异常请求
        String url = String.format("%s/validate?captcha_id=%s",
                "https://gcaptcha4.geetest.com", "bb962a9d4859644b981346830e3f2172");
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String reason = "未知错误";
        session.setAttribute("validate", false);
        // 发起请求，注意处理接口异常情况，当请求极验二次验证接口异常时做出相应异常处理
        try {
            HttpEntity<MultiValueMap<String, String>> requestEntity =
                    new HttpEntity<MultiValueMap<String, String>>(queryParams, headers);
            ResponseEntity<String> response =
                    client.exchange(url, HttpMethod.POST, requestEntity, String.class);
            JSONObject jsonObject = new JSONObject(response.getBody());
            // 5.根据极验返回的用户验证状态, 业务逻辑自行处理
            if ("success".equals(jsonObject.getString("result"))) {
                // 已经验证通过，添加validate信息
                session.setAttribute("validate", true);
                System.out.println(session.getAttribute("validate"));
                return Result.success(null);
            }
            if (jsonObject.getString("reason") != null) {
                reason =
                        jsonObject.getString("reason");
            }
        } catch (Exception e) {
            reason = "Api请求失败，请检查服务器网络或者用户参数配置 ";
        }
        return Result.fail(reason);
    }

    //查看用户名是否存在
    @ResponseBody
    @GetMapping("/isusers")
    public boolean isusers(String name) {
        User user = userService.getOne(new QueryWrapper<User>().eq("userName", name), false);
//        System.out.println(user.toString());
        //user为空，说明没有找到该用户，可以注册。反之。
        return user == null;
    }


    //查询电影前五热度
    @ResponseBody
    @GetMapping("/selectHeat")
    public List<Film> selectHeat() {
        return filmService.selectHeat();
    }

}
