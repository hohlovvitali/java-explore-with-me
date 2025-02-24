package ru.practicum.ewm.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid NewCommentDto newCommentDto,
                                    @PathVariable Long eventId,
                                    @PathVariable Long userId) {
        log.info("Запрос на создание комментария пользователем с ID={} для события ID={}", userId, eventId);
        return commentService.createComment(newCommentDto, eventId, userId);
    }

    @PatchMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Запрос на обновление комментария с ID={} пользователем ID={}", commentId, userId);
        return commentService.updateComment(userId, commentId, newCommentDto);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("Запрос на удаление комментария с ID={} пользователем ID={}", commentId, userId);
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsByUser(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение комментариев пользователя с ID={}, from={}, size={}", userId, from, size);
        return commentService.getCommentsByUser(userId, PageRequest.of(from / size, size));
    }
}