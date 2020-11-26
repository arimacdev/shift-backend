package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportMember.SupportMemberDetails;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_SupportMember;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SupportMemberRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Service
public class SupportMemberRepositoryImpl implements SupportMemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public SupportMemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Project_SupportMember getSupportMember(String memberId, String projectId) {
        String sql = "SELECT * FROM Project_SupportMember WHERE projectId=? AND assigneeId=? AND isEnabled=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Project_SupportMember(), projectId, memberId);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
           throw new PMException(e.getMessage());
        }
    }

    @Override
    public void addSupportMember(Project_SupportMember project_supportMember) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Project_SupportMember(projectId, assigneeId, assignedBy, assignedAt, isEnabled) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, project_supportMember.getProjectId());
            preparedStatement.setString(2, project_supportMember.getAssigneeId());
            preparedStatement.setString(3, project_supportMember.getAssignedBy());
            preparedStatement.setTimestamp(4, project_supportMember.getAssignedAt());
            preparedStatement.setBoolean(5, project_supportMember.getIsEnabled());
            return preparedStatement;
        });
    }

    @Override
    public void changeStatusOfSupportMember(String memberId, String projectId, boolean status) {
        String sql = "UPDATE Project_SupportMember SET isEnabled=? WHERE assigneeId=? AND projectId=?";
        try {
            jdbcTemplate.update(sql, status, memberId, projectId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<SupportMemberDetails> getSupportMemberByProject(String projectId) {
        String sql = "SELECT projectId,assigneeId,assignedAt,assignedBy,isEnabled,firstName,lastName,profileImage FROM Project_SupportMember INNER JOIN User ON userId = assigneeId WHERE projectId=? AND isEnabled=true AND isActive=true";
        try {
            return jdbcTemplate.query(sql, new SupportMemberDetails(), projectId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
