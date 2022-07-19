package com.zking.alipay;

import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import java.io.FileWriter;
import java.util.Objects;

// 2. 支付配置，唯一需要修改的是：url端口号、return_url链接
public class AlipayConfig
{
    /**
     * 项目配置：必须为spring项目，在web.xml中配置port端口
     *
     * 测试账号：
     *  买家账号gkylrk9430@sandbox.com
     *  登录密码111111
     *  支付密码111111
     * 参考地址：
     *  https://open.alipay.com/platform/appDaily.htm?tab=info
     *  https://opendocs.alipay.com/open/270/106291/
     * 注意：测试的时候换个浏览器，要么退出账号
    */
    // 应用ID，您的APPID，可以使用自己的沙箱测试ID
    public static final String app_id = "2021000119653835";
    // 商户私钥，您的PKCS8格式RSA2私钥，使用自己的
    public static final String merchant_private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCNie/qLWRKIwzHTMVNWIVf6Rw0KQ7Z+GnG6OUutb3PsM6GVBmYueKFjQYJ10Kd2K7GTtuI5uJta3e2U1A1g2l0l2bJgfy6DBLYQnf7zSdc4/d1UhvrDmXZIKXjGnMbt8FnMscbMVhJpWJH7cAGvM+rZGFJbv5F3etwLJiD0Ah46JRRDe5tlccZSG7upvpOieFWYeh/lqV46v8OcTiiemr7262It6gWUVtNtvCK90ssMMM1pF5ho6GJoPG0WEC4v56oZw/qtVu8oKLuIYnA3U5PbOAZxL0W7KhO6Miy+g02X/eFoIFyp6mtbhgVxACtOUo/blD5elQ94fIU9/xwDHotAgMBAAECggEBAIsl5tXE1moEV1XAZeCKfGU0WeP10YH9NJC2+kmXpwn+equZFqkNZmOBp0QZeoaQNhMEB9RpmIWWN9Tpx1s03IIhLo5A2uNVZ1lwz2Cf6mjqPaf7lzLZrstty2if4jaHyv3X7+MN+gw9KYgpdrWo/kkzoGl/U0AA0qwvS6ma5hlmmussZLrS6YqC5+bNEiTkEPJ9Y0YeqqcaY5L9xeg4ocwAa8nciax9eptfWEuSYBb7Ut3IAFRmqjgtA1pjX1PPHwyy/K3fQeLKZjrsMSq5t0jRWXCDmqR3CG7EnUNo2VH3pU1mQnTTxe2Rl6B+kOy0L+GqGQ6g3p4nxeU6vWsDgEECgYEA91L5c/qUujgWS+11FiMlYQfA0oV8LDoYRfIbE097MBNKjO47MCnldwrlj4lQAL2fGyiotjP3AwZuW1FRIX5+FljagGglVnhFIOdtQpZdjW9czfLVjpgDbCNsvUVBYg85dkoHkoOIdPsmdDjGE3IlsMGdfHU0XuMzXzNxMeN9rTUCgYEAkoD8ue4K+WOQ7iEMidk3QwVL7lFBDSQB0/DfA73qndVRwQVBa0bBAN+UsSb97xic4rdUvvWxximohFm0HOL18N94TqFnU7Mp8Nw4T3+UckPozkG99KsCEpP+gw6HSxtithLXSnuNrz5hZfNspirKH3bEZhZ1mfC4ZUNxLorOUBkCgYEAwfZjxi4917FG4zMOAVd86BTRhwjCsli5Upml3EYYIULv04i+CG7DE9IK6L/Sa1JK5lzDu2kTjl7Ly3gBfZgbQGFcsxl6Ns9Jv51f1bnhEpvEl4ubKiJThMpfbCxVM5cnBHrYZ3IaYUvaA4nG5dc+cdnWLZYphUT/s+i/HFYB6QUCgYAnEitUUr2ugPU37KxxXvKz9wenCRh2LrImGckAMHgPm8Jv3bzypS3306jODf4O89tzQz1gqBm/JYEBuwKj6Yt5r79gCP+LfO6kg7aozPeD+cRurW3BgJHO7pUj0o4WY7lRoXi1Lno80EAddmv1bNf2yC/cogS/B0nGB9R5ex9GkQKBgQDnEIyTTOa5I1WHBoA31/0UrWuKpF9nkz8TfJuZFvuHUz4dDuWC2NRK170lm/iyqIg+8MZLVphf0e0h/Tim4op4f/bDEO1+WYQT+vIGna5Ptcpd0+Gzm4vl7CQ6fwNuwovxQ1gYiPwU094/ZpzREdpiyWu3RBqWRv7pJQrXywGulA==\", \"json\", \"utf-8\", \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxcb7665b27YnKEvHF1PwjmhFWWldMLXwOQ56mRnMQCD2QM4BAPiofkobvr+9iL1l8BO/8Oy/k/Ap6s1usB3YRfJGQnReGIiQxiZNjqCoUl6AFK2gxKbZFF7wbwYM/pB9kENvWuN1Uu5vm0oUQO2i29PHAAQwMxkSgO18eEzNIiWNOoEfrFz17bzIK1heD5QtlA59IWI0aSlhIkJLx5hWOmOuQUsQ59I88ZsEM5Zb1YNymLZM7zjnbmSKT44Bg3+evnXLYc/byBCvPXVJmxRiNFS14myi7LrCd+j4r7Zp48NLhsPzcDzYRt+2MHMmz1vmWKicANAuHiK9agCd4QDOvwIDAQAB";
    // 支付宝公钥，对应APPID下的支付宝公钥，使用自己的
    public static final String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxcb7665b27YnKEvHF1PwjmhFWWldMLXwOQ56mRnMQCD2QM4BAPiofkobvr+9iL1l8BO/8Oy/k/Ap6s1usB3YRfJGQnReGIiQxiZNjqCoUl6AFK2gxKbZFF7wbwYM/pB9kENvWuN1Uu5vm0oUQO2i29PHAAQwMxkSgO18eEzNIiWNOoEfrFz17bzIK1heD5QtlA59IWI0aSlhIkJLx5hWOmOuQUsQ59I88ZsEM5Zb1YNymLZM7zjnbmSKT44Bg3+evnXLYc/byBCvPXVJmxRiNFS14myi7LrCd+j4r7Zp48NLhsPzcDzYRt+2MHMmz1vmWKicANAuHiK9agCd4QDOvwIDAQAB";
    // 签名方式
    public static final String sign_type = "RSA2";
    // 字符编码格式
    public static final String charset = "utf-8";
    // 支付宝网关：必须为alipaydev
    public static final String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
    // 这是日志输出目录：这个自定义
    public static final String log_path = "D:\\Othertemp\\Cache\\Temp";
    
    // 服务器异步通知页面路径,需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static final String notify_url;
    // 页面跳转同步通知页面路径,需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static final String return_url;
    static {
        ServletContext context = Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getServletContext();
        String contextPath = context == null ? "" : context.getContextPath();
        // String port = context == null ? "80" : context.getInitParameter("port");
        String port = "8080";
        return_url = "http://localhost:" + port + contextPath + "/alipay/return_url";
        notify_url = "http://localhost:" + port + contextPath + "/alipay/notify_url";
        System.out.printf("通知路径：%s%n跳转路径：%s%n", notify_url, return_url);
    }
    
    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord)
    {
        try(FileWriter writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt"))
        {
            writer.write(sWord);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
