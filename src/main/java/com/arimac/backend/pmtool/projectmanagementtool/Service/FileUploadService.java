package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.FileUploadEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileUploadService {
    Object uploadFileToTask(String userId, String projectId , String taskId, FileUploadEnum fileType, MultipartFile multipartFile);
    Object uploadFileToTaskGroupTask(String userId, String taskgroupId , String taskId,FileUploadEnum fileType, MultipartFile multipartFile);
    Object uploadFileToPersonalTask(String userId, String taskId, FileUploadEnum fileType, MultipartFile multipartFile);
    Object uploadProfilePicture(String userId, FileUploadEnum fileType, MultipartFile multipartFile);
    Object deleteFileFromTask(String userId, String projectId, String taskId, String taskFile);
    Object deleteFileFromTaskGroupTask(String userId, String taskgroupId, String taskId, String taskFile);
    Object uploadProjectFiles(String userId, String projectId, FileUploadEnum fileType, MultipartFile[] multipartFiles, String folderId);
    Object uploadCommentFile(String userId, String taskId, FileUploadEnum fileType, MultipartFile multipartFile);
    Object getAllProjectFiles(String userId, String projectId);
    Object flagProjectFile(String userId, String projectId, String projectFileId);
}
