package com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder;

import javax.validation.constraints.NotNull;

public class FolderDto {
    @NotNull
    private String folderName;
    private String parentFolder;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    @Override
    public String toString() {
        return "FolderDto{" +
                "folderName='" + folderName + '\'' +
                ", parentFolder='" + parentFolder + '\'' +
                '}';
    }
}
