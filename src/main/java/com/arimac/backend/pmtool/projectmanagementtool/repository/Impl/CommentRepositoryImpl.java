package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.CommentReaction;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.UpdateCommentDto;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Comment;
import com.arimac.backend.pmtool.projectmanagementtool.model.Reaction;
import com.arimac.backend.pmtool.projectmanagementtool.repository.CommentRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class CommentRepositoryImpl implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void addCommentToTask(Comment comment) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Comment (commentId,entityId,content,commenter,commentedAt,isParent,parentId,isUpdated,isDeleted) VALUES(?,?,?,?,?,?,?,?,?)");
                preparedStatement.setString(1, comment.getCommentId());
                preparedStatement.setString(2,comment.getEntityId());
                preparedStatement.setString(3,comment.getContent());
                preparedStatement.setString(4, comment.getCommenter());
                preparedStatement.setTimestamp(5, comment.getCommentedAt());
                preparedStatement.setBoolean(6, comment.getIsParent());
                preparedStatement.setString(7,comment.getParentId());
                preparedStatement.setBoolean(8, comment.getIsUpdated());
                preparedStatement.setBoolean(9, comment.getIsDeleted());

                return preparedStatement;
            });
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void updateComment(String commentId, UpdateCommentDto updateCommentDto) {
        String sql = "UPDATE Comment SET content=?, isUpdated=? WHERE commentId=?";
        try {
            jdbcTemplate.update(sql, updateCommentDto.getContent(), true, commentId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
    @Override
    public void flagComment(String commentId) {
        String sql = "UPDATE Comment SET isDeleted=? WHERE commentId=?";
        try {
            jdbcTemplate.update(sql, true, commentId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<CommentReaction> getTaskComments(String taskId, int limit, int offset) {
        String sql = "SELECT * FROM (SELECT  * FROM Comment WHERE entityId=? ORDER BY commentedAt DESC LIMIT ? OFFSET ?) AS C " +
                "LEFT JOIN Reaction R on C.commentId = R.commentId " +
                "LEFT JOIN User AS UR ON UR.userId = R.reactorId " +
                "LEFT JOIN User AS UC ON UC.userId = C.commenter";
        try {
            return jdbcTemplate.query(sql, new CommentReaction(), taskId, limit, offset);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public Comment getCommentById(String commentId) {
        String sql = "SELECT * FROM Comment WHERE commentId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Comment(), commentId);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
    @Override
    public void addCommentReaction(Reaction reaction) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Reaction(reactionId, commentId, reactorId, reactedAt) VALUES(?,?,?,?)");
                preparedStatement.setString(1, reaction.getReactionId());
                preparedStatement.setString(2, reaction.getCommentId());
                preparedStatement.setString(3, reaction.getReactorId());
                preparedStatement.setTimestamp(4, reaction.getReactedAt());
                return preparedStatement;
            });
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
    @Override
    public Reaction getCommentReaction(String userId, String commentId) {
        String sql = "SELECT * FROM Reaction WHERE commentId=? AND reactorId=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Reaction(), commentId, userId);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
    @Override
    public void updateCommentReaction(Reaction reaction) {
        String sql = "UPDATE Reaction SET reactionId=? WHERE reactorId=? AND commentId=?";
        try {
            jdbcTemplate.update(sql, reaction.getReactionId(), reaction.getReactorId(), reaction.getCommentId());
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
    @Override
    public void removeUserCommentReaction(String userId, String commentId) {
        String sql = "DELETE FROM Reaction WHERE reactorId=? AND commentId=?";
        try {
            jdbcTemplate.update(sql, userId, commentId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
