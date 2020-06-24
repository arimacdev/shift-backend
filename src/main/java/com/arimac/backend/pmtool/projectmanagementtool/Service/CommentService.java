package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.ReactionAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.UpdateCommentDto;

public interface CommentService {
    Object addCommentToTask(CommentAddDto commentAddDto);
    Object updateComment(String commentId, UpdateCommentDto updateCommentDto);
    Object flagComment(String userId, String commentId);
    Object getTaskComments(String userId, String taskId,  String startIndex, String endIndex);
    Object addOrUpdateReactionToComment(String userId, String commentId, ReactionAddDto reactionAddDto);
}
