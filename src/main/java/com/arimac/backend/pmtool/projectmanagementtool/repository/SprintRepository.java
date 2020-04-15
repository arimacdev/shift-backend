package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Sprint;

import java.util.List;

public interface SprintRepository {
    void createSprint(Sprint sprint);
    List<Sprint> getAllSprints(String projectId);
}
