package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintDto;

public interface SprintService {
    Object createSprint(SprintDto sprintDto);
    Object getAllProjectSprints(String userId, String projectId);
}
