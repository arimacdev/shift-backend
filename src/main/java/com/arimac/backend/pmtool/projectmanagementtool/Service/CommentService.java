package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.UpdateCommentDto;

public interface CommentService {
    Object addCommentToTask(CommentAddDto commentAddDto);
    Object updateComment(String commentId, UpdateCommentDto updateCommentDto);
}
