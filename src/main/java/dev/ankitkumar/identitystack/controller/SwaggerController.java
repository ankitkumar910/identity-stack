package dev.ankitkumar.identitystack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("")
public class SwaggerController {
    @GetMapping
    public String goToSwaggerHome(){
        return "redirect:/swagger-ui/index.html";
    }
}
