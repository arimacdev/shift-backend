package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintUpdateDto;

public interface SprintService {
    Object createSprint(SprintDto sprintDto);
    Object getAllProjectSprints(String userId, String projectId);
    Object updateSprint(String userId, String projectId, String sprintId, SprintUpdateDto sprintUpdateDto);
    Object getProjectSprint(String userId, String projectId, String sprintId);

}
