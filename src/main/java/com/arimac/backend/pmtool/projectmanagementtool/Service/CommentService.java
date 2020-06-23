package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentAddDto;

public interface CommentService {
    Object addCommentToTask(CommentAddDto commentAddDto);
}
