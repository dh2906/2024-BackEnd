package com.example.demo.controller;

import com.example.demo.controller.dto.request.MemberCreateRequest;
import com.example.demo.controller.dto.request.LogInRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.MemberService;
import com.example.demo.validate.MemberValidate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {
    private final MemberService memberService;
    private final AuthService authService;
    private final MemberValidate memberValidate;

    AuthController(MemberService memberService,
                   AuthService authService,
                   MemberValidate memberValidate) {
        this.memberService = memberService;
        this.authService = authService;
        this.memberValidate = memberValidate;
    }

    @GetMapping("/register")
    public String getSignUpPage() {
        return "register";
    }

    @PostMapping("/register")
    public String signUp(MemberCreateRequest request) {
        memberValidate.validateExistEmail(request.email());
        authService.register(request);

        return "/login";
    }

    @GetMapping("/login")
    public String getSignInPage() {
        return "login";
    }

    @PostMapping("/login")
    public String signIn(LogInRequest request) {
        if (authService.logIn(request)) {
            return "boardList";
        }
        return "login";
    }
}
