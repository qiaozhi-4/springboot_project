<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>电脑网站支付return_url</title>
</head>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.alipay.api.internal.util.*" %>
<%@ page import="com.zking.alipay.AlipayConfig" %>
<%
    /* *
     * 功能：支付宝服务器同步通知页面
     * 日期：2017-03-30
     * 说明：
     * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
     * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。


     *************************页面功能说明*************************
     * 该页面仅做页面展示，业务逻辑处理请勿在该页面执行
     */

    //获取支付宝GET过来反馈信息
    Map<String, String> params = new HashMap<String, String>();
    Map<String, String[]> requestParams = request.getParameterMap();
    for (String name : requestParams.keySet())
    {
        String[] values = (String[]) requestParams.get(name);
        String valueStr = "";
        for (int i = 0; i < values.length; i++)
        {
            valueStr = (i == values.length - 1) ? valueStr + values[i]
                    : valueStr + values[i] + ",";
        }
        //乱码解决，这段代码在出现乱码时使用
        // valueStr = valueStr.getBytes("ISO-8859-1"), "utf-8");
        params.put(name, valueStr);
    }

    //调用SDK验证签名
    boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

    //——请在这里编写您的程序（以下代码仅作参考）——
    if (signVerified)
    {
        //商户订单号
        String out_trade_no = request.getParameter("out_trade_no");

        //支付宝交易号
        String trade_no = request.getParameter("trade_no");

        //付款金额
        String total_amount = request.getParameter("total_amount");

        out.println("trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount);
    } else
    {
        out.println("验签失败");
    }
    //——请在这里编写您的程序（以上代码仅作参考）——
%>
<body>
</body>
</html>