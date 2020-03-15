package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;

import java.util.List;

public interface SubTaskRepository {
    Object addSubTaskToProject(SubTask subTask);
    List<SubTask> getAllSubTaksOfATask(String taskId);
    SubTask getSubTaskById(String subTaskId);
    SubTask updateSubTaskById(SubTask subTask);
    void flagSubTaskOfATask(String subTaskId);
}
