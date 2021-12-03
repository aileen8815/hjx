package com.wwyl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/decorators")
public class DecoratorController {

    @RequestMapping(value = "/flatlab")
    public String flatlab() {
        return "/flatlab";
    }

    @RequestMapping(value = "/flatlab-h")
    public String flatlabh() {
        return "/flatlab_h";
    }

}
