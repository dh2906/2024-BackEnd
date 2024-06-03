package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import com.example.demo.exception.ExceptionGenerator;
import com.example.demo.exception.StatusEnum;
import com.example.demo.service.BoardService;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.request.ArticleCreateRequest;
import com.example.demo.controller.dto.response.ArticleResponse;
import com.example.demo.controller.dto.request.ArticleUpdateRequest;
import com.example.demo.service.ArticleService;

@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final BoardService boardService;
    private final MemberService memberService;

    public ArticleController(ArticleService articleService,
                             BoardService boardService,
                             MemberService memberService) {
        this.articleService = articleService;
        this.boardService = boardService;
        this.memberService = memberService;
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> getArticles(
        @RequestParam Long boardId
    ) {
        List<ArticleResponse> response = articleService.getByBoardId(boardId);

        if (response.isEmpty())
            throw new ExceptionGenerator(StatusEnum.READ_NOT_PRESENT_ARTICLE);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> getArticle(
        @PathVariable Long id
    ) {
        if (articleService.getAll().stream()
                .noneMatch(article -> article.getId().equals(id)))
            throw new ExceptionGenerator(StatusEnum.READ_NOT_PRESENT_ARTICLE);

        ArticleResponse response = articleService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> crateArticle(
        @Valid @RequestBody ArticleCreateRequest request
    ) {
        if (boardService.getBoards().stream()
                .noneMatch(board -> board.id().equals(request.boardId())) ||
                memberService.getAll().stream()
                        .noneMatch(member -> member.id().equals(request.authorId())))
            throw new ExceptionGenerator(StatusEnum.CREATE_NOT_PRESENT_BOARD);

        ArticleResponse response = articleService.create(request);
        return ResponseEntity.created(URI.create("/articles/" + response.id())).body(response);
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(
        @PathVariable Long id,
        @Valid @RequestBody ArticleUpdateRequest request
    ) {
        if (boardService.getBoards().stream()
                .noneMatch(board -> board.id().equals(request.boardId())))
            throw new ExceptionGenerator(StatusEnum.EDIT_NOT_PRESENT_BOARD);

        ArticleResponse response = articleService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> updateArticle(
        @PathVariable Long id
    ) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
