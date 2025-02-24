package org.example.service;

import org.example.dao.CommentDao;
import org.example.dto.CommentDto;
import org.example.entity.Comment;
import org.example.mapper.CreateCommentMapper;

public class CommentService {

    private static final CommentService INSTANCE = new CommentService();
    private final CommentDao commentDao = CommentDao.getInstance();
    private final CreateCommentMapper createCommentMapper = CreateCommentMapper.getInstance();

    public Integer createComment(CommentDto comment) {
        Comment mappedComment = createCommentMapper.mapFrom(comment);
        commentDao.save(mappedComment);
        return mappedComment.getId();
    }

    public String getAllComments() {
       return commentDao.getAllComments();
    }


    public static CommentService getInstance() {return INSTANCE ;}
}
