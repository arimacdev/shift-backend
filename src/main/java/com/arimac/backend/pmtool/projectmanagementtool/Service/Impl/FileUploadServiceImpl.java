package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FileUploadService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectFileResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.ProjectUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.TaskUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FileUploadEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.Folder.FolderTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final ActivityLogService activityLogService;
    private final AmazonS3 amazonS3Client;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UtilsService utilsService;
    private final UserRepository userRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final NotificationService notificationService;
    private final ProjectFileRepository projectFileRepository;
    private final PersonalTaskRepository personalTaskRepository;
    private final TaskGroupTaskRepository taskGroupTaskRepository;
    private final FolderRepository folderRepository;
    private final CommentRepository commentRepository;

    public FileUploadServiceImpl(ActivityLogService activityLogService, AmazonS3 amazonS3Client, ProjectRepository projectRepository, TaskRepository taskRepository, TaskFileRepository taskFileRepository, UtilsService utilsService, UserRepository userRepository, TaskGroupRepository taskGroupRepository, NotificationService notificationService, ProjectFileRepository projectFileRepository, PersonalTaskRepository personalTaskRepository, TaskGroupTaskRepository taskGroupTaskRepository, FolderRepository folderRepository, CommentRepository commentRepository) {
        this.activityLogService = activityLogService;
        this.amazonS3Client = amazonS3Client;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskFileRepository = taskFileRepository;
        this.utilsService = utilsService;
        this.userRepository = userRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.notificationService = notificationService;
        this.projectFileRepository = projectFileRepository;
        this.personalTaskRepository = personalTaskRepository;
        this.taskGroupTaskRepository = taskGroupTaskRepository;
        this.folderRepository = folderRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Object uploadFileToTask(String userId, String projectId, String taskId, FileUploadEnum fileType, MultipartFile multipartFiles) {
        if (checkFileSize(multipartFiles))
            return new ErrorMessage(ResponseMessage.FILE_SIZE_TOO_LARGE, HttpStatus.UNPROCESSABLE_ENTITY);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null)
                return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
            if (!( (task.getTaskAssignee().equals(userId)) || (task.getTaskInitiator().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) ))
                return new ErrorMessage(ResponseMessage.UNAUTHORIZED_OPERATION, HttpStatus.UNAUTHORIZED);
            Folder taskFolder = folderRepository.getFolderByTaskId(taskId);
            String taskFolderId;
            if (taskFolder == null){
                Folder folder = new Folder();
                folder.setFolderId(utilsService.getUUId());
                folder.setProjectId(projectId);
                folder.setFolderName(task.getSecondaryTaskId() + " - " + task.getTaskName());
                folder.setFolderCreator(userId);
                folder.setFolderCreatedAt(utilsService.getCurrentTimestamp());
                folder.setTaskId(taskId);
                folder.setFolderType(FolderTypeEnum.TASK);
                folderRepository.createFolder(folder);
                taskFolderId = folder.getFolderId();
            } else {
                taskFolderId = taskFolder.getFolderId();
            }
            List<String> fileUrlList = new ArrayList<>();
//            for (MultipartFile currentMultipartFile : multipartFiles){
                String taskUrl = fileQueue(multipartFiles, fileType);
                fileUrlList.add(taskUrl);
                TaskFile taskFile = new TaskFile();
                taskFile.setTaskFileId(utilsService.getUUId());
                taskFile.setTaskId(taskId);
                taskFile.setTaskFileName(multipartFiles.getOriginalFilename());
                taskFile.setTaskFileSize((int) multipartFiles.getSize());
                taskFile.setTaskFileUrl(taskUrl);
                taskFile.setTaskFileCreator(userId);
                taskFile.setTaskFileDate(utilsService.getCurrentTimestamp());
                taskFile.setTaskFolder(taskFolderId);
                taskFileRepository.uploadTaskFile(taskFile);
//            }
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskFileUploadNotification(userId, taskId, taskUrl, multipartFiles.getOriginalFilename());
                activityLogService.addTaskLog(utilsService.addTaskUpdateLog(LogOperationEnum.UPDATE, userId, taskId, TaskUpdateTypeEnum.FILE, null, taskFile.getTaskFileId()));
            });

            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskFile);
        }

    @Override
    public Object uploadFileToTaskGroupTask(String userId, String taskgroupId, String taskId, FileUploadEnum fileType, MultipartFile multipartFile) {
        if (checkFileSize(multipartFile))
            return new ErrorMessage(ResponseMessage.FILE_SIZE_TOO_LARGE, HttpStatus.UNPROCESSABLE_ENTITY);
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskgroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskgroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        String taskUrl = fileQueue(multipartFile, fileType);
        List<String> fileUrlList = new ArrayList<>();
        fileUrlList.add(taskUrl);
        TaskFile taskFile = new TaskFile();
        taskFile.setTaskFileId(utilsService.getUUId());
        taskFile.setTaskId(taskId);
        taskFile.setTaskFileName(multipartFile.getOriginalFilename());
        taskFile.setTaskFileUrl(taskUrl);
        taskFile.setTaskFileCreator(userId);
        taskFile.setTaskFileSize((int) multipartFile.getSize());
        taskFile.setTaskFileDate(utilsService.getCurrentTimestamp());
        taskFileRepository.uploadTaskFile(taskFile);

        CompletableFuture.runAsync(()-> {
            notificationService.sendTaskGroupTaskFileUploadNotification(userId, task, taskgroupId, taskUrl, multipartFile.getOriginalFilename());
        });

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskFile);
    }

    @Override
    public Object uploadFileToPersonalTask(String userId, String taskId, FileUploadEnum fileType, MultipartFile multipartFiles) {
        if (checkFileSize(multipartFiles))
            return new ErrorMessage(ResponseMessage.FILE_SIZE_TOO_LARGE, HttpStatus.UNPROCESSABLE_ENTITY);
        PersonalTask task = personalTaskRepository.getPersonalTaskByUserId(userId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<String> fileUrlList = new ArrayList<>();
        String taskUrl = fileQueue(multipartFiles, fileType);
        fileUrlList.add(taskUrl);
        TaskFile taskFile = new TaskFile();
        taskFile.setTaskFileId(utilsService.getUUId());
        taskFile.setTaskId(taskId);
        taskFile.setTaskFileName(multipartFiles.getOriginalFilename());
        taskFile.setTaskFileUrl(taskUrl);
        taskFile.setTaskFileCreator(userId);
        taskFile.setTaskFileSize((int) multipartFiles.getSize());
        taskFile.setTaskFileDate(utilsService.getCurrentTimestamp());
        taskFileRepository.uploadTaskFile(taskFile);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskFile);
    }

    @Override
    public Object uploadProfilePicture(String userId, FileUploadEnum fileType, MultipartFile multipartFile) {
        if (checkFileSize(multipartFile))
            return new ErrorMessage(ResponseMessage.FILE_SIZE_TOO_LARGE, HttpStatus.UNPROCESSABLE_ENTITY);
        String url = fileQueue(multipartFile, fileType);
        userRepository.updateProfilePicture(userId, url);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, url);
    }

    @Override
    public Object deleteFileFromTask(String userId, String projectId, String taskId, String taskFile) {
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null) {
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
            }
            Task task = taskRepository.getProjectTask(taskId);
            if (task == null)
                return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
            if (!((task.getTaskAssignee().equals(userId)) || (task.getTaskInitiator().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue())))
                return new ErrorMessage(ResponseMessage.UNAUTHORIZED_OPERATION, HttpStatus.UNAUTHORIZED);
        taskFileRepository.flagTaskFile(taskFile);
        activityLogService.addTaskLog(utilsService.addTaskUpdateLog(LogOperationEnum.UPDATE, userId, taskId, TaskUpdateTypeEnum.FILE, taskFile, null));
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object deleteFileFromTaskGroupTask(String userId, String taskgroupId, String taskId, String taskFile) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskgroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskgroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        taskFileRepository.flagTaskFile(taskFile);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object uploadProjectFiles(String userId, String projectId, FileUploadEnum fileType, MultipartFile[] multipartFiles, String folderId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        List<ProjectFile> projectFiles = new ArrayList<>();
        if (!folderId.equals("default")){
            Folder folder = folderRepository.getFolderById(folderId);
            if (folder == null)
                return new ErrorMessage(ResponseMessage.PARENT_FOLDER_NOT_EXISTS, HttpStatus.NOT_FOUND);
        }
        for (MultipartFile multipartFile : multipartFiles) {
            if (checkFileSize(multipartFile))
                return new ErrorMessage(ResponseMessage.FILE_SIZE_TOO_LARGE, HttpStatus.UNPROCESSABLE_ENTITY);
            String projectFileUrl = fileQueue(multipartFile, fileType);
            logger.info("url {}",  multipartFile.getSize());
            ProjectFile projectFile = new ProjectFile();
            projectFile.setProjectFileId(utilsService.getUUId());
            projectFile.setProjectId(projectId);
            projectFile.setProjectFileName(multipartFile.getOriginalFilename());
            projectFile.setProjectFileAddedBy(userId);
            projectFile.setProjectFileUrl(projectFileUrl);
            projectFile.setProjectFileSize((int) multipartFile.getSize());
            projectFile.setProjectFileAddedOn(utilsService.getCurrentTimestamp());
            projectFile.setIsDeleted(false);
            if (!folderId.equals("default"))
            projectFile.setProjectFolder(folderId);
            projectFiles.add(projectFile);
            projectFileRepository.uploadProjectFile(projectFile);
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, userId, projectId, ProjectUpdateTypeEnum.FILE, null, projectFile.getProjectFileId()));
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, projectFiles);
    }

    @Override
    public Object uploadCommentFile(String userId, String taskId, FileUploadEnum fileType, MultipartFile multipartFile) {
        if (checkFileSize(multipartFile))
            return new ErrorMessage(ResponseMessage.FILE_SIZE_TOO_LARGE, HttpStatus.UNPROCESSABLE_ENTITY);
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(task.getProjectId(), userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        String url = fileQueue(multipartFile, fileType);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, url);
    }

    @Override
    public Object getAllProjectFiles(String userId, String projectId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        List<ProjectFileResponseDto> projectFiles = projectFileRepository.getAllProjectFiles(projectId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, projectFiles);
    }

    @Override
    public Object flagProjectFile(String userId, String projectId, String projectFileId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        ProjectFile projectFile = projectFileRepository.getProjectFile(projectFileId);
        if (projectFile == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if (!((projectFile.getProjectFileAddedBy().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue())))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED_OPERATION, HttpStatus.UNAUTHORIZED);
        projectFileRepository.flagProjectFile(projectFileId);
        activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, userId, projectId, ProjectUpdateTypeEnum.FILE, projectFile.getProjectFileId(), null));
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    private boolean checkFileSize(MultipartFile multipartFile){
        double fileSizeInMb;
        try {
             fileSizeInMb = (double) multipartFile.getSize() / 1000000;
        } catch (Exception e){
            return true;
        }
        if (fileSizeInMb > Double.parseDouble(ENVConfig.MAX_FILE_SIZE))
            return true;
         else
            return false;
    }

    private String fileQueue(MultipartFile multipartFile, FileUploadEnum fileType){
            try {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile, fileType);
                return uploadFile(file, fileName);
            } catch (Exception e){
                throw new PMException("File upload failed", HttpStatus.BAD_REQUEST);
            }
        }

        private String generateFileName(MultipartFile multipartFile, FileUploadEnum fileType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fileType.toString());
        
        stringBuilder.append("_");
        stringBuilder.append(new Date().getTime());
        stringBuilder.append("_");
        stringBuilder.append(multipartFile.getOriginalFilename());

        return  stringBuilder.toString();
        }

        private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
            File convertFile = new File(multipartFile.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convertFile);
            fos.write(multipartFile.getBytes());
            fos.close();
            return convertFile;
        }

        private String uploadFile(File file, String filePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(ENVConfig.AWS_BUCKET_NAME, filePath, file);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        putObjectRequest.withMetadata(metadata);
        amazonS3Client.putObject(putObjectRequest);
        String fileUploadedUrl = String.format("https://%s.s3-%s.amazonaws.com/%s", ENVConfig.AWS_BUCKET_NAME, ENVConfig.AWS_REGION, filePath);
        file.delete();
        logger.info("File uploaded to S3 successfully {}", fileUploadedUrl);
        return fileUploadedUrl;

        }
}
