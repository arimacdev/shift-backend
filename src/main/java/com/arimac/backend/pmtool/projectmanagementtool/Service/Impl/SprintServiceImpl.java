package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SprintService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Sprint;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SprintRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SprintServiceImpl implements SprintService {
    private static final Logger logger = LoggerFactory.getLogger(SprintServiceImpl.class);

    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;

    private final UtilsService utilsService;

    public SprintServiceImpl(SprintRepository sprintRepository, ProjectRepository projectRepository, UtilsService utilsService) {
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createSprint(SprintDto sprintDto) {
        ProjectUserResponseDto creator = projectRepository.getProjectByIdAndUserId(sprintDto.getProjectId(), sprintDto.getSprintCreatedBy());
        if (creator == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        if ( !((creator.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (creator.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Admin privileges", HttpStatus.FORBIDDEN);
        Sprint sprint = new Sprint();
        sprint.setSprintId(utilsService.getUUId());
        sprint.setProjectId(sprintDto.getProjectId());
        sprint.setSprintName(sprintDto.getSprintName());
        sprint.setSprintDescription(sprintDto.getSprintDescription());
        sprint.setSprintCreatedBy(sprintDto.getSprintCreatedBy());
//        sprint.setSprintCreatedAt(new Timestamp(utilsService.getCurrentDateTime().getMillis()));
        sprint.setSprintCreatedAt(utilsService.getCurrentTimestamp());
        sprintRepository.createSprint(sprint);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, sprint);
    }

    @Override
    public Object getAllProjectSprints(String userId, String projectId) {
        ProjectUserResponseDto userResponseDto = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (userResponseDto == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        List<Sprint> sprintList = sprintRepository.getAllSprints(projectId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, sprintList);
    }

    @Override
    public Object updateSprint(String userId, String projectId, String sprintId, SprintUpdateDto sprintUpdateDto) {
        if (sprintUpdateDto.getSprintName() == null && sprintUpdateDto.getSprintDescription() == null)
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        ProjectUserResponseDto modifier = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (modifier == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        if ( !((modifier.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (modifier.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Admin privileges", HttpStatus.FORBIDDEN);
        Sprint sprint = sprintRepository.getSprintById(sprintId);
        if (sprint == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        SprintUpdateDto modifiedSprint = new SprintUpdateDto();
        if (sprintUpdateDto.getSprintName() == null || sprintUpdateDto.getSprintName().isEmpty())
            modifiedSprint.setSprintName(sprint.getSprintName());
        else
            modifiedSprint.setSprintName(sprintUpdateDto.getSprintName());
        if (sprintUpdateDto.getSprintDescription() == null || sprintUpdateDto.getSprintDescription().isEmpty())
            modifiedSprint.setSprintDescription(sprint.getSprintDescription());
        else
            modifiedSprint.setSprintDescription(sprintUpdateDto.getSprintDescription());
        sprintRepository.updateSprint(sprintId, modifiedSprint);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, modifiedSprint);
    }

    @Override
    public Object getProjectSprint(String userId, String projectId, String sprintId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        Sprint sprint = sprintRepository.getSprintById(sprintId);
        if (sprint == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, sprint);
    }
}
