package top.soulblack.quick.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quick")
public class IndexController {

    @Autowired
    private MessageSource messageSource;

    @ApiOperation(value = "index接口", httpMethod = "GET")
    @GetMapping("/index")
    public String index() {
        return "hello-world";
    }

    @ApiOperation(value = "测试语言国际化", httpMethod = "GET")
    @GetMapping("/language")
    public String language(@RequestParam(value = "language") String language) {
        return messageSource.getMessage(language, null, LocaleContextHolder.getLocale());
    }

}
