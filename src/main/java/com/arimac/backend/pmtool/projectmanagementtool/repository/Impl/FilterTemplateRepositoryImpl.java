package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Template;
import com.arimac.backend.pmtool.projectmanagementtool.repository.FilterTemplateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class FilterTemplateRepositoryImpl implements FilterTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    public FilterTemplateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFilterTemplate(Template template) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Template(templateId, templateName, templateCreatorId, templateQuery) VALUES(?,?,?,?)");
                preparedStatement.setString(1, template.getTemplateId());
                preparedStatement.setString(2, template.getTemplateName());
                preparedStatement.setString(3, template.getTemplateCreatorId());
                preparedStatement.setString(4, template.getTemplateQuery());

                return preparedStatement;
            });
        } catch (Exception e){
            throw new PMException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public List<Template> getAllUserTemplates(String userId) {
        String sql = "SELECT * FROM Template WHERE templateCreatorId=?";
        try {
            return jdbcTemplate.query(sql, new Template(), userId);
        } catch (Exception e){

            throw new PMException(e.getMessage());
        }
    }
}
