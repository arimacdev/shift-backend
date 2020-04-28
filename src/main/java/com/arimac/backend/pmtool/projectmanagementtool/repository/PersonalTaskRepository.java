package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;

public interface PersonalTaskRepository {
    void addPersonalTask(PersonalTask personalTask);
}
