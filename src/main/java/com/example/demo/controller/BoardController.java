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

import com.example.demo.controller.dto.request.BoardCreateRequest;
import com.example.demo.controller.dto.request.BoardUpdateRequest;
import com.example.demo.controller.dto.response.BoardResponse;
import com.example.demo.service.BoardService;

@RestController
public class BoardController {
    private final BoardService boardService;
    private final ArticleService articleService;

    public BoardController(BoardService boardService,
                           ArticleService articleService) {
        this.boardService = boardService;
        this.articleService = articleService;
    }

    @GetMapping("/boards")
    public List<BoardResponse> getBoards() {
        List<BoardResponse> response = boardService.getBoards();

        if (response.isEmpty())
            throw new ExceptionGenerator(StatusEnum.READ_NOT_PRESENT_BOARD);

        return response;
    }

    @GetMapping("/boards/{id}")
    public BoardResponse getBoard(
        @PathVariable Long id
    ) {
        if (boardService.getBoards().stream()
                .noneMatch(board -> board.id().equals(id)))
            throw new ExceptionGenerator(StatusEnum.READ_NOT_PRESENT_BOARD);

        return boardService.getBoardById(id);
    }

    @PostMapping("/boards")
    public BoardResponse createBoard(
        @Valid @RequestBody BoardCreateRequest request
    ) {
        return boardService.createBoard(request);
    }

    @PutMapping("/boards/{id}")
    public BoardResponse updateBoard(
        @PathVariable Long id,
        @Valid @RequestBody BoardUpdateRequest updateRequest
    ) {
        return boardService.update(id, updateRequest);
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> deleteBoard(
        @PathVariable Long id
    ) {
        if (articleService.getAll().stream()
                .anyMatch(article -> article.getBoardId().equals(id)))
            throw new ExceptionGenerator(StatusEnum.DELETE_BOARD_PRESENT_ARTICLE);
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
