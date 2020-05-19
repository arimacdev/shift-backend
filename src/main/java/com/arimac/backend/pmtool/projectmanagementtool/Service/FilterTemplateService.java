package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Template.TemplateDto;

public interface FilterTemplateService {
    Object createFilterTemplate(TemplateDto templateDto);
    Object getAllUserTemplates(String userId);
}
