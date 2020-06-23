package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Comment;

public interface CommentRepository {
    void addCommentToTask(Comment comment);
}
