package com.arimac.backend.pmtool.projectmanagementtool.dtos.Internal;


//INTERNAL USE ONLY

public class UpdateAliasDto {
    private int updatedTasks;
    private int updatedProjects;

    public int getUpdatedTasks() {
        return updatedTasks;
    }

    public void setUpdatedTasks(int updatedTasks) {
        this.updatedTasks = updatedTasks;
    }

    public int getUpdatedProjects() {
        return updatedProjects;
    }

    public void setUpdatedProjects(int updatedProjects) {
        this.updatedProjects = updatedProjects;
    }
}
