package test.cash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Not implemented yet
 */
@Controller
public class AdminController {

    @GetMapping("/admin")
    public String home() {
        return "admin/admin";
    }
}
