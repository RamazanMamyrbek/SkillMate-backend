package ru.skillmate.backend.controllers.users;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/")
    public String demo() {
        return "Hello";
    }
}
