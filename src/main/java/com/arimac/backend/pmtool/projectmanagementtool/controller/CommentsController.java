package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CommentService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentAddDto;
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
        logger.info("HIT - GET task/<taskId>/comment---> addCommentToTask  userId: {} | Dto: {}", userId, commentAddDto);
        return sendResponse(commentService.addCommentToTask(commentAddDto));
    }
}
