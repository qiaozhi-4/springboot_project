package com.zking.controller;

import com.zking.entity.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

// @RestController = @ResponseBody + @Controller
@RestController
//自动生成无参带参构造
@RequiredArgsConstructor
//标注共享的属性名
@SessionAttributes({"validate"})
public class jyController {
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
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
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
                    new HttpEntity<>(queryParams, headers);
            ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
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


}

