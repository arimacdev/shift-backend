package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.UpdateCommentDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Comment;
import com.arimac.backend.pmtool.projectmanagementtool.model.Reaction;

public interface CommentRepository {
    void addCommentToTask(Comment comment);
    void updateComment(String commentId, UpdateCommentDto updateCommentDto);
    void deleteComment(String commentId);
    Comment getCommentById(String commentId);
    void addCommentReaction(Reaction reaction);
}
