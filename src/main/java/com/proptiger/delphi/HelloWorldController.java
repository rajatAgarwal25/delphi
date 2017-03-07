package com.proptiger.delphi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/hello/")
public class HelloWorldController {

    private static Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    @RequestMapping(value = "world", method = RequestMethod.GET)
    @ResponseBody
    public String printHello() {
        return "hello-world";
    }
}
