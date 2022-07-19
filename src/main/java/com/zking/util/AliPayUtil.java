package com.zking.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

// 支付宝沙箱支付工具类
@Component
@ConfigurationProperties(value = "alipay")
@Getter
@Setter
public class AliPayUtil {

    private String appid;
    private String url;
    private String private_key;
    private String public_key;
    private String notify_url;
    private String return_url;

    public  String pay(String id,String price,String subject)  {

        AlipayClient alipayClient = new DefaultAlipayClient(url, appid, private_key, "json", "UTF-8", public_key, "RSA2");
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notify_url);
        request.setReturnUrl(return_url);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);
        bizContent.put("total_amount", price);
        bizContent.put("subject", subject);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

        request.setBizContent(bizContent.toString());
        System.out.println(bizContent.toString());
        AlipayTradePagePayResponse response = null;
        String form = null;
        try {
            response = alipayClient.pageExecute(request);
            // 响应为表单格式，可嵌入页面，具体以返回的结果为准
            form = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        return form;
    }

    // out_trade_no 根据订单号判断是否支付成功
    public String query(String id) {
        AlipayClient alipayClient = new DefaultAlipayClient(url, appid , private_key , "json", "UTF-8", public_key , "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);

        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        String body = null;
        try {
            response = alipayClient.execute(request);
            // 响应中获取参数
            body = response.getBody();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return body;
    }

    // 退款 trade_no 和 out_trade_no 有其中一种即可
    public String fallback(String id,Double money ,String oid){
        AlipayClient alipayClient = new DefaultAlipayClient(url, appid , private_key , "json", "UTF-8", public_key , "RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        // bizContent.put("trade_no", id); // 支付宝交易号
        bizContent.put("out_trade_no", oid); // 订单号
        bizContent.put("refund_amount",money); // 退款金额
        bizContent.put("out_request_no", "HZ01RF001"); // 退款请求号

        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response = null;
        String body = null;
        try {
            response = alipayClient.execute(request);
            body = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        return body;
    }



    // 订单号生成
    public  String orderId() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNowStr = sdf.format(new Date());
        String replaceUUID = UUID.randomUUID().toString().replace("-", "");
        return dateNowStr+replaceUUID;
    }
}