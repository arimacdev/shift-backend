package com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder;

import com.arimac.backend.pmtool.projectmanagementtool.model.Folder;
import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectFile;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskFile;

import java.util.List;

public class FileSearchResult {
    private List<Folder> folders;
    private List<TaskFile> taskFiles;
    private List<ProjectFile> projectFiles;

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<TaskFile> getTaskFiles() {
        return taskFiles;
    }

    public void setTaskFiles(List<TaskFile> taskFiles) {
        this.taskFiles = taskFiles;
    }

    public List<ProjectFile> getProjectFiles() {
        return projectFiles;
    }

    public void setProjectFiles(List<ProjectFile> projectFiles) {
        this.projectFiles = projectFiles;
    }
}
