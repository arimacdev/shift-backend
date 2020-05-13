package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTaskUpdateDto;

import java.util.List;

public interface PersonalTaskRepository {
    void addPersonalTask(PersonalTask personalTask);
    List<PersonalTask> getAllPersonalTasks(String userId);
    PersonalTask getPersonalTaskByUserId(String userId, String taskId);
    void updatePersonalTask(String taskId, PersonalTaskUpdateDto personalTaskUpdateDto);
    void flagPersonalTask(String taskId);

    //INTERNAL ONLY
    List<PersonalTask> getAllPersonalTasksOfAllUsers();
}
