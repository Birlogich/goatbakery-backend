package org.example.mapper;

import org.example.dto.CommentDto;

import org.example.entity.Comment;


public class CreateCommentMapper implements Mapper<CommentDto, Comment>{

    private static final CreateCommentMapper INSTANCE = new CreateCommentMapper();

    @Override
    public Comment mapFrom(CommentDto object) {
        return Comment.builder()
                .item(object.getItem())
                .user(object.getUser())
                .comment(object.getComment())
                .build();
    }

    public static CreateCommentMapper getInstance() {
        return INSTANCE;
    }
}
