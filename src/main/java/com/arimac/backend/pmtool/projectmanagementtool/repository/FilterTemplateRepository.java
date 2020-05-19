package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Template;

import java.util.List;

public interface FilterTemplateRepository {
    void createFilterTemplate(Template template);
    List<Template> getAllUserTemplates(String userId);
}
