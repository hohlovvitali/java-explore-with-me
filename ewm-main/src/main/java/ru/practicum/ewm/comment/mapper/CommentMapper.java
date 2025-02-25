package ru.practicum.ewm.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(NewCommentDto newCommentDto, Event event, User author) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .event(event)
                .author(author)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        EventShortDto eventShortDto = EventShortDto.builder()
                .id(comment.getEvent().getId())
                .title(comment.getEvent().getTitle())
                .build();

        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .createdOn(comment.getCreatedOn())
                .event(eventShortDto)
                .build();
    }
}