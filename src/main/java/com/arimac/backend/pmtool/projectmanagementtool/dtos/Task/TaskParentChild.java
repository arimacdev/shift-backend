package com.arimac.backend.pmtool.projectmanagementtool.dtos.Task;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class TaskParentChild {
    private TaskUserResponseDto parentTask;
    private List<TaskUserResponseDto> childTasks;

    public TaskUserResponseDto getParentTask() {
        return parentTask;
    }

    public void setParentTask(TaskUserResponseDto parentTask) {
        this.parentTask = parentTask;
    }

    public List<TaskUserResponseDto> getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(List<TaskUserResponseDto> childTasks) {
        this.childTasks = childTasks;
    }
}
