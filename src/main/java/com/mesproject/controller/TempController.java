package com.mesproject.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class TempController {

    @GetMapping(value = "/test")
    public String test() {
        return "getting-started";
    }

    @GetMapping(value = "/test2")
    public String test2() {
        return "getting-started-copy";
    }

}

