package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskLogService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.TaskSprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.UserTaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.UserTaskGroupResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.*;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final String DEFAULT = "default";

    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskFileRepository taskFileRepository;
    private final TaskLogService taskLogService;
    private final UtilsService utilsService;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final TaskGroupService taskGroupService;
    private final TaskGroupRepository taskGroupRepository;
    private final SprintRepository sprintRepository;

    private final RestTemplate restTemplate;

    public TaskServiceImpl(SubTaskRepository subTaskRepository, TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository, TaskFileRepository taskFileRepository, TaskLogService taskLogService, UtilsService utilsService, NotificationService notificationService, NotificationRepository notificationRepository, TaskGroupService taskGroupService, TaskGroupRepository taskGroupRepository, SprintRepository sprintRepository, RestTemplate restTemplate) {
        this.subTaskRepository = subTaskRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskFileRepository = taskFileRepository;
        this.taskLogService = taskLogService;
        this.utilsService = utilsService;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.taskGroupService = taskGroupService;
        this.taskGroupRepository = taskGroupRepository;
        this.sprintRepository = sprintRepository;
        this.restTemplate = restTemplate;
    }
    //TASK GROUP && PROJECT
    @Override
    public Object addTaskToProject(String projectId, TaskDto taskDto) {
        if ( (taskDto.getTaskName() == null || taskDto.getTaskName().isEmpty()) || (taskDto.getProjectId() == null || taskDto.getProjectId().isEmpty()) || (taskDto.getTaskInitiator()== null || taskDto.getTaskInitiator().isEmpty()) )
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        Task task = new Task();
        if (taskDto.getTaskType().equals(TaskTypeEnum.project)) {
            ProjectUserResponseDto taskInitiator = projectRepository.getProjectByIdAndUserId(projectId, taskDto.getTaskInitiator());
            if (taskInitiator == null)
                return new ErrorMessage(ResponseMessage.ASSIGNER_NOT_MEMBER, HttpStatus.NOT_FOUND);
            ProjectUserResponseDto taskAssignee = null;
            if (taskDto.getTaskAssignee() != null) {
                taskAssignee = projectRepository.getProjectByIdAndUserId(projectId, taskDto.getTaskInitiator());
                if (taskAssignee == null)
                    return new ErrorMessage(ResponseMessage.ASSIGNEE_NOT_MEMBER, HttpStatus.NOT_FOUND);
            }
            if(taskDto.getParentTaskId() != null){
                Task parentTask = taskRepository.getProjectTask(taskDto.getParentTaskId());
                if (parentTask == null)
                    return new ErrorMessage("No Such Parent Task", HttpStatus.NOT_FOUND);
                if (!parentTask.getIsParent())
                    return new ErrorMessage("Task is not a Parent Task", HttpStatus.BAD_REQUEST);
                task.setIsParent(false);
            } else {
                task.setIsParent(true);
            }
            task.setParentId(taskDto.getParentTaskId());
            task.setIssueType(taskDto.getIssueType());
        } else if (taskDto.getTaskType().equals(TaskTypeEnum.taskGroup)){
            TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskDto.getTaskInitiator(), taskDto.getProjectId());
            if (member == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
            if (taskDto.getTaskAssignee() != null)
                if (taskGroupRepository.getTaskGroupMemberByTaskGroup(taskDto.getTaskAssignee(), taskDto.getProjectId()) == null)
                    return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
        }
        task.setTaskId(utilsService.getUUId());
        task.setProjectId(taskDto.getProjectId());
        task.setTaskName(taskDto.getTaskName());
        task.setTaskInitiator(taskDto.getTaskInitiator());
        if (taskDto.getTaskAssignee() == null || taskDto.getTaskAssignee().isEmpty()){
            task.setTaskAssignee(taskDto.getTaskInitiator());
        } else {
            task.setTaskAssignee(taskDto.getTaskAssignee());
        }
        task.setTaskNote(taskDto.getTaskNotes());
        if (taskDto.getTaskStatus() == null){
            if (taskDto.getTaskType().equals(TaskTypeEnum.project))
            task.setTaskStatus(TaskStatusEnum.pending);
            if (taskDto.getTaskType().equals(TaskTypeEnum.taskGroup))
              task.setTaskStatus(TaskStatusEnum.open);
        } else {
            task.setTaskStatus(taskDto.getTaskStatus());
        }
        task.setTaskCreatedAt(utilsService.getCurrentTimestamp());
        if (taskDto.getTaskDueDate() != null){
            task.setTaskDueDateAt(taskDto.getTaskDueDate());
        }
        if (taskDto.getTaskRemindOnDate() != null){
            task.setTaskReminderAt(task.getTaskReminderAt());
        }
        task.setTaskReminderAt(taskDto.getTaskRemindOnDate());
        task.setIsDeleted(false);
        task.setTaskType(taskDto.getTaskType());
        task.setSprintId(DEFAULT);
        taskRepository.addTaskToProject(task);
        if (taskDto.getTaskType().equals(TaskTypeEnum.project) && task.getTaskDueDateAt()!= null) {
            DateTime duedate = new DateTime(task.getTaskDueDateAt().getTime());
            DateTime now = DateTime.now();
            DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
            DateTime dueUtc = new DateTime(duedate, DateTimeZone.forID("UTC"));
            Duration duration = new Duration(nowCol, dueUtc);
            int difference = (int) duration.getStandardMinutes();
            int timeFixDifference = difference - 330;
            Notification notification = new Notification();
            notification.setNotificationId(utilsService.getUUId());
            notification.setTaskId(task.getTaskId());
            notification.setAssigneeId(task.getTaskAssignee());
            notification.setTaskDueDateAt(task.getTaskDueDateAt());
            if (timeFixDifference < 1440) {
                notification.setDaily(true);
            } else {
                notification.setDaily(false);
            }
            notification.setHourly(false);
            notificationRepository.addTaskNotification(notification);
            //Slack Notification
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskAssignNotification(task);
            });
        }
//        taskLogService.addTaskLog(task);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object getAllProjectTasksByUser(String userId, String projectId, TaskTypeEnum type) {
        if (type.equals(TaskTypeEnum.project)) {
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);

        } else if (type.equals(TaskTypeEnum.taskGroup)){
            TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, projectId);
            if (member == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
        }
        List<TaskUserResponseDto> taskList = taskRepository.getAllProjectTasksWithProfile(projectId);
       return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskList);
    }

    @Override
    public Object getAllUserAssignedTasks(String userId, String projectId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        List<TaskUserResponseDto> taskList = taskRepository.getAllUserAssignedTasksWithProfile(userId, projectId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskList);
    }

    @Override
    public Object getProjectTask(String userId, String projectId, String taskId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object getProjectTaskFiles(String userId, String projectId, String taskId, TaskTypeEnum type) {
        if (type.equals(TaskTypeEnum.project)){
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null){
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
            }
        } else if (type.equals(TaskTypeEnum.taskGroup)){
            TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, projectId);
            if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
        }
        Object fileList = taskFileRepository.getAllTaskFiles(taskId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, fileList);
    }

    @Override
    public Object updateProjectTask(String userId, String projectId, String taskId, TaskUpdateDto taskUpdateDto) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if (taskUpdateDto.getTaskType().equals(TaskTypeEnum.project)) {
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
            if (!( (task.getTaskAssignee().equals(userId)) || (task.getTaskInitiator().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) )){
                return new ErrorMessage(ResponseMessage.UNAUTHORIZED_OPERATION, HttpStatus.UNAUTHORIZED);
            }
            if (taskUpdateDto.getTaskAssignee() != null){
                ProjectUserResponseDto assignee = projectRepository.getProjectByIdAndUserId(projectId, taskUpdateDto.getTaskAssignee());
                if (assignee == null){
                    return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
                }
            }
        } else if (taskUpdateDto.getTaskType().equals(TaskTypeEnum.taskGroup)){
            TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, projectId);
            if (member == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
            if (taskUpdateDto.getTaskAssignee() != null){
                TaskGroup_Member assignee = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskUpdateDto.getTaskAssignee(), projectId);
                if (assignee == null){
                    return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
                }
            }
        }
        TaskUpdateDto updateDto = new TaskUpdateDto();

        if (taskUpdateDto.getTaskName() == null || taskUpdateDto.getTaskName().isEmpty()) {
            updateDto.setTaskName(task.getTaskName());
        } else {
            updateDto.setTaskName(taskUpdateDto.getTaskName());
        }
        if (taskUpdateDto.getTaskAssignee() == null || taskUpdateDto.getTaskAssignee().isEmpty()) {
            updateDto.setTaskAssignee(task.getTaskAssignee());
        } else {
            updateDto.setTaskAssignee(taskUpdateDto.getTaskAssignee());
        }
        if (taskUpdateDto.getTaskNotes() == null || taskUpdateDto.getTaskNotes().isEmpty()){
            updateDto.setTaskNotes(task.getTaskNote());
        } else {
            updateDto.setTaskNotes(taskUpdateDto.getTaskNotes());
        }
        if (taskUpdateDto.getTaskStatus() == null || taskUpdateDto.getTaskStatus().isEmpty()) {
            updateDto.setTaskStatus(task.getTaskStatus().toString());
        } else {
            updateDto.setTaskStatus(taskUpdateDto.getTaskStatus());
        }
        if (taskUpdateDto.getTaskDueDate() == null) {
            updateDto.setTaskDueDate(task.getTaskDueDateAt());
        } else {
            updateDto.setTaskDueDate(taskUpdateDto.getTaskDueDate());
        }
        if (taskUpdateDto.getTaskRemindOnDate() == null) {
            updateDto.setTaskRemindOnDate(task.getTaskReminderAt());
        } else {
            updateDto.setTaskRemindOnDate(taskUpdateDto.getTaskRemindOnDate());
        }

        Object updateTask = taskRepository.updateProjectTask(taskId, updateDto);

        if (taskUpdateDto.getTaskType().equals(TaskTypeEnum.project) && taskUpdateDto.getTaskAssignee() != null) {
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskAssigneeUpdateNotification(task, userId, taskUpdateDto.getTaskAssignee());;
            });
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
        }
        if (taskUpdateDto.getTaskType().equals(TaskTypeEnum.project) && taskUpdateDto.getTaskStatus() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskModificationNotification(task, taskUpdateDto, "status", userId);
            });
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
        }
        if (taskUpdateDto.getTaskType().equals(TaskTypeEnum.project) && taskUpdateDto.getTaskName() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskModificationNotification(task, taskUpdateDto, "name", userId);
            });
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
        }
        if (taskUpdateDto.getTaskType().equals(TaskTypeEnum.project) && taskUpdateDto.getTaskNotes() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskModificationNotification(task, taskUpdateDto, "notes", userId);
            });
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
        }
        if (taskUpdateDto.getTaskType().equals(TaskTypeEnum.project) && taskUpdateDto.getTaskDueDate() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskModificationNotification(task, taskUpdateDto, "dueDate", userId);
            });

        }

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
    }

    @Override
    public Object flagProjectTask(String userId, String projectId, String taskId, TaskTypeEnum taskType) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if (taskType.equals(TaskTypeEnum.project)) {
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
            if (!((task.getTaskAssignee().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()))) // check for super admin privileges about delete
                return new ErrorMessage("User doesn't have privileges", HttpStatus.FORBIDDEN);
            notificationRepository.deleteNotification(taskId);
        } else if (taskType.equals(TaskTypeEnum.taskGroup)){
            TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, projectId);
            if (member == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
            if (member.getTaskGroupRole() != TaskGroupRoleEnum.owner.getRoleValue())
                return new ErrorMessage(ResponseMessage.UNAUTHORIZED_OPERATION, HttpStatus.UNAUTHORIZED);
        }
        taskRepository.flagProjectTask(taskId);
        subTaskRepository.flagTaskBoundSubTasks(taskId);
        List<TaskFile>  taskFileList = taskFileRepository.getAllTaskFiles(taskId);
        for (TaskFile taskFile: taskFileList) {
            taskFileRepository.flagTaskFile(taskFile.getTaskFileId());
        }
        return new Response(ResponseMessage.SUCCESS);
    }

    private Map<String, TaskCompletionDto> getTaskCompletionMap(List<Task> taskList){
        Map<String, TaskCompletionDto> userTaskCompletionMap = new HashMap<>();
        String user = null;
        for (int i = 0 ; i < taskList.size(); i++){
            Task task = taskList.get(i);
            user = task.getTaskAssignee();
            TaskCompletionDto taskCompletionDto = new TaskCompletionDto();
            if (userTaskCompletionMap.get(user) != null){
                taskCompletionDto = userTaskCompletionMap.get(user);
                int completed = taskCompletionDto.getCompleted();
                int total = taskCompletionDto.getTotalTasks();
                if (task.getTaskStatus().equals(TaskStatusEnum.closed))
                    completed += 1;
                total += 1;
                taskCompletionDto.setCompleted(completed);
                taskCompletionDto.setTotalTasks(total);
                userTaskCompletionMap.put(user, taskCompletionDto);
            } else {
                if (task.getTaskStatus().equals(TaskStatusEnum.closed)){
                    taskCompletionDto.setCompleted(1);
                } else {
                    taskCompletionDto.setCompleted(0);
                }
                taskCompletionDto.setTotalTasks(1);
                userTaskCompletionMap.put(user, taskCompletionDto);
            }
        }

        return userTaskCompletionMap;
    }


    @Override
    public Object getProjectTaskCompletionUserDetails(String userId, String projectId, TaskTypeEnum type) {
//        List<User> userList = userRepository.getAllProjectUsers(projectId);
        List<Task> taskList = taskRepository.getAllProjectTasksByUser(projectId);
        Map<String, TaskCompletionDto> userTaskCompletionMap = getTaskCompletionMap(taskList);
        if (type.equals(TaskTypeEnum.project)) {
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
            List<UserProjectResponseDto> userTaskStatusList = new ArrayList<>();
            List<UserProjectDto> userProjectDtoList = userRepository.getUsersProjectDetails(projectId);
            for (UserProjectDto projectUserDetails : userProjectDtoList) {
                TaskCompletionDto taskStatus = userTaskCompletionMap.get(projectUserDetails.getAssigneeId());
                UserProjectResponseDto userTaskStatus = new UserProjectResponseDto();
                userTaskStatus.setProjectId(projectId);
                userTaskStatus.setAssigneeId(projectUserDetails.getAssigneeId());
                userTaskStatus.setAssigneeFirstName(projectUserDetails.getAssigneeFirstName());
                userTaskStatus.setAssigneeLastName(projectUserDetails.getAssigneeLastName());
                userTaskStatus.setAssigneeProfileImage(projectUserDetails.getAssigneeProfileImage());
                userTaskStatus.setProjectRoleId(projectUserDetails.getProjectRoleId());
                userTaskStatus.setProjectRoleName(projectUserDetails.getProjectRoleName());
                userTaskStatus.setProjectJobRoleName(projectUserDetails.getProjectJobRoleName());
                if (taskStatus != null) {
                    userTaskStatus.setTasksCompleted(taskStatus.getCompleted());
                    userTaskStatus.setTotalTasks(taskStatus.getTotalTasks());
                } else {
                    userTaskStatus.setTasksCompleted(0);
                    userTaskStatus.setTotalTasks(0);
                }
                userTaskStatusList.add(userTaskStatus);
            }
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userTaskStatusList);
        } else if (type.equals(TaskTypeEnum.taskGroup)){
            TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, projectId);
            if (member == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
            List<UserTaskGroupResponseDto> userTaskStatusList = new ArrayList<>();
            List<UserTaskGroupDto> userTaskGroupDtoList = userRepository.getUsersTaskGroupDetails(projectId);
            for (UserTaskGroupDto taskGroupUser: userTaskGroupDtoList){
                TaskCompletionDto taskStatus = userTaskCompletionMap.get(taskGroupUser.getAssigneeId());
                UserTaskGroupResponseDto userTaskGroupStatus = new UserTaskGroupResponseDto();
                userTaskGroupStatus.setAssigneeId(taskGroupUser.getAssigneeId());
                userTaskGroupStatus.setTaskGroupId(taskGroupUser.getTaskGroupId());
                userTaskGroupStatus.setAssigneeFirstName(taskGroupUser.getAssigneeFirstName());
                userTaskGroupStatus.setAssigneeLastName(taskGroupUser.getAssigneeLastName());
                userTaskGroupStatus.setAssigneeProfileImage(taskGroupUser.getAssigneeProfileImage());
                userTaskGroupStatus.setTaskGroupRole(taskGroupUser.getTaskGroupRole());
                if (taskStatus != null) {
                    userTaskGroupStatus.setTasksCompleted(taskStatus.getCompleted());
                    userTaskGroupStatus.setTotalTasks(taskStatus.getTotalTasks());
                } else {
                    userTaskGroupStatus.setTasksCompleted(0);
                    userTaskGroupStatus.setTotalTasks(0);
                }
                userTaskStatusList.add(userTaskGroupStatus);
            }
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userTaskStatusList);
        } else {
            return new ErrorMessage("Invalid Task Type", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object getProjectTaskCompletion(String userId, String projectId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
//        if (projectUser == null)
//            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        List<Task> taskList = taskRepository.getAllProjectTasksByUser(projectId);
        int dueToday = 0;
        int overDue = 0;
        int left = 0;
        int userAssigned = 0;
        int completed = 0;
        for (Task task : taskList){
            if (task.getTaskDueDateAt() != null && !task.getTaskStatus().equals(TaskStatusEnum.closed)) {
                long now = new Date().getTime();
                long due = task.getTaskDueDateAt().getTime();
                DateTime nowUTC = new DateTime(now, DateTimeZone.forID("UTC"));
                DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
                DateTime dueUTC = new DateTime(due, DateTimeZone.forID("UTC"));
                DateTime newDueUTC = dueUTC.minusMinutes(330);
                if (newDueUTC.isBefore(nowUTC)) {
                    overDue += 1;
                } else if (dueUTC.toLocalDate().equals(nowCol.toLocalDate())) {
                    dueToday += 1;
                }
            }
           if (task.getTaskStatus().equals(TaskStatusEnum.closed)){
               completed +=1;
           } else {
               left += 1;
           }
           if (task.getTaskAssignee().equals(userId)){
               userAssigned += 1;
           }
        }
        ProjectTaskCompletionDto completionResponse = new ProjectTaskCompletionDto();
        completionResponse.setTasksDueToday(dueToday);
        completionResponse.setTasksOverDue(overDue);
        completionResponse.setTasksLeft(left);
        completionResponse.setTasksAssigned(userAssigned);
        completionResponse.setTasksCompleted(completed);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, completionResponse);
    }

    @Override
    public Object getAllUsersWithTaskCompletion(String userId) {
        //TODO Admin validation
        User adminUser = userRepository.getUserByUserId(userId);
        if (adminUser == null){
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
        }
        List<WorkLoadTaskStatusDto> workLoadList = taskRepository.getAllUsersWithTaskCompletion();
        if (workLoadList.isEmpty())
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, workLoadList);
        Map<String, UserWorkLoadDto> workStatusMap = new HashMap<>();
        for (WorkLoadTaskStatusDto workLoadItem: workLoadList){
            UserWorkLoadDto mapItem = workStatusMap.get(workLoadItem.getUserId());
            if (mapItem != null){
                    if (workLoadItem.getTaskStatus().equals("closed")){
                        mapItem.setTotalTasks(mapItem.getTotalTasks() +1);
                        mapItem.setTasksCompleted(mapItem.getTasksCompleted() +1);
                    } else {
                        mapItem.setTotalTasks(mapItem.getTotalTasks() + 1);
                    }
                    workStatusMap.put(workLoadItem.getUserId(), mapItem);
            } else {
                    UserWorkLoadDto userWorkLoad = new UserWorkLoadDto();
                if (workLoadItem.getTaskId() != null) {
                    userWorkLoad.setUserId(workLoadItem.getUserId());
                    userWorkLoad.setFirstName(workLoadItem.getFirstName());
                    userWorkLoad.setLastName(workLoadItem.getLastName());
                    userWorkLoad.setEmail(workLoadItem.getEmail());
                    userWorkLoad.setProfileImage(workLoadItem.getProfileImage());
                    if (workLoadItem.getTaskStatus().equals("closed")) {
                        userWorkLoad.setTasksCompleted(1);
                        userWorkLoad.setTotalTasks(1);
                    } else {
                        userWorkLoad.setTasksCompleted(0);
                        userWorkLoad.setTotalTasks(1);
                    }
                } else  {
                    userWorkLoad.setUserId(workLoadItem.getUserId());
                    userWorkLoad.setFirstName(workLoadItem.getFirstName());
                    userWorkLoad.setLastName(workLoadItem.getLastName());
                    userWorkLoad.setEmail(workLoadItem.getEmail());
                    userWorkLoad.setProfileImage(workLoadItem.getProfileImage());
                    userWorkLoad.setTasksCompleted(0);
                    userWorkLoad.setTotalTasks(0);
                }
                workStatusMap.put(workLoadItem.getUserId(), userWorkLoad);
            }
        }
        List<UserWorkLoadDto> userWorkLoadResponse = new ArrayList<>(workStatusMap.values());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userWorkLoadResponse);
    }

//    @Override
//    public Object getAllUserAssignedTaskWithCompletion(String admin, String userId, String from, String to) {
//        User adminUser = userRepository.getUserByUserId(admin);
//        if (adminUser == null)
//            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
//        User projectUser = userRepository.getUserByUserId(userId);
//        if (projectUser == null)
//            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
//        List<WorkLoadTaskStatusDto> workLoadList = taskRepository.getAllUserAssignedTaskWithCompletion(userId, from, to);
//        Map<String,UserProjectWorkLoadDto> userProjectWorkLoadMap = new HashMap<>();
//        for (WorkLoadTaskStatusDto workLoadTaskItem : workLoadList){
//            UserProjectWorkLoadDto mapItem = userProjectWorkLoadMap.get(workLoadTaskItem.getProjectId());
//            if (mapItem != null){
//                if (workLoadTaskItem.getTaskStatus().equals("closed")) {
//                    mapItem.setCompleted(mapItem.getCompleted() + 1);
//                    mapItem.setTotal(mapItem.getTotal() + 1);
//                } else {
//                    mapItem.setTotal(mapItem.getTotal() + 1);
//                }
//                    ProjectTaskWorkLoadDto projectTaskWorkLoad = new ProjectTaskWorkLoadDto();
//                    projectTaskWorkLoad.setTaskId(workLoadTaskItem.getTaskId());
//                    projectTaskWorkLoad.setTaskName(workLoadTaskItem.getTaskName());
//                    projectTaskWorkLoad.setAssigneeId(workLoadTaskItem.getTaskAssignee());
//                    projectTaskWorkLoad.setTaskStatus(TaskStatusEnum.valueOf(workLoadTaskItem.getTaskStatus()));
//                    projectTaskWorkLoad.setDueDate(workLoadTaskItem.getTaskDueDateAt());
//                    projectTaskWorkLoad.setTaskNotes(workLoadTaskItem.getTaskNote());
//                    List<ProjectTaskWorkLoadDto> taskList = mapItem.getTaskList();
//                    taskList.add(projectTaskWorkLoad);
//                    mapItem.setTaskList(taskList); /** check here */
//                    userProjectWorkLoadMap.put(workLoadTaskItem.getProjectId(), mapItem);
//            } else {
//                UserProjectWorkLoadDto projectWorkLoad = new UserProjectWorkLoadDto();
//                projectWorkLoad.setUserId(workLoadTaskItem.getUserId());
//                projectWorkLoad.setProjectId(workLoadTaskItem.getProjectId());
//                projectWorkLoad.setProjectName(workLoadTaskItem.getProjectName());
//                ProjectTaskWorkLoadDto projectTaskWorkLoad = new ProjectTaskWorkLoadDto();
//                projectTaskWorkLoad.setTaskId(workLoadTaskItem.getTaskId());
//                projectTaskWorkLoad.setTaskName(workLoadTaskItem.getTaskName());
//                projectTaskWorkLoad.setAssigneeId(workLoadTaskItem.getTaskAssignee());
//                projectTaskWorkLoad.setTaskStatus(TaskStatusEnum.valueOf(workLoadTaskItem.getTaskStatus()));
//                projectTaskWorkLoad.setDueDate(workLoadTaskItem.getTaskDueDateAt());
//                projectTaskWorkLoad.setTaskNotes(workLoadTaskItem.getTaskNote());
//                List<ProjectTaskWorkLoadDto> taskList = new ArrayList<>();
//                taskList.add(projectTaskWorkLoad);
//                projectWorkLoad.setTaskList(taskList);
//                if (workLoadTaskItem.getTaskStatus().equals("closed")){
//                    projectWorkLoad.setCompleted(1);
//                    projectWorkLoad.setTotal(1);
//                } else {
//                    projectWorkLoad.setCompleted(0);
//                    projectWorkLoad.setTotal(1);
//                }
//                userProjectWorkLoadMap.put(workLoadTaskItem.getProjectId(), projectWorkLoad);
//            }
//        }
//        List<UserProjectWorkLoadDto> userProjectWorkLoadTaskResponse = new ArrayList<>(userProjectWorkLoadMap.values());
//        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userProjectWorkLoadTaskResponse);
//    }

//    @Override
//    public Object getAllProjectsWithCompletion(String user, String userId) {
//        List<ProjectUserResponseDto> projectList = projectRepository.getAllProjectsByUser(userId);
//        List<UserProjectWorkLoadDto> userProjectWorkLoadTaskResponse = new ArrayList<>();
//        for (ProjectUserResponseDto project : projectList){
//            UserProjectWorkLoadDto projectWorkLoad = new UserProjectWorkLoadDto();
//            projectWorkLoad.setUserId(project.getAssigneeId());
//            projectWorkLoad.setProjectId(project.getProjectId());
//            projectWorkLoad.setProjectName(project.getProjectName());
//            projectWorkLoad.setCompleted(0);
//            projectWorkLoad.setTotal(0);
//            List<ProjectTaskWorkLoadDto> taskList = new ArrayList<>();
//            projectWorkLoad.setTaskList(taskList);
//            userProjectWorkLoadTaskResponse.add(projectWorkLoad);
//        }
//        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userProjectWorkLoadTaskResponse);
//    }


    @Override
    public Object getAllUserAssignedTaskWithCompletion(String admin, String userId, String from, String to) {
        User adminUser = userRepository.getUserByUserId(admin);
        if (adminUser == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
        User projectUser = userRepository.getUserByUserId(userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
        List<WorkLoadProjectDto> workLoadList = taskRepository.getAllUserAssignedTaskWithCompletion(userId, from, to);
        Map<String,UserProjectWorkLoadDto> userProjectWorkLoadMap = new HashMap<>();
        for (WorkLoadProjectDto workLoadTaskItem : workLoadList){
            UserProjectWorkLoadDto mapItem = userProjectWorkLoadMap.get(workLoadTaskItem.getProjectId());
            if (mapItem != null && workLoadTaskItem.getTaskId()!= null){
                if (workLoadTaskItem.getTaskAssignee().equals(userId)) {
                    if (workLoadTaskItem.getTaskStatus().equals("closed")) {
                        mapItem.setCompleted(mapItem.getCompleted() + 1);
                        mapItem.setTotal(mapItem.getTotal() + 1);
                    } else {
                        mapItem.setTotal(mapItem.getTotal() + 1);
                    }
                    ProjectTaskWorkLoadDto projectTaskWorkLoad = new ProjectTaskWorkLoadDto();
                    projectTaskWorkLoad.setTaskId(workLoadTaskItem.getTaskId());
                    projectTaskWorkLoad.setTaskName(workLoadTaskItem.getTaskName());
                    projectTaskWorkLoad.setAssigneeId(workLoadTaskItem.getTaskAssignee());
                    projectTaskWorkLoad.setTaskStatus(TaskStatusEnum.valueOf(workLoadTaskItem.getTaskStatus()));
                    projectTaskWorkLoad.setDueDate(workLoadTaskItem.getTaskDueDateAt());
                    projectTaskWorkLoad.setTaskNotes(workLoadTaskItem.getTaskNote());
                    List<ProjectTaskWorkLoadDto> taskList = mapItem.getTaskList();
                    taskList.add(projectTaskWorkLoad);
                    mapItem.setTaskList(taskList); /** check here */
                    userProjectWorkLoadMap.put(workLoadTaskItem.getProjectId(), mapItem);
                }
            } else {
                UserProjectWorkLoadDto projectWorkLoad = new UserProjectWorkLoadDto();
                projectWorkLoad.setUserId(userId); // set userId
                projectWorkLoad.setProjectId(workLoadTaskItem.getProjectId());
                projectWorkLoad.setProjectName(workLoadTaskItem.getProjectName());
                //Add Tasks if exists
                if (workLoadTaskItem.getTaskId() != null){
                    if (workLoadTaskItem.getTaskAssignee().equals(userId)) {
                        ProjectTaskWorkLoadDto projectTaskWorkLoad = new ProjectTaskWorkLoadDto();
                        projectTaskWorkLoad.setTaskId(workLoadTaskItem.getTaskId());
                        projectTaskWorkLoad.setTaskName(workLoadTaskItem.getTaskName());
                        projectTaskWorkLoad.setAssigneeId(workLoadTaskItem.getTaskAssignee());
                        projectTaskWorkLoad.setTaskStatus(TaskStatusEnum.valueOf(workLoadTaskItem.getTaskStatus()));
                        projectTaskWorkLoad.setDueDate(workLoadTaskItem.getTaskDueDateAt());
                        projectTaskWorkLoad.setTaskNotes(workLoadTaskItem.getTaskNote());
                        List<ProjectTaskWorkLoadDto> taskList = new ArrayList<>();
                        taskList.add(projectTaskWorkLoad);
                        projectWorkLoad.setTaskList(taskList);
                        if (workLoadTaskItem.getTaskStatus().equals("closed")) {
                            projectWorkLoad.setCompleted(1);
                            projectWorkLoad.setTotal(1);
                        } else {
                            projectWorkLoad.setCompleted(0);
                            projectWorkLoad.setTotal(1);
                        }
                    } else {
                        List<ProjectTaskWorkLoadDto> taskList = new ArrayList<>();
                        projectWorkLoad.setTaskList(taskList);
                        projectWorkLoad.setCompleted(0);
                        projectWorkLoad.setTotal(0);
                    }
                } else {
                    List<ProjectTaskWorkLoadDto> taskList = new ArrayList<>();
                    projectWorkLoad.setTaskList(taskList);
                    projectWorkLoad.setCompleted(0);
                    projectWorkLoad.setTotal(0);
                }
                userProjectWorkLoadMap.put(workLoadTaskItem.getProjectId(), projectWorkLoad);
            }
        }
        List<UserProjectWorkLoadDto> userProjectWorkLoadTaskResponse = new ArrayList<>(userProjectWorkLoadMap.values());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userProjectWorkLoadTaskResponse);
    }

    @Override
    public Object updateProjectTaskSprint(String userId, String projectId, String taskId, TaskSprintUpdateDto taskSprintUpdateDto) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if (!task.getTaskType().equals(TaskTypeEnum.project))
            return new ErrorMessage("Cannot Add Sprints to Non-Project Tasks", HttpStatus.BAD_REQUEST);
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (!task.getProjectId().equals(projectId))
            return new ErrorMessage("Task doesnot belong to the project", HttpStatus.BAD_REQUEST);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        if (!( (task.getTaskAssignee().equals(userId)) || (task.getTaskInitiator().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Sufficient privileges", HttpStatus.FORBIDDEN);
        if (!taskSprintUpdateDto.getPreviousSprint().equals(DEFAULT)) {
            Sprint sprint = sprintRepository.getSprintById(taskSprintUpdateDto.getPreviousSprint());
            if (sprint == null)
                return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        }
        if (task.getSprintId().equals(taskSprintUpdateDto.getNewSprint()))
            return new ErrorMessage("New Sprint Cannot be the Previous Sprint", HttpStatus.BAD_REQUEST);
        if (!taskSprintUpdateDto.getNewSprint().equals("default")) {
            Sprint newSprint = sprintRepository.getSprintById(taskSprintUpdateDto.getNewSprint());
            if (newSprint == null)
                return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        }
        taskRepository.updateProjectTaskSprint(taskId, taskSprintUpdateDto);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskSprintUpdateDto);
    }


}



