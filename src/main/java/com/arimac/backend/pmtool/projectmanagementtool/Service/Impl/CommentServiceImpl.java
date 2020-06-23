package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CommentService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Comment;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.CommentRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UtilsService utilsService;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository, UtilsService utilsService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object addCommentToTask(CommentAddDto commentAddDto) {
        User commenter = userRepository.getUserByUserId(commentAddDto.getCommenter());
        if (commenter == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Task task = taskRepository.getProjectTask(commentAddDto.getEntityId());
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        Comment comment = new Comment();
        comment.setCommentId(utilsService.getUUId());
        comment.setCommenter(commentAddDto.getCommenter());
        comment.setEntityId(commentAddDto.getEntityId());
        comment.setContent(commentAddDto.getContent());
        comment.setCommentedAt(utilsService.getCurrentTimestamp());
        comment.setIsUpdated(false);
        comment.setIsDeleted(false);
        if (commentAddDto.getParentId()!= null && !commentAddDto.getParentId().isEmpty()){
//            Comment parentComment = taskRepository.getProjectTask(commentAddDto.getParentId());
//            if (pare == null)
//                return new ErrorMessage(Res)
            comment.setParentId(commentAddDto.getParentId());
            comment.setIsParent(false);
        } else {
            comment.setIsParent(true);
        }
        commentRepository.addCommentToTask(comment);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
