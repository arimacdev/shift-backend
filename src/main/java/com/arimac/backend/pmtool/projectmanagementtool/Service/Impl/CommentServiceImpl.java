package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CommentService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.ReactionAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.UpdateCommentDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.CommentRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
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
    private final ProjectRepository projectRepository;
    private final UtilsService utilsService;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository, UtilsService utilsService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
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
        Project_User project_user = projectRepository.getProjectUser(task.getProjectId(), commentAddDto.getCommenter());
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.ASSIGNEE_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        Comment comment = new Comment();
        comment.setCommentId(utilsService.getUUId());
        comment.setCommenter(commentAddDto.getCommenter());
        comment.setEntityId(commentAddDto.getEntityId());
        comment.setContent(commentAddDto.getContent());
        comment.setCommentedAt(utilsService.getCurrentTimestamp());
        comment.setIsUpdated(false);
        comment.setIsDeleted(false);
        comment.setIsParent(true);
        commentRepository.addCommentToTask(comment);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object updateComment(String commentId, UpdateCommentDto updateCommentDto) {
        User commenter = userRepository.getUserByUserId(updateCommentDto.getCommenter());
        if (commenter == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Comment comment = commentRepository.getCommentById(commentId);
        if (comment == null)
            return new ErrorMessage(ResponseMessage.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!updateCommentDto.getCommenter().equals(comment.getCommenter()))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        commentRepository.updateComment(commentId, updateCommentDto);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object flagComment(String userId, String commentId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Comment comment = commentRepository.getCommentById(commentId);
        if (comment == null)
            return new ErrorMessage(ResponseMessage.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!comment.getCommenter().equals(userId))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        commentRepository.flagComment(commentId);
        //TODO Flag Reactions
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getTaskComments(String userId, String taskId, String startIndex, String endIndex) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(task.getProjectId(), userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        return null;
    }

    @Override
    public Object addOrUpdateReactionToComment(String userId, String commentId, ReactionAddDto reactionAddDto) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
         Comment comment = commentRepository.getCommentById(commentId);
        if (comment == null)
            return new ErrorMessage(ResponseMessage.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        Task task = taskRepository.getProjectTask(comment.getEntityId());
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(task.getProjectId(), userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        Reaction commentReaction = commentRepository.getCommentReaction(userId, commentId);
        if (commentReaction == null) {
            commentRepository.addCommentReaction(getReaction(commentId,reactionAddDto,userId));
        } else if (commentReaction.getReactionId().equals(reactionAddDto.getReactionId())){
            return new ErrorMessage(ResponseMessage.ALREADY_REACTED_WITH_REACTION, HttpStatus.UNPROCESSABLE_ENTITY);
        } else
           commentRepository.updateCommentReaction(getReaction(commentId,reactionAddDto,userId));
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    private Reaction getReaction (String commentId, ReactionAddDto reactionAddDto, String userId){
        Reaction reaction = new Reaction();
        reaction.setCommentId(commentId);
        reaction.setReactionId(reactionAddDto.getReactionId());
        reaction.setReactorId(userId);
        reaction.setReactedAt(utilsService.getCurrentTimestamp());

        return reaction;
    }
}
