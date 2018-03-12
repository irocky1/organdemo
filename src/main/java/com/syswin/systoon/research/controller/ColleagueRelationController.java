package com.syswin.systoon.research.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rocky on 2018/1/23.
 */
@RequestMapping("/cr")
@RestController
public class ColleagueRelationController {

    @RequestMapping("/i")
    public String index() {
        return "Here is colleague-releation demo.";
    }

}
