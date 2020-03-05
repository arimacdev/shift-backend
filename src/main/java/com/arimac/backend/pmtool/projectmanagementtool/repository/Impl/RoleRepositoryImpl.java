package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectRole;
import com.arimac.backend.pmtool.projectmanagementtool.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoleRepositoryImpl implements RoleRepository {

    private static final Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);


    private final JdbcTemplate jdbcTemplate;

    public RoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProjectRole getRolebyId(int projectRoleId) {
        String sql = "SELECT * from ProjectRole where projectRoleId=?";
        return jdbcTemplate.queryForObject(sql, new ProjectRole(), projectRoleId);
    }

}
