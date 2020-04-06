package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.FileUploadEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileUploadService {
    Object uploadFileToTask(String userId, String projectId , String taskId, FileUploadEnum fileType, MultipartFile multipartFile) throws IOException;
    Object uploadFileToPersonalTask(String userId, String taskId, FileUploadEnum fileType, MultipartFile multipartFile);
    Object uploadProfilePicture(String userId, FileUploadEnum fileType, MultipartFile multipartFile);
}
