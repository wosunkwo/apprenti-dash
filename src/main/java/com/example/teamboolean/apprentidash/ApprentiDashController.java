package com.example.teamboolean.apprentidash;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApprentiDashController {

    @GetMapping("/")
    public String getHome(){
        return "home";
    }
}
