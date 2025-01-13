package org.example.oauthjan25.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "Hello, this is home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "This is a secured ulr";
    }

}
