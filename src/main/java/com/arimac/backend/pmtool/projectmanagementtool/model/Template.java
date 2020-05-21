package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Template implements RowMapper<Template> {
    private String templateId;
    private String templateName;
    private String templateCreatorId;
    private String templateQuery;

    public Template() {
    }

    public Template(String templateId, String templateName, String templateCreatorId, String templateQuery) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.templateCreatorId = templateCreatorId;
        this.templateQuery = templateQuery;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateCreatorId() {
        return templateCreatorId;
    }

    public void setTemplateCreatorId(String templateCreatorId) {
        this.templateCreatorId = templateCreatorId;
    }

    public String getTemplateQuery() {
        return templateQuery;
    }

    public void setTemplateQuery(String templateQuery) {
        this.templateQuery = templateQuery;
    }

    @Override
    public Template mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Template(
                resultSet.getString("templateId"),
                resultSet.getString("templateName"),
                resultSet.getString("templateCreatorId"),
                resultSet.getString("templateQuery")
        );
    }
}
