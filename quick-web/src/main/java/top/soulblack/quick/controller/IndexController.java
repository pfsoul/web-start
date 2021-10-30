package top.soulblack.quick.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quick")
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "hello-world";
    }

}
