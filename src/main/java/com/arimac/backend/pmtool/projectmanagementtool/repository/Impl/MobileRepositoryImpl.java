package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.Mobile;
import com.arimac.backend.pmtool.projectmanagementtool.repository.MobileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MobileRepositoryImpl implements MobileRepository {
    private final JdbcTemplate jdbcTemplate;

    public MobileRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mobile getMobileStatus(String platform) {
        String sql = "SELECT * FROM Mobile WHERE platform=?";
        return jdbcTemplate.queryForObject(sql, new Mobile(), platform);
    }
}
