package com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder;

import javax.validation.constraints.NotNull;

public class MoveFolderDto {
    @NotNull
    private String fileId;
//    @NotNull
    private String previousParentFolder;
//    @NotNull
    private String newParentFolder;

    public String getPreviousParentFolder() {
        return previousParentFolder;
    }

    public void setPreviousParentFolder(String previousParentFolder) {
        this.previousParentFolder = previousParentFolder;
    }

    public String getNewParentFolder() {
        return newParentFolder;
    }

    public void setNewParentFolder(String newParentFolder) {
        this.newParentFolder = newParentFolder;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "MoveFolderDto{" +
                "fileId='" + fileId + '\'' +
                ", previousParentFolder='" + previousParentFolder + '\'' +
                ", newParentFolder='" + newParentFolder + '\'' +
                '}';
    }
}
