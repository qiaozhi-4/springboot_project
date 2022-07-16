package com.zking.advice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

// @Component // 这里不用@Component这些注解！
@ControllerAdvice(/*basePackages = {"com.zking.*"}, // 指定包*/annotations = {Controller.class})  // 指定被增强注解类型
// @ResponseBody
public class MyAdviceController {

    //注解这些异常来这里处理
    //@ExceptionHandler({RuntimeException.class,Exception.class})
    //public ModelAndView handleExl(HttpServletResponse response) throws IOException {
    //    response.setContentType("text/html;charset=utf-8");
    //    response.getWriter().write("你爆了");
    //    return new ModelAndView();
    //}


}
