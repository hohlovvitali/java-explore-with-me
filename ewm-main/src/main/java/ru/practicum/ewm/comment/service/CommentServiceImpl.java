package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto createComment(NewCommentDto newCommentDto, Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID: " + eventId + " не найдено"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + userId + " не найден"));

        if (event.getInitiator().getId().equals(user.getId())) {
            log.warn("Пользователь с ID={} пытается оставить комментарий к событию с ID={}, " +
                    "инициатором которого является он сам", userId, eventId);
            throw new ValidationException("Инициатор не может оставить комментарий к своему мероприятию");
        }

        log.debug("Создаем комментарий: {}", newCommentDto);
        log.debug("Событие найдено: {}", event);
        log.debug("Автор найден: {}", user);
        Comment comment = CommentMapper.toComment(newCommentDto, event, user);
        log.debug("Комментарий перед сохранением: {}", comment);

        comment = commentRepository.save(comment);
        log.debug("Комментарий после сохранения: {}", comment);

        log.info("Комментарий успешно добавлен пользователем с ID={} и именем {} к событию с ID={}",
                userId, user.getName(), eventId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ID " + commentId + " не найден"));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            log.warn("Пользователь с ID={} пытается редактировать комментарий с ID={}," +
                    "автором которого сам не является", userId, commentId);
            throw new ValidationException("Редактировать комментарий может только автор этого комментария");
        }

        comment.setText(newCommentDto.getText());
        comment = commentRepository.save(comment);
        log.info("Комментарий с ID={} успешно обновлен его автором с ID={}", commentId, userId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с Id " + commentId + " не найден"));

        if (!comment.getAuthor().getId().equals(userId)) {
            log.warn("Пользователь с ID={} пытается удалить комментарий с ID={}, автором которого не является", userId, commentId);
            throw new ValidationException("Удалять комментарий может только автор этого комментария");
        }

        commentRepository.delete(comment);
        log.info("Комментарий с ID={} успешно удалено автором с ID={}", commentId, userId);
    }


    @Override
    public List<CommentDto> getCommentsByEvent(Long eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID " + eventId + " не найдено"));

        Page<Comment> commentPage = commentRepository.findAllByEventId(eventId, pageable);

        if (commentPage.getTotalElements() == 0) {
            log.info("Нет комментариев для события с ID={}", eventId);
        } else {
            log.info("Найдено {} комментариев для события с ID={}", commentPage.getTotalElements(), eventId);
        }

        return commentPage.getContent().stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        Page<Comment> commentPage = commentRepository.findAllByAuthorId(userId, pageable);

        if (commentPage.getTotalElements() == 0) {
            log.info("У пользователя с ID={} нет комментариев", userId);
        } else {
            log.info("Найдено {} комментариев для пользователя с ID={}", commentPage.getTotalElements(), userId);
        }

        return commentPage.getContent().stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ID " + commentId + " не найден"));
        commentRepository.delete(comment);
        log.info("Комментарий с ID={} удален администратором", commentId);
    }
}