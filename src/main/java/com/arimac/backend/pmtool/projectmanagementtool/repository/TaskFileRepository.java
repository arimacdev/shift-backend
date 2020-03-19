package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.TaskFile;

public interface TaskFileRepository {
    Object uploadTaskFile(TaskFile taskFile);
    Object getAllTaskFiles(String taskId);
}
