package com.nitinol.todolistapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String swagger(){
        return "redirect:/swagger-ui.html";
    }
}
