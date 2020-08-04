package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Files.TaskFileUserProfileDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Filteration.TaskGroupWorkLoadFilteration;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskCompletionDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.UserTaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.UserTaskGroupResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskParentChild;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.*;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskGroupTaskServiceImpl implements TaskGroupTaskService {

    private final TaskGroupRepository taskGroupRepository;
    private final TaskGroupTaskRepository taskGroupTaskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final UtilsService utilsService;

    public TaskGroupTaskServiceImpl(TaskGroupRepository taskGroupRepository, TaskGroupTaskRepository taskGroupTaskRepository, TaskFileRepository taskFileRepository, UserRepository userRepository, NotificationService notificationService, NotificationRepository notificationRepository, UtilsService utilsService) {
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupTaskRepository = taskGroupTaskRepository;
        this.taskFileRepository = taskFileRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.utilsService = utilsService;
    }


    @Override
    public Object addTaskGroupTask(String taskGroupId, TaskGroupTaskDto taskDto) {
        if ( (taskDto.getTaskName() == null || taskDto.getTaskName().isEmpty()) || (taskDto.getTaskGroupId() == null || taskDto.getTaskGroupId().isEmpty()) || (taskDto.getTaskInitiator()== null || taskDto.getTaskInitiator().isEmpty()) )
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskDto.getTaskInitiator(), taskDto.getTaskGroupId());
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
        if (taskDto.getTaskAssignee() != null)
            if (taskGroupRepository.getTaskGroupMemberByTaskGroup(taskDto.getTaskAssignee(), taskDto.getTaskGroupId()) == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
        TaskGroupTask task = new TaskGroupTask();
        if((taskDto.getParentTaskId() != null) && !(taskDto.getParentTaskId().isEmpty())){
            TaskGroupTask parentTask = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskDto.getParentTaskId());
            if (parentTask == null)
                return new ErrorMessage("No Such Parent Task", HttpStatus.NOT_FOUND);
            if (!parentTask.getIsParent())
                return new ErrorMessage("Task is not a Parent Task", HttpStatus.UNPROCESSABLE_ENTITY);
            task.setIsParent(false);
            task.setParentId(taskDto.getParentTaskId());
        } else {
            task.setIsParent(true);
        }
        task.setTaskId(utilsService.getUUId());
        task.setTaskGroupId(taskDto.getTaskGroupId());
        task.setTaskName(taskDto.getTaskName());
        task.setTaskInitiator(taskDto.getTaskInitiator());
        if (taskDto.getTaskAssignee() == null || taskDto.getTaskAssignee().isEmpty()){
            task.setTaskAssignee(taskDto.getTaskInitiator());
        } else {
            task.setTaskAssignee(taskDto.getTaskAssignee());
        }
        task.setTaskNote(taskDto.getTaskNotes());
        if (taskDto.getTaskStatus() == null){
            task.setTaskStatus(TaskGroupTaskStatusEnum.open);
        } else {
            task.setTaskStatus(taskDto.getTaskStatus());
        }
        task.setTaskCreatedAt(utilsService.getCurrentTimestamp());
        if (taskDto.getTaskDueDate() != null){
            task.setTaskDueDateAt(taskDto.getTaskDueDate());
            setNotification(task, taskDto.getTaskDueDate());
        }
        if (taskDto.getTaskRemindOnDate() != null){
            task.setTaskReminderAt(task.getTaskReminderAt());
        }
        task.setTaskReminderAt(taskDto.getTaskRemindOnDate());
        task.setIsDeleted(false);
        taskGroupTaskRepository.addTaskGroupTask(task);

        CompletableFuture.runAsync(()-> {
            notificationService.sendTaskGroupTaskAssignNotification(task);
        });

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object getTaskGroupTask(String userId, String taskGroupId, String taskId) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object updateTaskGroupTask(String userId, String taskGroupId, String taskId, TaskGroupTaskUpdateDto taskUpdateDto) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (taskUpdateDto.getTaskAssignee() != null){
            TaskGroup_Member assignee = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskUpdateDto.getTaskAssignee(), taskGroupId);
            if (assignee == null){
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
            }
        }
        TaskGroupTaskUpdateDto updateDto = new TaskGroupTaskUpdateDto();

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
        } else if (task.getIsParent() && taskUpdateDto.getTaskStatus() == TaskGroupTaskStatusEnum.closed){
            List<TaskGroupTask> children = taskGroupTaskRepository.getAllChildrenOfParentTask(taskId);
            for(TaskGroupTask child: children){
                if (child.getTaskStatus() != TaskGroupTaskStatusEnum.closed)
                    return new ErrorMessage(ResponseMessage.PARENT_TASK_HAS_PENDING_CHILD_TASKS, HttpStatus.UNPROCESSABLE_ENTITY);
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
        Object updateTask = taskGroupTaskRepository.updateTaskGroupTask(taskId, updateDto);

        //Notifications
        if (taskUpdateDto.getTaskAssignee() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskGroupTaskAssigneeUpdateNotification(task, userId, taskUpdateDto.getTaskAssignee());
            });
        }
        if (taskUpdateDto.getTaskName() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskGroupTaskContentModificationNotification(task, taskUpdateDto, "name", userId);
            });
        }
        if (taskUpdateDto.getTaskNotes() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskGroupTaskContentModificationNotification(task, taskUpdateDto, "notes", userId);
            });
        }
           if (taskUpdateDto.getTaskDueDate() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskGroupTaskContentModificationNotification(task, taskUpdateDto, "dueDate", userId);
                Notification taskNotfication = notificationRepository.getNotificationByTaskId(taskId);
                if (taskNotfication != null) notificationRepository.deleteNotification(taskId);
                    notificationRepository.addTaskNotification(setNotification(task, taskUpdateDto.getTaskDueDate()));
            });
        }

        if (taskUpdateDto.getTaskStatus() != null){
            CompletableFuture.runAsync(()-> {
                notificationService.sendTaskGroupTaskContentModificationNotification(task, taskUpdateDto, "status", userId);;
            });
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
    }

    private Notification setNotification(TaskGroupTask task, Timestamp dueDate){
        DateTime duedate = new DateTime(dueDate);
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
    public Object flagTaskGroupTask(String userId, String taskGroupId, String taskId) {
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
            TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
            if (member == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
            if (member.getTaskGroupRole() != TaskGroupRoleEnum.owner.getRoleValue())
                return new ErrorMessage(ResponseMessage.UNAUTHORIZED_OPERATION, HttpStatus.UNAUTHORIZED);
        taskGroupTaskRepository.flagTaskGroupTask(taskId);
        //subTaskRepository.flagTaskBoundSubTasks(taskId); //TODO Flag Child Tasks
        List<TaskFile> taskFileList = taskFileRepository.getAllTaskFiles(taskId);
        for (TaskFile taskFile: taskFileList) {
            taskFileRepository.flagTaskFile(taskFile.getTaskFileId());
        }
        CompletableFuture.runAsync(()-> {
            notificationService.sendTaskGroupTaskDeleteNotification(task,  userId);
        });
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getAllTaskGroupTasksByUser(String userId, String taskGroupId) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
            List<TaskGroupTaskUserResponseDto> parentTaskList = taskGroupTaskRepository.getAllParentTasksWithProfile(taskGroupId);
            List<TaskGroupTaskUserResponseDto> childTaskList = taskGroupTaskRepository.getAllChildTasksWithProfile(taskGroupId);

            Map<String, TaskGroupTaskParentChild> parentChildMap = new HashMap<>();
            for (TaskGroupTaskUserResponseDto parentTask : parentTaskList){
                if (parentChildMap.get(parentTask.getTaskId()) == null){
                    TaskGroupTaskParentChild taskParentChild = new TaskGroupTaskParentChild();
                    taskParentChild.setParentTask(parentTask);
                    taskParentChild.setChildTasks(new ArrayList<>());
                    parentChildMap.put(parentTask.getTaskId(), taskParentChild);
                }
            }
            for (TaskGroupTaskUserResponseDto childTask: childTaskList){
                if (parentChildMap.get(childTask.getParentId()) != null){
                    TaskGroupTaskParentChild parentChild = parentChildMap.get(childTask.getParentId());
                    List<TaskGroupTaskUserResponseDto> childTasks = parentChild.getChildTasks();
                    childTasks.add(childTask);
                    parentChild.setChildTasks(childTasks);
                }
            }
            List<TaskGroupTaskParentChild> parentChildList = new ArrayList<>(parentChildMap.values());
            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, parentChildList);
    }

    @Override
    public Object getTaskCompletionUserDetails(String userId, String taskGroupId) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
            if (member == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
        List<TaskGroupTask> taskList = taskGroupTaskRepository.getAllTaskGroupTasksByUser(taskGroupId);
        Map<String, TaskCompletionDto> userTaskCompletionMap = getTaskCompletionMap(taskList);
            List<UserTaskGroupResponseDto> userTaskStatusList = new ArrayList<>();
            List<UserTaskGroupDto> userTaskGroupDtoList = userRepository.getUsersTaskGroupDetails(taskGroupId);
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
    }

    @Override
    public Object getAllUserAssignedTasks(String userId, String taskGroupId) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
        List<TaskGroupTaskUserResponseDto> taskList = taskGroupTaskRepository.getAllUserAssignedTasksWithProfile(userId, taskGroupId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskList);
    }

    @Override
    public Object getProjectTaskFiles(String userId, String taskGroupId, String taskId) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
         List<TaskFileUserProfileDto> fileList = taskFileRepository.getTaskFilesWithUserProfile(taskId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, fileList);
    }

    @Override
    public Object transitionFromParentToChild(String userId, String taskGroupId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
       if (!task.getIsParent())
            return new Response(ResponseMessage.CANNOT_TRANSITION_CHILD_TASK, HttpStatus.UNPROCESSABLE_ENTITY);
        if (taskGroupTaskRepository.checkChildTasksOfAParentTask(taskId))
            return new ErrorMessage(ResponseMessage.PARENT_TASK_HAS_CHILDREN, HttpStatus.UNPROCESSABLE_ENTITY);
        TaskGroupTask parentTask = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskParentChildUpdateDto.getNewParent());
        if (parentTask == null)
            return new ErrorMessage(ResponseMessage.PARENT_TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!parentTask.getIsParent())
            return new ErrorMessage("New Parent Task is not a Parent Task", HttpStatus.UNPROCESSABLE_ENTITY);
        taskGroupTaskRepository.transitionFromParentToChild(taskId, taskParentChildUpdateDto);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskParentChildUpdateDto);
    }

    @Override
    public Object getAllChildrenOfParentTask(String userId, String taskGroupId, String taskId) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        if(!task.getIsParent())
            return new ErrorMessage(ResponseMessage.TASK_NOT_PARENT_TASK, HttpStatus.UNPROCESSABLE_ENTITY);
        List<TaskGroupTaskUserResponseDto> children = taskGroupTaskRepository.getAllChildrenOfParentTaskWithProfile(taskId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, children);
    }

    @Override
    public Object taskGroupWorkloadQueryFilter(String userId, String query) {
        String decodedQuery;
        String OrderBySubString = null;
        query = query.replaceAll("%", "%25");
        try {
            decodedQuery = URLDecoder.decode(query, "UTF-8");

            if (decodedQuery.contains("ORDER BY")){
                String[] split = decodedQuery.split("ORDER BY");
                String baseSubstring = split[0];
                OrderBySubString = split[1];

                String[] words = OrderBySubString.split("\\s+");
                List<String> orderBy = new ArrayList<String>(Arrays.asList(words));
                orderBy.removeAll(Arrays.asList(""));
//            for (String word: orderBy){
//                if (!FilterQueryTypeEnum.contains(word) && !FilterOrderEnum.contains(word))
//                    return new ErrorMessage(ResponseMessage.INVALID_FILTER_QUERY, HttpStatus.BAD_REQUEST);
//            }
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
        List<TaskGroupWorkLoadFilteration> taskList = taskGroupTaskRepository.taskGroupTaskFilteration(decodedQuery, OrderBySubString);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskList);
    }

    private Map<String, TaskCompletionDto> getTaskCompletionMap(List<TaskGroupTask> taskList){
        Map<String, TaskCompletionDto> userTaskCompletionMap = new HashMap<>();
        String user = null;
        for (int i = 0 ; i < taskList.size(); i++){
            TaskGroupTask task = taskList.get(i);
            user = task.getTaskAssignee();
            TaskCompletionDto taskCompletionDto = new TaskCompletionDto();
            if (userTaskCompletionMap.get(user) != null){
                taskCompletionDto = userTaskCompletionMap.get(user);
                int completed = taskCompletionDto.getCompleted();
                int total = taskCompletionDto.getTotalTasks();
                if (task.getTaskStatus().equals(TaskGroupTaskStatusEnum.closed))
                    completed += 1;
                total += 1;
                taskCompletionDto.setCompleted(completed);
                taskCompletionDto.setTotalTasks(total);
                userTaskCompletionMap.put(user, taskCompletionDto);
            } else {
                if (task.getTaskStatus().equals(TaskGroupTaskStatusEnum.closed)){
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
}
