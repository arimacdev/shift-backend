package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Files.TaskFileUserProfileDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskFile;

import java.util.List;

public interface TaskFileRepository {
    Object uploadTaskFile(TaskFile taskFile);
    TaskFile getTaskFileById(String fileId);
    TaskFile getTaskFileWithFlag(String fileId);
    List<TaskFile> getAllTaskFiles(String taskId);
    List<TaskFileUserProfileDto> getTaskFilesWithUserProfile(String taskId);
    void flagTaskFile(String taskFileId);
    List<TaskFile> getFolderTaskFiles(String folderId);
    void flagFolderTaskFiles(String folderId);
}
