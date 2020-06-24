package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.UpdateCommentDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Comment;

public interface CommentRepository {
    void addCommentToTask(Comment comment);
    void updateComment(String commentId, UpdateCommentDto updateCommentDto);
    void deleteComment(String commentId);
    Comment getCommentById(String commentId);
}
