package com.arimac.backend.pmtool.projectmanagementtool.dtos.Template;

public class TemplateDto {
    private String templateName;
    private String templateCreatorId;
    private String templateQuery;

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
    public String toString() {
        return "TemplateDto{" +
                "templateName='" + templateName + '\'' +
                ", templateCreatorId='" + templateCreatorId + '\'' +
                ", templateQuery='" + templateQuery + '\'' +
                '}';
    }
}
