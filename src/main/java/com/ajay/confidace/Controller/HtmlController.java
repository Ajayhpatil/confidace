package com.ajay.confidace.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class HtmlController {

        @GetMapping("/home")
        public String showHomePage() {
            return "home"; // refers to home.html
        }

        @GetMapping("/formm")
        public String showForm() {
            return "form";
        }

        @PostMapping("/submit")
        public String submitForm(@RequestParam String username,
                                 @RequestParam String password,
                                 Model model) {

            // Pass data to HTML
            model.addAttribute("user", username);
            model.addAttribute("pass", password);

            return "result";
        }

        @GetMapping("/exit")
        public String exitForm() {
            return "exit";
        }
    }

