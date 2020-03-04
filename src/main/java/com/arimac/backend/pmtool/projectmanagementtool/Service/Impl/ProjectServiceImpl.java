package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TransactionRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final UtilsService utilsService;

    public ProjectServiceImpl(TransactionRepository transactionRepository, UtilsService utilsService) {
        this.transactionRepository = transactionRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createProject(ProjectDto projectDto) {
        Project project = new Project();

        project.setProjectId(utilsService.getUUId());
        project.setProjectName(projectDto.getProjectName());
        project.setClientId(projectDto.getClientId());
        project.setProjectStartDate(projectDto.getProjectStartDate());
        project.setProjectEndDate(projectDto.getProjectEndDate());
        project.setProjectStatus(ProjectStatusEnum.presales.toString());
        transactionRepository.createProject(project);

        return new Response(ResponseMessage.SUCCESS, project);
    }
}
