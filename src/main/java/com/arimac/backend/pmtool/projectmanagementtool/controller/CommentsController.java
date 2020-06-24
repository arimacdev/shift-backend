package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CommentService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.UpdateCommentDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CommentsController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(CommentsController.class);

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "Add Comment to a Task", notes = "Add Comment to a Task")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping("/task/comment")
    public ResponseEntity<Object> addCommentToTask(@RequestHeader("userId") String userId, @RequestBody CommentAddDto commentAddDto){
        logger.info("HIT - GET task/comment---> addCommentToTask  userId: {} | Dto: {}", userId, commentAddDto);
        return sendResponse(commentService.addCommentToTask(commentAddDto));
    }

    @ApiOperation(value = "Update a comment", notes = "Update a comment")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PutMapping("/task/comment/{commentId}")
    public ResponseEntity<Object> updateComment(@RequestHeader("userId") String userId, @PathVariable("commentId") String commentId,  @RequestBody UpdateCommentDto updateCommentDto){
        logger.info("HIT - PUT task/comment/<commentId>---> updateComment  userId: {} | commentId: {} | Dto: {}", userId, commentId, updateCommentDto);
        return sendResponse(commentService.updateComment(commentId, updateCommentDto));
    }

    @ApiOperation(value = "Update a comment", notes = "Update a comment")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @DeleteMapping("/task/comment/{commentId}")
    public ResponseEntity<Object> flagComment(@RequestHeader("userId") String userId, @PathVariable("commentId") String commentId){
        logger.info("HIT - DELETE task/comment/<commentId>---> updateComment  userId: {} | commentId: {}", userId, commentId);
        return sendResponse(commentService.flagComment(userId, commentId));
    }


//    @ApiOperation(value = "Get Parent Comments", notes = "Get Parent Comments")
//    @ApiResponse(code = 200, message = "Success", response = List.class)
//    @PostMapping("/task/comment")
//    public ResponseEntity<Object> getParentComments(@RequestHeader("userId") String userId, @RequestBody CommentAddDto commentAddDto){
//        logger.info("HIT - GET task/<taskId>/comment---> addCommentToTask  userId: {} | Dto: {}", userId, commentAddDto);
//        return sendResponse(commentService.addCommentToTask(commentAddDto));
//    }




}
