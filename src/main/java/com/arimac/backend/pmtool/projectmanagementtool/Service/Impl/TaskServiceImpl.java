package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Files.TaskFileUserProfileDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Filteration.WorkloadFilteration;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.TaskSprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChild;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.*;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.EntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final String DEFAULT = "default";
    private static final String ALL = "all";
    private static final String NAME = "name";
    private static final String STATUS = "status";
    private static final String NOTES = "notes";
    private static final String DUE_DATE = "dueDate";
    private static final String CLOSED = "closed";
    private static final String ORDER_BY = "ORDER BY";

    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskFileRepository taskFileRepository;
    private final UtilsService utilsService;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final SprintRepository sprintRepository;
    private final ActivityLogService activityLogService;

    public TaskServiceImpl(SubTaskRepository subTaskRepository, TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository, TaskFileRepository taskFileRepository, UtilsService utilsService, NotificationService notificationService, NotificationRepository notificationRepository, TaskGroupRepository taskGroupRepository, SprintRepository sprintRepository, ActivityLogService activityLogService) {
        this.subTaskRepository = subTaskRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskFileRepository = taskFileRepository;
        this.utilsService = utilsService;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.sprintRepository = sprintRepository;
        this.activityLogService = activityLogService;
    }

    //TASK GROUP && PROJECT
    @Override
    public Object addTaskToProject(String projectId, TaskDto taskDto) {
        if ( (taskDto.getTaskName() == null || taskDto.getTaskName().isEmpty()) || (taskDto.getProjectId() == null || taskDto.getProjectId().isEmpty()) || (taskDto.getTaskInitiator()== null || taskDto.getTaskInitiator().isEmpty() || taskDto.getIssueType() == null) )
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
//        if (taskDto.getTaskType().equals(TaskTypeEnum.project)) {
            ProjectUserResponseDto taskInitiator = projectRepository.getProjectByIdAndUserId(projectId, taskDto.getTaskInitiator());
            if (taskInitiator == null)
                return new ErrorMessage(ResponseMessage.ASSIGNER_NOT_MEMBER, HttpStatus.NOT_FOUND);
            ProjectUserResponseDto taskAssignee = null;
            if (taskDto.getTaskAssignee() != null) {
                taskAssignee = projectRepository.getProjectByIdAndUserId(projectId, taskDto.getTaskInitiator());
                if (taskAssignee == null)
                    return new ErrorMessage(ResponseMessage.ASSIGNEE_NOT_MEMBER, HttpStatus.NOT_FOUND);
            }
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        Task task = new Task();
        if((taskDto.getParentTaskId() != null) && !(taskDto.getParentTaskId().isEmpty())){
            Task parentTask = taskRepository.getTaskByProjectIdTaskId(projectId, taskDto.getParentTaskId());
            if (parentTask == null)
                return new ErrorMessage("No Such Parent Task", HttpStatus.NOT_FOUND);
            if (!parentTask.getIsParent())
                return new ErrorMessage("Task is not a Parent Task", HttpStatus.UNPROCESSABLE_ENTITY);
            task.setIsParent(false);
        } else {
            task.setIsParent(true);
        }
        int issueId = project.getIssueCount() + 1;
        String secondaryTaskId = project.getProjectAlias() + "-" + issueId;

        task.setSecondaryTaskId(secondaryTaskId);
        task.setParentId(taskDto.getParentTaskId());
        task.setIssueType(taskDto.getIssueType());
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
//            if (taskDto.getTaskType().equals(TaskTypeEnum.project))
            task.setTaskStatus(TaskStatusEnum.pending);
//            if (taskDto.getTaskType().equals(TaskTypeEnum.taskGroup))
//              task.setTaskStatus(TaskStatusEnum.open);
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
//        task.setTaskType(taskDto.getTaskType());
        if (taskDto.getSprintId() == null || taskDto.getSprintId().isEmpty()){
            task.setSprintId(DEFAULT);
        } else {
            Sprint sprint = sprintRepository.getSprintById(taskDto.getSprintId());
            if (sprint == null || !sprint.getProjectId().equals(projectId))
                task.setSprintId(DEFAULT);
            else
                task.setSprintId(taskDto.getSprintId());
        }
        taskRepository.addTaskToProject(task);
        projectRepository.updateIssueCount(projectId, issueId);
        if (task.getTaskDueDateAt()!= null) {
//            DateTime duedate = new DateTime(task.getTaskDueDateAt().getTime());
//            DateTime now = DateTime.now();
//            DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
//            DateTime dueUtc = new DateTime(duedate, DateTimeZone.forID("UTC"));
//            Duration duration = new Duration(nowCol, dueUtc);
//            int difference = (int) duration.getStandardMinutes();
//            int timeFixDifference = difference - 330;
//            Notification notification = new Notification();
//            notification.setNotificationId(utilsService.getUUId());
//            notification.setTaskId(task.getTaskId());
//            notification.setAssigneeId(task.getTaskAssignee());
//            notification.setTaskDueDateAt(task.getTaskDueDateAt());
//            if (timeFixDifference < 1440) {
//                notification.setDaily(true);
//            } else {
//                notification.setDaily(false);
//            }
//            notification.setHourly(false);
            notificationRepository.addTaskNotification(setNotification(task, task.getTaskDueDateAt()));
        }
        CompletableFuture.runAsync(()-> {
            notificationService.sendTaskAssignNotification(task);
        });
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object getAllProjectTasksByUser(String userId, String projectId) {
//        if (type.equals(TaskTypeEnum.project)) {
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        List<TaskUserResponseDto> parentTaskList = taskRepository.getAllParentTasksWithProfile(projectId);
        List<TaskUserResponseDto> childTaskList = taskRepository.getAllChildTasksWithProfile(projectId);
        Map<String, TaskParentChild> parentChildMap = new HashMap<>();
        for (TaskUserResponseDto parentTask : parentTaskList){
            if (parentChildMap.get(parentTask.getTaskId()) == null){
                TaskParentChild taskParentChild = new TaskParentChild();
                taskParentChild.setParentTask(parentTask);
                taskParentChild.setChildTasks(new ArrayList<>());
                parentChildMap.put(parentTask.getTaskId(), taskParentChild);
            }
        }
        for (TaskUserResponseDto childTask: childTaskList){
            if (parentChildMap.get(childTask.getParentId()) != null){
                TaskParentChild parentChild = parentChildMap.get(childTask.getParentId());
                List<TaskUserResponseDto> childTasks = parentChild.getChildTasks();
                childTasks.add(childTask);
                parentChild.setChildTasks(childTasks);
            }
        }
        List<TaskParentChild> parentChildList = new ArrayList<>(parentChildMap.values());
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, parentChildList);

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
    public Object getProjectTaskFiles(String userId, String projectId, String taskId) {
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null){
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
            }
        List<TaskFileUserProfileDto> fileList = taskFileRepository.getTaskFilesWithUserProfile(taskId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, fileList);
    }

    @Override
    public Object updateProjectTask(String userId, String projectId, String taskId, TaskUpdateDto taskUpdateDto) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
//        if (taskUpdateDto.getTaskType().equals(TaskTypeEnum.project)) {
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
        if (taskUpdateDto.getTaskStatus() == null) {
            updateDto.setTaskStatus(task.getTaskStatus());
        } else if(task.getIsParent() && taskUpdateDto.getTaskStatus().equals(TaskStatusEnum.closed)){
            if(task.getIsParent()){
                List<Task> children = taskRepository.getAllChildrenOfParentTask(taskId);
                for(Task child: children){
                    if (child.getTaskStatus() != TaskStatusEnum.closed)
                        return new ErrorMessage(ResponseMessage.PARENT_TASK_HAS_PENDING_CHILD_TASKS, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            updateDto.setTaskStatus(taskUpdateDto.getTaskStatus());
        } else
            updateDto.setTaskStatus(taskUpdateDto.getTaskStatus());
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
        if (taskUpdateDto.getIssueType()== null){
            updateDto.setIssueType(task.getIssueType());
        } else {
            updateDto.setIssueType(taskUpdateDto.getIssueType());
        }

        Object updateTask = taskRepository.updateProjectTask(taskId, updateDto);

        if (taskUpdateDto.getTaskAssignee() != null) {
//            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskAssigneeUpdateNotification(task, userId, taskUpdateDto.getTaskAssignee());;
//            });
        }
        if (taskUpdateDto.getTaskStatus() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskModificationNotification(task, taskUpdateDto, STATUS, userId);
            });
        }
        if (taskUpdateDto.getTaskName() != null){
            CompletableFuture.runAsync(()-> {
                ActivityLog activityLog = new ActivityLog();
                activityLog.setLogId(utilsService.getUUId());
                activityLog.setEntityType(EntityEnum.TASK);
                activityLog.setEntityId(taskId);
                activityLog.setActionTimestamp(utilsService.getCurrentTimestamp());
                activityLog.setOperation(LogOperationEnum.UPDATE);
                activityLog.setUpdateType("NAME");
                activityLog.setPreviousValue(task.getTaskName());
                activityLog.setUpdatedvalue(taskUpdateDto.getTaskName());
                activityLog.setActor(userId);
                activityLogService.addTaskLog(activityLog);
                notificationService.sendTaskModificationNotification(task, taskUpdateDto, NAME, userId);
            });
        }
        if (taskUpdateDto.getTaskNotes() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskModificationNotification(task, taskUpdateDto, NOTES, userId);
            });
        }
        if (taskUpdateDto.getTaskDueDate() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskModificationNotification(task, taskUpdateDto, DUE_DATE, userId);
            });
            Notification taskNotification = notificationRepository.getNotificationByTaskId(taskId);
            if (taskNotification != null) notificationRepository.deleteNotification(taskId);
//            DateTime duedate = new DateTime(task.getTaskDueDateAt().getTime());
//            DateTime now = DateTime.now();
//            DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
//            DateTime dueUtc = new DateTime(duedate, DateTimeZone.forID("UTC"));
//            Duration duration = new Duration(nowCol, dueUtc);
//            int difference = (int) duration.getStandardMinutes();
//            int timeFixDifference = difference - 330;
//            Notification notification = new Notification();
//            notification.setNotificationId(utilsService.getUUId());
//            notification.setTaskId(task.getTaskId());
//            notification.setAssigneeId(task.getTaskAssignee());
//            notification.setTaskDueDateAt(task.getTaskDueDateAt());
//            if (timeFixDifference < 1440) {
//                notification.setDaily(true);
//            } else {
//                notification.setDaily(false);
//            }
//            notification.setHourly(false);
            notificationRepository.addTaskNotification(setNotification(task, updateDto.getTaskDueDate()));
        }
         return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
    }

    private Notification setNotification(Task task, Timestamp dueDate){
        DateTime duedate = new DateTime(dueDate.getTime());
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

        return notification;
    }

    @Override
    public Object flagProjectTask(String userId, String projectId, String taskId) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
//        if (taskType.equals(TaskTypeEnum.project)) {
            ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
            if (projectUser == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
            if (!((task.getTaskAssignee().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()) || projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue())) // check for super admin privileges about delete
                return new ErrorMessage("User doesn't have privileges", HttpStatus.FORBIDDEN);
            notificationRepository.deleteNotification(taskId);
        taskRepository.flagProjectTask(taskId);
        subTaskRepository.flagTaskBoundSubTasks(taskId);
        List<TaskFile>  taskFileList = taskFileRepository.getAllTaskFiles(taskId);
        for (TaskFile taskFile: taskFileList) {
            taskFileRepository.flagTaskFile(taskFile.getTaskFileId());
        }
        CompletableFuture.runAsync(()-> {
            notificationService.sendTaskDeleteNotification(task,  userId);
        });

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
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
    public Object getProjectTaskCompletionUserDetails(String userId, String projectId) {
//        List<User> userList = userRepository.getAllProjectUsers(projectId);
        List<Task> taskList = taskRepository.getAllProjectTasksByUser(projectId);
        Map<String, TaskCompletionDto> userTaskCompletionMap = getTaskCompletionMap(taskList);
//        if (type.equals(TaskTypeEnum.project)) {
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
                userTaskStatus.setIsUserBlocked(projectUserDetails.getIsUserBlocked());
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
    public Object getAllUsersWithTaskCompletion(String userId, List<String> assignees, String from, String to) {
        //TODO Admin validation
        User adminUser = userRepository.getUserByUserId(userId);
        if (adminUser == null){
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Date fromDate = null;
        Date toDate = null;
        if (!from.equals(ALL) || !to.equals(ALL)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            try {
                fromDate = dateFormat.parse(from);
                toDate = dateFormat.parse(to);
            } catch (ParseException e) {
                return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
            }
            if (fromDate.after(toDate) || toDate.before(fromDate))
                return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
        }
        List<WorkLoadTaskStatusDto> workLoadList = taskRepository.getAllUsersWithTaskCompletion(assignees, from, to);
        if (workLoadList.isEmpty())
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, workLoadList);
        Map<String, UserWorkLoadDto> workStatusMap = new HashMap<>();
        for (WorkLoadTaskStatusDto workLoadItem: workLoadList){
            UserWorkLoadDto mapItem = workStatusMap.get(workLoadItem.getUserId());
            if (mapItem != null){
                    if ( ((!from.equals(ALL) && !to.equals(ALL)) && dateBetweenDue(fromDate, toDate, workLoadItem.getTaskDueDateAt()) ) || (from.equals(ALL) && to.equals(ALL))) {
                        if (workLoadItem.getTaskStatus().equals(CLOSED)) {
                            mapItem.setTotalTasks(mapItem.getTotalTasks() + 1);
                            mapItem.setTasksCompleted(mapItem.getTasksCompleted() + 1);
                        } else {
                            mapItem.setTotalTasks(mapItem.getTotalTasks() + 1);
                        }
                        workStatusMap.put(workLoadItem.getUserId(), mapItem);
                    }
            } else {
                    UserWorkLoadDto userWorkLoad = new UserWorkLoadDto();
                if (workLoadItem.getTaskId() != null) {
                    userWorkLoad.setUserId(workLoadItem.getUserId());
                    userWorkLoad.setFirstName(workLoadItem.getFirstName());
                    userWorkLoad.setLastName(workLoadItem.getLastName());
                    userWorkLoad.setEmail(workLoadItem.getEmail());
                    userWorkLoad.setProfileImage(workLoadItem.getProfileImage());
                    if ( ((!from.equals(ALL) && !to.equals(ALL)) && dateBetweenDue(fromDate, toDate, workLoadItem.getTaskDueDateAt()) ) || (from.equals(ALL) && to.equals(ALL))) {
                        if (workLoadItem.getTaskStatus().equals(CLOSED)) {
                            userWorkLoad.setTasksCompleted(1);
                        } else {
                            userWorkLoad.setTasksCompleted(0);
                        }
                        userWorkLoad.setTotalTasks(1);
                    } else {
                        userWorkLoad.setTotalTasks(0);
                        userWorkLoad.setTasksCompleted(0);
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
        Collections.sort(userWorkLoadResponse, Comparator.comparingInt(UserWorkLoadDto::getTotalTasks));
        Collections.reverse(userWorkLoadResponse);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userWorkLoadResponse);
    }

    private boolean dateBetweenDue(Date from, Date to, Timestamp dueDate){
        if (dueDate == null)
            return false;
        Date due = new Date(dueDate.getTime());
        return !due.before(from) && !due.after(to);

    }

    @Override
    public Object getAllUserAssignedTaskWithCompletion(String admin, String userId, String from, String to) {
        User adminUser = userRepository.getUserByUserId(admin);
        if (adminUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        User projectUser = userRepository.getUserByUserId(userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<WorkLoadProjectDto> workLoadList = taskRepository.getAllUserAssignedTaskWithCompletion(userId, from, to);
        Map<String,UserProjectWorkLoadDto> userProjectWorkLoadMap = new HashMap<>();
        for (WorkLoadProjectDto workLoadTaskItem : workLoadList){
            UserProjectWorkLoadDto mapItem = userProjectWorkLoadMap.get(workLoadTaskItem.getProjectId());
            if (mapItem != null && workLoadTaskItem.getTaskId()!= null){
                if (workLoadTaskItem.getTaskAssignee().equals(userId)) {
                    if (workLoadTaskItem.getTaskStatus().equals(CLOSED)) {
                        mapItem.setCompleted(mapItem.getCompleted() + 1);
                        mapItem.setTotal(mapItem.getTotal() + 1);
                    } else {
                        mapItem.setTotal(mapItem.getTotal() + 1);
                    }
//                    ProjectTaskWorkLoadDto projectTaskWorkLoad = new ProjectTaskWorkLoadDto();
//                    projectTaskWorkLoad.setTaskId(workLoadTaskItem.getTaskId());
//                    projectTaskWorkLoad.setTaskName(workLoadTaskItem.getTaskName());
//                    projectTaskWorkLoad.setAssigneeId(workLoadTaskItem.getTaskAssignee());
//                    projectTaskWorkLoad.setTaskStatus(TaskStatusEnum.valueOf(workLoadTaskItem.getTaskStatus()));
//                    projectTaskWorkLoad.setDueDate(workLoadTaskItem.getTaskDueDateAt());
//                    projectTaskWorkLoad.setTaskNotes(workLoadTaskItem.getTaskNote());
                    List<WorkLoadProjectDto> taskList = mapItem.getTaskList();
                    taskList.add(workLoadTaskItem);
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
//                        ProjectTaskWorkLoadDto projectTaskWorkLoad = new ProjectTaskWorkLoadDto();
//                        projectTaskWorkLoad.setTaskId(workLoadTaskItem.getTaskId());
//                        projectTaskWorkLoad.setTaskName(workLoadTaskItem.getTaskName());
//                        projectTaskWorkLoad.setAssigneeId(workLoadTaskItem.getTaskAssignee());
//                        projectTaskWorkLoad.setTaskStatus(TaskStatusEnum.valueOf(workLoadTaskItem.getTaskStatus()));
//                        projectTaskWorkLoad.setDueDate(workLoadTaskItem.getTaskDueDateAt());
//                        projectTaskWorkLoad.setTaskNotes(workLoadTaskItem.getTaskNote());
                        List<WorkLoadProjectDto> taskList = new ArrayList<>();
                        taskList.add(workLoadTaskItem);
                        projectWorkLoad.setTaskList(taskList);
                        if (workLoadTaskItem.getTaskStatus().equals(CLOSED)) {
                            projectWorkLoad.setCompleted(1);
                            projectWorkLoad.setTotal(1);
                        } else {
                            projectWorkLoad.setCompleted(0);
                            projectWorkLoad.setTotal(1);
                        }
                    } else {
                        List<WorkLoadProjectDto> taskList = new ArrayList<>();
                        projectWorkLoad.setTaskList(taskList);
                        projectWorkLoad.setCompleted(0);
                        projectWorkLoad.setTotal(0);
                    }
                } else {
                    List<WorkLoadProjectDto> taskList = new ArrayList<>();
                    projectWorkLoad.setTaskList(taskList);
                    projectWorkLoad.setCompleted(0);
                    projectWorkLoad.setTotal(0);
                }
                userProjectWorkLoadMap.put(workLoadTaskItem.getProjectId(), projectWorkLoad);
            }
        }
        List<UserProjectWorkLoadDto> userProjectWorkLoadTaskResponse = new ArrayList<>(userProjectWorkLoadMap.values());
        Collections.sort(userProjectWorkLoadTaskResponse, Comparator.comparingInt(UserProjectWorkLoadDto::getTotal));
        Collections.reverse(userProjectWorkLoadTaskResponse);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userProjectWorkLoadTaskResponse);
    }

    @Override
    public Object updateProjectTaskSprint(String userId, String projectId, String taskId, TaskSprintUpdateDto taskSprintUpdateDto) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!task.getIsParent())
            return new ErrorMessage(ResponseMessage.TASK_NOT_PARENT_TASK, HttpStatus.FORBIDDEN);
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (!task.getProjectId().equals(projectId))
            return new ErrorMessage("Task doesn't belong to the project", HttpStatus.FORBIDDEN);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        if (!( (task.getTaskAssignee().equals(userId)) || (task.getTaskInitiator().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Sufficient privileges", HttpStatus.FORBIDDEN);
        if (!taskSprintUpdateDto.getPreviousSprint().equals(DEFAULT)) {
            Sprint sprint = sprintRepository.getSprintById(taskSprintUpdateDto.getPreviousSprint());
            if (sprint == null)
                return new ErrorMessage(ResponseMessage.SPRINT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (task.getSprintId().equals(taskSprintUpdateDto.getNewSprint()))
            return new ErrorMessage("New Sprint Cannot be the Previous Sprint", HttpStatus.UNPROCESSABLE_ENTITY);
        if (!taskSprintUpdateDto.getNewSprint().equals("default")) {
            Sprint newSprint = sprintRepository.getSprintById(taskSprintUpdateDto.getNewSprint());
            if (newSprint == null)
                return new ErrorMessage(ResponseMessage.SPRINT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        taskRepository.updateProjectTaskSprint(taskId, taskSprintUpdateDto);
        List<Task> children = taskRepository.getAllChildrenOfParentTask(taskId);
        for (Task child : children){
            TaskSprintUpdateDto childSprint = new TaskSprintUpdateDto();
            childSprint.setNewSprint(taskSprintUpdateDto.getNewSprint());
            taskRepository.updateProjectTaskSprint(child.getTaskId(), childSprint);
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskSprintUpdateDto);
    }

    @Override
    public Object getAllChildrenOfParentTask(String userId, String projectId, String taskId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        Task task = taskRepository.getTaskByProjectIdTaskId(projectId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        if(!task.getIsParent())
            return new ErrorMessage(ResponseMessage.TASK_NOT_PARENT_TASK, HttpStatus.UNPROCESSABLE_ENTITY);
        List<TaskUserResponseDto> children = taskRepository.getAllChildrenOfParentTaskWithProfile(taskId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, children);
    }

    @Override
    public Object updateProjectTaskParent(String userId, String projectId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        Task task = taskRepository.getTaskByProjectIdTaskId(projectId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!task.getProjectId().equals(projectId))
            return new ErrorMessage("Task doesn't belong to the project", HttpStatus.UNPROCESSABLE_ENTITY);
        if (!((task.getTaskAssignee().equals(userId)) || (task.getTaskInitiator().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Sufficient privileges", HttpStatus.FORBIDDEN);
        if (task.getIsParent())
            return new ErrorMessage(ResponseMessage.TASK_NOT_CHILD_TASK, HttpStatus.UNPROCESSABLE_ENTITY);
        if (!task.getParentId().equals(taskParentChildUpdateDto.getPreviousParent()))
            return new ErrorMessage("Invalid Parent Task", HttpStatus.UNPROCESSABLE_ENTITY);
        Task newParent = taskRepository.getTaskByProjectIdTaskId(projectId, taskParentChildUpdateDto.getNewParent());
        if (newParent == null)
            return new ErrorMessage("New Parent Task Not Found", HttpStatus.NOT_FOUND);
        if (!newParent.getIsParent())
            return new ErrorMessage("New Parent Task is Not a Parent Task", HttpStatus.UNPROCESSABLE_ENTITY);
        taskRepository.updateProjectTaskParent(taskId, taskParentChildUpdateDto);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskParentChildUpdateDto);
    }

    @Override
    public Object transitionFromParentToChild(String userId, String projectId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        Task task = taskRepository.getTaskByProjectIdTaskId(projectId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!((task.getTaskAssignee().equals(userId)) || (task.getTaskInitiator().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Sufficient privileges", HttpStatus.FORBIDDEN);
        if (!task.getIsParent())
            return new Response(ResponseMessage.CANNOT_TRANSITION_CHILD_TASK, HttpStatus.UNPROCESSABLE_ENTITY);
        if (taskRepository.checkChildTasksOfAParentTask(taskId))
            return new ErrorMessage(ResponseMessage.PARENT_TASK_HAS_CHILDREN, HttpStatus.UNPROCESSABLE_ENTITY);
        Task parentTask = taskRepository.getTaskByProjectIdTaskId(projectId, taskParentChildUpdateDto.getNewParent());
        if (parentTask == null)
            return new ErrorMessage(ResponseMessage.PARENT_TASK_NOT_FOUND, HttpStatus.UNPROCESSABLE_ENTITY);
        if (!parentTask.getIsParent())
            return new ErrorMessage("New Parent Task is not a Parent Task", HttpStatus.UNPROCESSABLE_ENTITY);
        taskRepository.transitionFromParentToChild(taskId, taskParentChildUpdateDto);
        TaskSprintUpdateDto taskSprintUpdateDto = new TaskSprintUpdateDto();
        taskSprintUpdateDto.setNewSprint(parentTask.getSprintId());
        taskRepository.updateProjectTaskSprint(taskId, taskSprintUpdateDto);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskParentChildUpdateDto);
    }

    @Override
    @Deprecated
    public Object addParentToParentTask(String userId, String projectId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        Task task = taskRepository.getTaskByProjectIdTaskId(projectId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if (!((task.getTaskAssignee().equals(userId)) || (task.getTaskInitiator().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Sufficient privileges", HttpStatus.UNAUTHORIZED);
        if (!task.getIsParent())
            return new Response(ResponseMessage.TASK_NOT_PARENT_TASK, HttpStatus.UNPROCESSABLE_ENTITY);
        if (taskRepository.checkChildTasksOfAParentTask(taskId))
            return new ErrorMessage(ResponseMessage.PARENT_TASK_HAS_CHILDREN, HttpStatus.UNPROCESSABLE_ENTITY);
        Task parentTask = taskRepository.getTaskByProjectIdTaskId(projectId, taskParentChildUpdateDto.getNewParent());
        if (parentTask == null)
            return new ErrorMessage(ResponseMessage.PARENT_TASK_NOT_FOUND, HttpStatus.UNPROCESSABLE_ENTITY);

        return null;
    }

    @Override
    @Deprecated
    public Object addChildToParentTask(String userId, String projectId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        return null;
    }

    @Override
    public Object filterTasks(String userId, String projectId, FilterTypeEnum filterType, String issueType, String from, String to, String assignee) {
        switch (filterType){
            case dueDate:
                if ((from == null || from.isEmpty()) || (to == null || to.isEmpty()))
                    return new ErrorMessage("Invalid Request Headers", HttpStatus.BAD_REQUEST);
                break;
            case assignee:
                if (assignee == null || assignee.isEmpty())
                    return new ErrorMessage("Invalid Request Headers", HttpStatus.BAD_REQUEST);
                break;
        }
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        List<TaskUserDto> filteredList = taskRepository.filterTasks(projectId, filterType, from, to, assignee, issueType.toString());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, filteredList);
    }

    @Override
    public Object workloadQueryFilter(String userId, String query) {
        String decodedQuery;
        String OrderBySubString = null;
        query = query.replaceAll("%", "%25");
        try {
            decodedQuery = URLDecoder.decode(query, "UTF-8");

            if (decodedQuery.contains(ORDER_BY)){
            String[] split = decodedQuery.split(ORDER_BY);
            String baseSubstring = split[0];
            OrderBySubString = split[1];

            String[] words = OrderBySubString.split("\\s+");
            List<String> orderBy = new ArrayList<String>(Arrays.asList(words));
            orderBy.removeAll(Arrays.asList(""));
            for (String word: orderBy){
                if (!FilterQueryTypeEnum.contains(word) && !FilterOrderEnum.contains(word))
                    return new ErrorMessage(ResponseMessage.INVALID_FILTER_QUERY, HttpStatus.BAD_REQUEST);
            }
            decodedQuery = baseSubstring;
        }
        String[] words = decodedQuery.split("\\s+");
        for (String word : words){
            boolean type = FilterQueryTypeEnum.contains(word);
            boolean operator = FilterQueryOperatorEnum.contains(word);
            boolean argument = word.startsWith("(\"") && word.endsWith("\")") ||  word.startsWith("\"") && word.endsWith("\"") || word.startsWith("(") && word.endsWith(",") || word.startsWith("\"") && word.endsWith(")") || word.startsWith("\"") || word.endsWith("\"") || word.startsWith("(");
            if (!type && !operator && !argument)
                return new ErrorMessage(ResponseMessage.INVALID_FILTER_QUERY, HttpStatus.BAD_REQUEST);
        }
        } catch (UnsupportedEncodingException e){
            throw new PMException(ResponseMessage.URL_DECODING_ERROR, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            throw new PMException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        List<WorkloadFilteration> list = taskRepository.taskFilteration(decodedQuery, OrderBySubString);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, list);
    }

}



