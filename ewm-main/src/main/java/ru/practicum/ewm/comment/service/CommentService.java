package ru.practicum.ewm.comment.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(NewCommentDto newCommentDto, Long eventId, Long userId);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto);

    void deleteComment(Long userId, Long commentId);

    List<CommentDto> getCommentsByEvent(Long eventId, Pageable pageable);

    List<CommentDto> getCommentsByUser(Long userId, Pageable pageable);

    void deleteCommentByAdmin(Long commentId);
}