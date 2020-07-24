package com.tk.prac4springboot.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@Controller
public class testApi {
    private Logger logger = LoggerFactory.getLogger(testApi.class);
//    @RequestMapping("/hello")
//    @ResponseBody
//    public String testHello()
//    {
//        logger.info("接口成功被访问");
//        logger.warn("该接口返回一个Json");
//        return "Hello,Visitor ---> this is a message from server";
//    }
    @RequestMapping("/bcCenter")
    public String bitCoin(){
        return "BitCoin.html";
    }
}
