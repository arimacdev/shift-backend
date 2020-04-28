package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;

import java.util.List;

public interface PersonalTaskRepository {
    void addPersonalTask(PersonalTask personalTask);
    List<PersonalTask> getAllPersonalTasks(String userId);
}
