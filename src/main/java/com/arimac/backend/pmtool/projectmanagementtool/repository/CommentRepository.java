package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentReaction;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.UpdateCommentDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Comment;
import com.arimac.backend.pmtool.projectmanagementtool.model.Reaction;

import java.util.List;

public interface CommentRepository {
    void addCommentToTask(Comment comment);
    void updateComment(String commentId, UpdateCommentDto updateCommentDto);
    void flagComment(String commentId);
    List<CommentReaction> getTaskComments(String taskId, int limit, int offset);
    Comment getCommentById(String commentId);
    void addCommentReaction(Reaction reaction);
    Reaction getCommentReaction(String userId, String commentId);
    void updateCommentReaction(Reaction reaction);
    void removeUserCommentReaction(String userId, String commentId);
}
