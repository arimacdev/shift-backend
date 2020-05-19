package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FilterTemplateService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Template.TemplateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Template;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.FilterTemplateRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterTemplateServiceImpl implements FilterTemplateService {

    private final FilterTemplateRepository filterTemplateRepository;
    private final UserRepository userRepository;
    private final UtilsService utilsService;

    public FilterTemplateServiceImpl(FilterTemplateRepository filterTemplateRepository, UserRepository userRepository, UtilsService utilsService) {
        this.filterTemplateRepository = filterTemplateRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createFilterTemplate(TemplateDto templateDto) {
        User user = userRepository.getUserByUserId(templateDto.getTemplateCreatorId());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Template template = new Template();
        template.setTemplateId(utilsService.getUUId());
        template.setTemplateName(templateDto.getTemplateName());
        template.setTemplateCreatorId(templateDto.getTemplateCreatorId());
        template.setTemplateQuery(templateDto.getTemplateQuery());

        filterTemplateRepository.createFilterTemplate(template);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, template);
    }

    @Override
    public Object getAllUserTemplates(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<Template> templates = filterTemplateRepository.getAllUserTemplates(userId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, templates);
    }
}
