package com.example.demo.controller;

import java.util.List;

import com.example.demo.exception.ExceptionGenerator;
import com.example.demo.exception.StatusEnum;
import com.example.demo.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.request.MemberCreateRequest;
import com.example.demo.controller.dto.request.MemberUpdateRequest;
import com.example.demo.controller.dto.response.MemberResponse;
import com.example.demo.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final ArticleService articleService;

    public MemberController(MemberService memberService,
                            ArticleService articleService) {
        this.memberService = memberService;
        this.articleService = articleService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> response = memberService.getAll();

        if (response.isEmpty())
            throw new ExceptionGenerator(StatusEnum.READ_NOT_PRESENT_MEMBER);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> getMember(
        @PathVariable Long id
    ) {
        if (memberService.getAll().stream()
                .noneMatch(member -> member.id().equals(id)))
            throw new ExceptionGenerator(StatusEnum.READ_NOT_PRESENT_MEMBER);

        MemberResponse response = memberService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> create(
        @Valid @RequestBody MemberCreateRequest request
    ) {
        MemberResponse response = memberService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(
        @PathVariable Long id,
        @Valid @RequestBody MemberUpdateRequest request
    ) {
        if (memberService.getAll().stream()
                .anyMatch(member -> member.email().equals(request.email())))
            throw new ExceptionGenerator(StatusEnum.EDIT_CONFLICT_EMAIL);

        MemberResponse response = memberService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(
        @PathVariable Long id
    ) {
        if (articleService.getAll().stream()
                .anyMatch(article -> article.getAuthorId().equals(id)))
            throw new ExceptionGenerator(StatusEnum.DELETE_MEMBER_PRESENT_ARTICLE);

        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
