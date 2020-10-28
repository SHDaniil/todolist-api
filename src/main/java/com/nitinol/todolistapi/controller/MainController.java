package com.nitinol.todolistapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Главный контроллер
 */
@Controller
@RequestMapping("/")
public class MainController {

    /**
     * @return Переадресация на swagger-ui
     */
    @GetMapping
    public String swagger(){
        return "redirect:/swagger-ui.html";
    }
}
