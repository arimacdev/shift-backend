package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalSupportService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SupportProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.AddServiceTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportTaskLink;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportTicketFile;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportUser;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.ServiceTicketStatus;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.ServiceTicketUpdate;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.File.FileTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.Folder.FolderTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SupportProjectServiceImpl implements SupportProjectService {
    private static final String DEFAULT = "default";

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final OrganizationRepository organizationRepository;
    private final SupportMemberRepository supportMemberRepository;
    private final InternalSupportService internalSupportService;
    private final TaskFileRepository taskFileRepository;
    private final TaskRelationshipRepository taskRelationshipRepository;
    private final FolderRepository folderRepository;
    private final UtilsService utilsService;

    public SupportProjectServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository, OrganizationRepository organizationRepository, SupportMemberRepository supportMemberRepository, InternalSupportService internalSupportService, TaskFileRepository taskFileRepository, TaskRelationshipRepository taskRelationshipRepository, FolderRepository folderRepository, UtilsService utilsService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.organizationRepository = organizationRepository;
        this.supportMemberRepository = supportMemberRepository;
        this.internalSupportService = internalSupportService;
        this.taskFileRepository = taskFileRepository;
        this.taskRelationshipRepository = taskRelationshipRepository;
        this.folderRepository = folderRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createAdminForSupportProject(String userId, String project, AddSupportUserDto addSupportUserDto) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        Organization organization = organizationRepository.getOrganizationById(addSupportUserDto.getOrganizationId());
        if (organization == null)
            return new ErrorMessage(ResponseMessage.ORGANIZATION_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!organization.isHasSupportProjects())
            return new ErrorMessage(ResponseMessage.SUPPPORT_SERVICE_NOT_ENABLED, HttpStatus.UNPROCESSABLE_ENTITY);
        internalSupportService.createAdminForSupportProject(project, addSupportUserDto, true);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getSupportUserByEmail(String userId, String email){
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        SupportUser supportUser = internalSupportService.getSupportUserByEmail(email,true);
        return new Response(ResponseMessage.SUCCESS, supportUser );
    }

    @Override
    public Object getSupportProjects(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK,projectRepository.getSupportProjects());
    }

    @Override
    public Object getSupportUsersByOrganization(String userId, String organizationId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, internalSupportService.getSupportUsersByOrganization(organizationId, true));
    }

    @Override
    public Object getSupportUsersByProject(String userId, String projectId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
//        TODO Verify Support Disabling
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportAdded())
            return new ErrorMessage(ResponseMessage.PROJECT_SUPPORT_NOT_ADDED, HttpStatus.UNPROCESSABLE_ENTITY);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, internalSupportService.getSupportUsersByProject(projectId, true));
    }

    @Override
    public Object getSupportTicketStatusByProject(String userId, String projectId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportAdded())
            return new ErrorMessage(ResponseMessage.PROJECT_SUPPORT_NOT_ADDED, HttpStatus.UNPROCESSABLE_ENTITY);
        ServiceTicketStatus ticketStatus = internalSupportService.getSupportTicketStatusByProject(userId, projectId, true);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, ticketStatus);
    }

    @Override
    public Object getSupportTicketById(String userId, String projectId, String ticketId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportAdded())
            return new ErrorMessage(ResponseMessage.PROJECT_SUPPORT_NOT_ADDED, HttpStatus.UNPROCESSABLE_ENTITY);
        Project_SupportMember member  = supportMemberRepository.getSupportMember(userId, projectId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.SUPPORT_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, internalSupportService.getSupportTicketById(projectId, ticketId, true));
    }

    @Override
    public Object getSupportTicketsByProject(String userId, String projectId,  int startIndex, int endIndex) {
        int limit = endIndex - startIndex;
        if (startIndex < 0 || endIndex < 0 || endIndex < startIndex)
            return new ErrorMessage("Invalid Start/End Index", HttpStatus.BAD_REQUEST);
        if (limit > 10)
            return new ErrorMessage(ResponseMessage.REQUEST_ITEM_LIMIT_EXCEEDED, HttpStatus.UNPROCESSABLE_ENTITY);
        Object projectStatus = checkProjectStatus(projectId);
        if (projectStatus instanceof ErrorMessage)
            return projectStatus;
        Object tickets = internalSupportService.getSupportTicketsByProject(projectId, startIndex, limit, true);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, tickets);
    }

    @Override
    public Object supportTicketInternalUpdate(String user, String ticketId, ServiceTicketUpdate serviceTicketUpdate) {
        if (serviceTicketUpdate.getServiceLevel() == null && serviceTicketUpdate.getTicketStatus() == null)
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        Object projectStatus = checkProjectStatus(serviceTicketUpdate.getProjectId());
        if (projectStatus instanceof ErrorMessage)
            return projectStatus;
        Project_SupportMember member  = supportMemberRepository.getSupportMember(user, serviceTicketUpdate.getProjectId());
        if (member == null)
            return new ErrorMessage(ResponseMessage.SUPPORT_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
        internalSupportService.supportTicketInternalUpdate(user, ticketId, serviceTicketUpdate, false);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object createTaskFromServiceTicket(String user, String ticketId, AddServiceTask addServiceTask) {
        //TODO Check User Role
        //Check Service Ticket
        //if ticket belongs to project
        Project_SupportMember member = supportMemberRepository.getSupportMember(user, addServiceTask.getProjectId());
        if (member == null)
            return new ErrorMessage(ResponseMessage.SUPPORT_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project project = projectRepository.getProjectById(addServiceTask.getProjectId());
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportAdded())
            return new ErrorMessage(ResponseMessage.PROJECT_SUPPORT_NOT_ADDED, HttpStatus.UNPROCESSABLE_ENTITY);
        Task task = new Task();
        task.setServiceTicketId(ticketId);
        task.setTaskId(utilsService.getUUId());
        task.setProjectId(addServiceTask.getProjectId());
        if (addServiceTask.getParentTask() != null){
            Task parent = taskRepository.getTaskByProjectIdTaskId(addServiceTask.getProjectId(), addServiceTask.getParentTask());
            if (parent == null)
                return new ErrorMessage(ResponseMessage.PARENT_TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
            if (parent.getTaskStatus() == TaskStatusEnum.closed)
                return new ErrorMessage(ResponseMessage.PARENT_TASK_CLOSED, HttpStatus.UNPROCESSABLE_ENTITY);
            task.setIsParent(false);
            task.setParentId(addServiceTask.getParentTask());
            task.setSprintId(parent.getSprintId());
        } else {
            task.setIsParent(true);
            task.setSprintId(DEFAULT);
        }
        task.setEstimatedWeight(new BigDecimal("0.00"));
        task.setTaskCreatedAt(utilsService.getCurrentTimestamp());
        task.setSecondaryTaskId(project.getProjectAlias() + "-" + project.getIssueCount() + 1);
        task.setTaskName(addServiceTask.getIssueTopic());
        task.setTaskNote(addServiceTask.getIssueDescription());
        task.setTaskAssignee(user);
        task.setTaskInitiator(user);
        task.setIssueType(IssueTypeEnum.support);
        task.setTaskStatus(TaskStatusEnum.open);
        taskRepository.addTaskToProject(task);

        List<SupportTicketFile> ticketFiles = internalSupportService.getFilesOfSupportTicket(addServiceTask.getProjectId(), ticketId, true);
        String taskFolderId;
        if (!ticketFiles.isEmpty()) {
            Folder folder = new Folder();
            folder.setFolderId(utilsService.getUUId());
            folder.setProjectId(addServiceTask.getProjectId());
            folder.setFolderName(task.getSecondaryTaskId() + " - " + task.getTaskName());
            folder.setFolderCreator(user);
            folder.setFolderCreatedAt(utilsService.getCurrentTimestamp());
            folder.setTaskId(task.getTaskId());
            folder.setFolderType(FolderTypeEnum.TASK);
            folderRepository.createFolder(folder);
            taskFolderId = folder.getFolderId();

            for (SupportTicketFile supportTicketFile : ticketFiles) {
                TaskFile taskFile = new TaskFile();
                taskFile.setTaskFileId(utilsService.getUUId());
                taskFile.setTaskId(task.getTaskId());
                taskFile.setTaskFileName(supportTicketFile.getTicketFileName());
                taskFile.setTaskFileUrl(supportTicketFile.getTicketFileUrl());
                taskFile.setTaskFileCreator(supportTicketFile.getFileCreatedBy());
                taskFile.setTaskFileSize(supportTicketFile.getTicketFileSize());
                taskFile.setTaskFileDate(supportTicketFile.getTicketFileDate());
                taskFile.setFileType(FileTypeEnum.TASK);
                taskFile.setTaskFolder(taskFolderId);
                taskFileRepository.uploadTaskFile(taskFile);
            }
        }

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task.getTaskId());
    }

    @Override
    public Object getSupportFilesOfSupportTicket(String user, String ticketId, String projectId) {
        Object projectStatus = checkProjectStatus(projectId);
        if (projectStatus instanceof ErrorMessage)
            return projectStatus;
        Project_SupportMember member  = supportMemberRepository.getSupportMember(user, projectId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.SUPPORT_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, internalSupportService.getFilesOfSupportTicket(projectId,ticketId,  true));
    }

    @Override
    public Object getAssociatedTaskOfTicket(String user, String project, String ticketId) {
        Object projectStatus = checkProjectStatus(project);
        if (projectStatus instanceof ErrorMessage)
            return projectStatus;
        Project_SupportMember member  = supportMemberRepository.getSupportMember(user, project);
        if (member == null)
            return new ErrorMessage(ResponseMessage.SUPPORT_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskRepository.getAssociatedTaskOfTicket(ticketId));
    }

    @Override
    public Object createLinkBetweenSupportTask(String user, String ticketId, TaskRelationship taskRelationship) {
        if (taskRelationship.getFromLink().equals(taskRelationship.getToLink()))
            return new ErrorMessage(ResponseMessage.CANNOT_LINK_TO_SAME_TICKET, HttpStatus.BAD_REQUEST);
        Object projectStatus = checkProjectStatus(taskRelationship.getProjectId());
        if (projectStatus instanceof ErrorMessage)
            return projectStatus;
        if (taskRepository.getTaskByProjectIdTaskId(taskRelationship.getProjectId(),taskRelationship.getFromLink()) == null)
            return new ErrorMessage(ResponseMessage.LINK_FROM_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (taskRepository.getTaskByProjectIdTaskId(taskRelationship.getProjectId(), taskRelationship.getToLink()) == null)
            return new ErrorMessage(ResponseMessage.LINK_TO_NOT_FOUND, HttpStatus.NOT_FOUND);
        taskRelationshipRepository.createTaskRelationship(taskRelationship);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    private Object checkProjectStatus(String projectId){
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportAdded())
            return new ErrorMessage(ResponseMessage.PROJECT_SUPPORT_NOT_ADDED, HttpStatus.UNPROCESSABLE_ENTITY);
        return true;
    }

}
