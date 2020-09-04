package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CommentService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.*;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, comment);
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
    public Object getTaskComments(String userId, String taskId, int startIndex, int endIndex, boolean allComments) {
        int limit = endIndex - startIndex;
        if (!allComments) {
            if (startIndex < 0 || endIndex < 0 || endIndex < startIndex)
                return new ErrorMessage("Invalid Start/End Index Combination", HttpStatus.BAD_REQUEST);
            if (limit > 11)
                return new ErrorMessage(ResponseMessage.REQUEST_ITEM_LIMIT_EXCEEDED, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(task.getProjectId(), userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        List<CommentReaction> commentReactionList = commentRepository.getTaskComments(taskId, limit, startIndex );
        Map<String, CommentReactionResponse> commentReactionResponseMap = new HashMap<>();
        for (CommentReaction commentReaction : commentReactionList){
            UserReaction userReaction = new UserReaction();
            if (commentReaction.getReactionId()!= null){
                userReaction.setReactionId(commentReaction.getReactionId());
                userReaction.setResponderId(commentReaction.getResponderId());
                userReaction.setResponderFirstName(commentReaction.getResponderFirstName());
                userReaction.setResponderLastName(commentReaction.getResponderLastName());
                userReaction.setResponderProfileImage(commentReaction.getResponderProfileImage());
            }
            if (commentReactionResponseMap.get(commentReaction.getCommentId())!= null){
                Map<String, ReactionRespondants> userReactions = commentReactionResponseMap.get(commentReaction.getCommentId()).getReactions();
                if (userReactions.get(commentReaction.getReactionId())!= null){
                    ReactionRespondants reactionRespondants = userReactions.get(commentReaction.getReactionId());
                    reactionRespondants.getRespondants().add(userReaction);
                   // int res = reactionRespondants.getResponses();
                    //reactionRespondants.setResponses(res + 1);
                } else {
                    ReactionRespondants reactionRespondants = new ReactionRespondants();
                    reactionRespondants.setReactionId(commentReaction.getReactionId());
                    List<UserReaction> uReactions = new ArrayList<>();
                    uReactions.add(userReaction);
                    reactionRespondants.setRespondants(uReactions);
                    userReactions.put(commentReaction.getReactionId(), reactionRespondants);
                }
//                if (commentReaction.getReactionId()!= null){
//                    userReactions.add(userReaction);
                    commentReactionResponseMap.get(commentReaction.getCommentId()).setReactions(userReactions);
//                }
            } else {
                CommentReactionResponse commentReactionResponse = new CommentReactionResponse();
                commentReactionResponse.setCommentId(commentReaction.getCommentId());
                commentReactionResponse.setContent(commentReaction.getContent());
                commentReactionResponse.setIsEdited(commentReaction.getIsUpdated());
                commentReactionResponse.setCommenter(commentReaction.getCommenterId());
                commentReactionResponse.setCommenterFistName(commentReaction.getCommenterFirstName());
                commentReactionResponse.setCommenterLatName(commentReaction.getCommenterLastName());
                commentReactionResponse.setCommenterProfileImage(commentReaction.getCommenterProfileImage());
                commentReactionResponse.setCommentedAt(commentReaction.getCommentedAt());
                //List<UserReaction> userReactionList = new ArrayList<>();
                Map<String, ReactionRespondants> userReactions = new HashMap<>();
                if (commentReaction.getReactionId()!= null) {
                    ReactionRespondants reactionRespondants = new ReactionRespondants();
                    reactionRespondants.setReactionId(commentReaction.getReactionId());
                    List<UserReaction> uReaction = new ArrayList<>();
                    uReaction.add(userReaction);
                    //userReactionList.add(userReaction);
                    reactionRespondants.setRespondants(uReaction);
                   // reactionRespondants.setResponses(1);
                    userReactions.put(commentReaction.getReactionId(), reactionRespondants);
                }
                commentReactionResponse.setReactions(userReactions);
                commentReactionResponseMap.put(commentReaction.getCommentId(), commentReactionResponse );
            }
        }
        List<CommentReactionResponse> commentReactionResponseList = new ArrayList<>(commentReactionResponseMap.values());
        List<CommentReactionList> commentReactionLists = new ArrayList<>();
        for (CommentReactionResponse commentReactionResponse: commentReactionResponseList){
            CommentReactionList reactionList = new CommentReactionList();
            reactionList.setCommentId(commentReactionResponse.getCommentId());
            reactionList.setCommenter(commentReactionResponse.getCommenter());
            reactionList.setCommenterFistName(commentReactionResponse.getCommenterFistName());
            reactionList.setCommenterLatName(commentReactionResponse.getCommenterLatName());
            reactionList.setCommenterProfileImage(commentReactionResponse.getCommenterProfileImage());
            reactionList.setCommentedAt(commentReactionResponse.getCommentedAt());
            reactionList.setContent(commentReactionResponse.getContent());
            reactionList.setReactions(new ArrayList<>(commentReactionResponse.getReactions().values()));
            reactionList.setIsEdited(commentReactionResponse.getIsEdited());
           // List<ReactionRespondants> reactionRespondants = new ArrayList<>(commentReactionResponse.getReactions().values());
           commentReactionLists.add(reactionList);
        }
        //TODO
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, commentReactionLists);
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
           // return new ErrorMessage(ResponseMessage.ALREADY_REACTED_WITH_REACTION, HttpStatus.UNPROCESSABLE_ENTITY);
            commentRepository.removeUserCommentReaction(userId, commentId);
        } else if (!commentReaction.getReactorId().equals(userId)) {
            return new ErrorMessage(ResponseMessage.NOT_REACTOR, HttpStatus.UNAUTHORIZED);
        } else
            commentRepository.updateCommentReaction(getReaction(commentId, reactionAddDto, userId));

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object removeUserCommentReaction(String userId, String commentId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Comment comment = commentRepository.getCommentById(commentId);
        if (comment == null)
            return new ErrorMessage(ResponseMessage.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        Reaction commentReaction = commentRepository.getCommentReaction(userId, commentId);
        if (commentReaction == null)
            return new ErrorMessage(ResponseMessage.REACTION_NOT_FOUND, HttpStatus.NOT_FOUND);
        commentRepository.removeUserCommentReaction(userId, commentId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getCommentCountOfTask(String userId, String taskId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        int count = commentRepository.getCommentCountOfTask(taskId);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, count);
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
