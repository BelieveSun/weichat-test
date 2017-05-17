package com.believe.sun.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sungj on 17-5-16.
 */
@RestController("/")
public class WecomeController {
    @RequestMapping("")
    public ModelAndView index(){
        return new ModelAndView("index.jsp");
    }
}
