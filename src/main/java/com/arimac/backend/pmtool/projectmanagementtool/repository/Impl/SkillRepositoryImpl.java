package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Skill;
import com.arimac.backend.pmtool.projectmanagementtool.model.UserSkill;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SkillRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;

@Service
public class SkillRepositoryImpl implements SkillRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SkillRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void addSkill(Skill skill) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Skill (skillId, categoryId, skillName, skillcreator, skillCreatedAt, isDeleted) VALUES (?,?,?,?,?,?)");
            preparedStatement.setString(1, skill.getSkillId());
            preparedStatement.setString(2, skill.getCategoryId());
            preparedStatement.setString(3, skill.getSkillName());
            preparedStatement.setString(4, skill.getSkillCreator());
            preparedStatement.setTimestamp(5, skill.getSkillCreatedAt());
            preparedStatement.setBoolean(6, false);

            return preparedStatement;
        });
    }

    @Override
    public Skill getSkillByNameAndCategory(String categoryId, String name) {
        String sql = "SELECT * FROM Skill WHERE categoryId=? AND skillName=? AND isDeleted=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Skill(), categoryId, name, false);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Skill> getAllCategorySkills(String categoryId) {
        String sql = "SELECT * FROM Skill WHERE categoryId=? AND isDeleted=?";
        return jdbcTemplate.query(sql, new Skill(), categoryId, false);
    }

    @Override
    public Skill getSkillByIdAndCategory(String categoryId, String skillId) {
        String sql = "SELECT * FROM Skill WHERE categoryId=? AND skillId=? AND isDeleted=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Skill(), categoryId, skillId, false);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void flagSkill(String skillId) {
        String sql = "UPDATE Skill SET isDeleted=? WHERE skillId=?";
        jdbcTemplate.update(sql, true, skillId);
    }

    @Override
    public void updateSkill(SkillDto skillDto, String skillId) {
        String sql = "UPDATE Skill SET skillName=? WHERE skillId=?";
        jdbcTemplate.update(sql, skillDto.getSkillName(), skillId);
    }

    @Override
    public void addSkillToUser(UserSkill userSkill) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO UserSkill (userId, categoryId, skillId) VALUES (?,?,?)");
            preparedStatement.setString(1, userSkill.getUserId());
            preparedStatement.setString(2, userSkill.getCategoryId());
            preparedStatement.setString(3, userSkill.getSkillId());

            return preparedStatement;
        });
    }

    @Override
    public boolean checkIfSkillAdded(String userId, String categoryId, Set<String> skills) {
        String sql = "SELECT EXISTS (SELECT * FROM UserSkill WHERE skillId IN (:ids) AND userId=:userId AND categoryId=:categoryId LIMIT 1)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", skills);
        parameters.addValue("userId", userId);
        parameters.addValue("categoryId", categoryId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, parameters, Boolean.class);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void removeSkillsFromUser(String userId, String categoryId, Set<String> skills) {
        String sql = "DELETE FROM UserSkill WHERE skillId IN (:ids) AND userId=:userId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", skills);
        parameters.addValue("userId", userId);
        try {
            namedParameterJdbcTemplate.update(sql, parameters);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }


}
