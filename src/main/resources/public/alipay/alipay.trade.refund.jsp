<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>退款</title>
</head>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.alipay.api.*" %>
<%@ page import="com.alipay.api.request.*" %>
<%@ page import="com.zking.alipay.AlipayConfig" %>
<%
    //获得初始化的AlipayClient
    AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

    //设置请求参数
    AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

    //商户订单号，商户网站订单系统中唯一订单号
    String out_trade_no = request.getParameter("WIDTRout_trade_no");
    //支付宝交易号
    String trade_no = request.getParameter("WIDTRtrade_no");
    //请二选一设置
    //需要退款的金额，该金额不能大于订单金额，必填
    String refund_amount = request.getParameter("WIDTRrefund_amount");
    //退款的原因说明
    String refund_reason = request.getParameter("WIDTRrefund_reason");
    //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
    String out_request_no = request.getParameter("WIDTRout_request_no");

    alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                                        + "\"trade_no\":\"" + trade_no + "\","
                                        + "\"refund_amount\":\"" + refund_amount + "\","
                                        + "\"refund_reason\":\"" + refund_reason + "\","
                                        + "\"out_request_no\":\"" + out_request_no + "\"}");

    //请求
    String result = null;
    try
    {
        result = alipayClient.execute(alipayRequest).getBody();
    } catch (AlipayApiException e)
    {
        e.printStackTrace();
    }

    //输出
    out.println(result);
%>
<body>
</body>
</html>