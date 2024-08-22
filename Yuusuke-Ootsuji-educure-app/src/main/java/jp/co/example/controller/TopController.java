package jp.co.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class TopController {
	
	private final HttpSession session;

    public TopController(HttpSession session) {
        this.session = session;
    }

    @GetMapping("/top")
    public String getTop() {
        return "top";
    }
}
