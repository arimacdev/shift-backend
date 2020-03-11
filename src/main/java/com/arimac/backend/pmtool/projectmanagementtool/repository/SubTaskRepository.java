package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;

public interface SubTaskRepository {
    Object addSubTaskToProject(SubTask subTask);
}
