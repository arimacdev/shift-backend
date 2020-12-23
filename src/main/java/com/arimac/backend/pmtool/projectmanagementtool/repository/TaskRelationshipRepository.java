package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.TaskRelationship;

import java.util.List;

public interface TaskRelationshipRepository {
    void createTaskRelationship(TaskRelationship taskRelationship);
    List<TaskRelationship> getTaskRelationship(String taskId);
}
