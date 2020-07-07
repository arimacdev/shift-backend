package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.TaskSprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;

import java.util.List;

public interface TaskService {
    Object addTaskToProject(String projectId, TaskDto taskDto);
    Object getAllProjectTasksByUser(String userId, String projectId, int startIndex, int endIndex);
    Object getAllUserAssignedTasks(String userId, String projectId);
    Object getProjectTask(String userId, String projectId, String taskId);
    Object getProjectTaskFiles(String userId, String projectId, String taskId);
    Object updateProjectTask(String userId, String projectId, String taskId, TaskUpdateDto taskUpdateDto);
    Object flagProjectTask(String userId, String projectId, String taskId);
//    Object getProjectTaskCompletionByUser(String userId, String projectId);
    Object getProjectTaskCompletionUserDetails(String userId, String projectId); // people tab
    Object getProjectTaskCompletion(String userId, String projectId); //project tab
    Object getAllUsersWithTaskCompletion(String userId, List<String> assignees, String from, String to);
    Object getAllUserAssignedTaskWithCompletion(String user, String userId, String from, String to);
    Object updateProjectTaskSprint(String userId, String projectId, String taskId, TaskSprintUpdateDto taskSprintUpdateDto);
    Object updateProjectTaskParent(String userId, String projectId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    Object getAllChildrenOfParentTask(String userId, String projectId, String taskId);
    Object transitionFromParentToChild(String userId, String projectId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    Object addParentToParentTask(String userId, String projectId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    Object addChildToParentTask(String userId, String projectId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    Object filterTasks(String userId, String projectId, FilterTypeEnum filterType, String issueType, String from, String to, String assignee);
    Object workloadQueryFilter(String userId, String query);

}
