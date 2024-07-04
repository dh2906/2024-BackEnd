package com.example.demo.controller;

import com.example.demo.controller.dto.request.MemberCreateRequest;
import com.example.demo.controller.dto.request.SignInRequest;
import com.example.demo.service.MemberService;
import com.example.demo.validate.MemberValidate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {
    private final MemberService memberService;
    private final MemberValidate memberValidate;
    private final PasswordEncoder passwordEncoder;

    AccountController(MemberService memberService,
                      MemberValidate memberValidate,
                      PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.memberValidate = memberValidate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signUp";
    }

    @PostMapping("/signup")
    public String signUp(MemberCreateRequest request) {
        memberValidate.validateExistEmail(request.email());
        memberService.create(request);

        return "signIn";
    }

    @GetMapping("/signin")
    public String getSignInPage() {
        return "signIn";
    }

    @PostMapping("/signin")
    public String signIn(SignInRequest request) {
        if (passwordEncoder.matches(
                request.password(),
                memberService
                        .getByEmail(request.email())
                        .getPassword())
        )
            return "boardList";

        return "signIn";
    }
}
