package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Filteration.WorkloadFilteration;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.TaskSprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

import java.util.List;

public interface TaskRepository {
    Task addTaskToProject(Task task); // done
    List<Task> getAllProjectTasksByUser(String projectId);
    List<TaskUserResponseDto> getAllProjectTasksWithProfile(String projectId);
    List<TaskUserResponseDto> getAllParentTasksWithProfile(String projectId);
    List<TaskUserResponseDto> getAllChildTasksWithProfile(String projectId);
    List<Task> getAllUserAssignedTasks(String userId, String projectId);
    List<TaskUserResponseDto> getAllUserAssignedTasksWithProfile(String userId, String projectId);
    Task getProjectTask(String taskId); // done //remove
    Task getTaskByProjectIdTaskId(String projectId, String taskId);
    Task getProjectTaskWithDeleted(String taskId);
    Object updateProjectTask(String taskId,TaskUpdateDto taskUpdateDto);
    void flagProjectTask(String taskId);
    void flagProjectBoundTasks(String projectId);
    List<WorkLoadTaskStatusDto> getAllUsersWithTaskCompletion(List<String> assignees, String from, String to);
    List<WorkLoadProjectDto> getAllUserAssignedTaskWithCompletion(String userId, String from, String to);

    List<WorkloadFilteration> taskFilteration(String baseQuery, String orderQuery);
    // Personal Tasks and Task List
    //List<Task> getAllPersonalTasks(String userId);
    //update sprint
    void updateProjectTaskSprint(String taskId, TaskSprintUpdateDto taskSprintUpdateDto);
    void updateProjectTaskParent(String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    void transitionFromParentToChild(String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    void addParentToParentTask(String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    List<TaskUserResponseDto> getAllChildrenOfParentTaskWithProfile(String taskId);
    List<Task> getAllChildrenOfParentTask(String taskId);
    boolean checkChildTasksOfAParentTask(String taskId);
    List<TaskUserDto> filterTasks(String projectId, FilterTypeEnum filterType, String from, String to, String assignee, String issueType);

    //QUERIES FOR INTERNAL PURPOSES
    void updateProjectAlias(String taskId, String taskAlias);
}
