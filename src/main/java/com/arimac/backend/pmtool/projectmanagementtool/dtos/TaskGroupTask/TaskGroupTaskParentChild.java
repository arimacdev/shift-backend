package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUserResponseDto;

import java.util.List;

public class TaskGroupTaskParentChild {
    private TaskGroupTaskUserResponseDto parentTask;
    private List<TaskGroupTaskUserResponseDto> childTasks;

    public TaskGroupTaskUserResponseDto getParentTask() {
        return parentTask;
    }

    public void setParentTask(TaskGroupTaskUserResponseDto parentTask) {
        this.parentTask = parentTask;
    }

    public List<TaskGroupTaskUserResponseDto> getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(List<TaskGroupTaskUserResponseDto> childTasks) {
        this.childTasks = childTasks;
    }
}
