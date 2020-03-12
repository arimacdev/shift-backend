package com.arimac.backend.pmtool.projectmanagementtool.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    Object uploadFileToTask(String userId, String projectId , String taskId, MultipartFile multipartFile);
}
