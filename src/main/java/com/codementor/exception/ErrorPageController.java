package com.codementor.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {

    @GetMapping("/error/403")
    public String forbiddenPage() {
        return "error/403";
    }
}
