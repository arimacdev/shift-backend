package com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.FileUploadEnum;

import java.sql.Timestamp;

public class SupportTicketFile {
    private String ticketFileId;
    private String ticketId;
    private String ticketFileName;
    private String ticketFileUrl;
    private String fileCreatedBy;
    private int ticketFileSize;
    private Timestamp ticketFileDate;
    private boolean isDeleted;
    private FileUploadEnum fileType;

    public String getTicketFileId() {
        return ticketFileId;
    }

    public void setTicketFileId(String ticketFileId) {
        this.ticketFileId = ticketFileId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketFileName() {
        return ticketFileName;
    }

    public void setTicketFileName(String ticketFileName) {
        this.ticketFileName = ticketFileName;
    }

    public String getTicketFileUrl() {
        return ticketFileUrl;
    }

    public void setTicketFileUrl(String ticketFileUrl) {
        this.ticketFileUrl = ticketFileUrl;
    }

    public String getFileCreatedBy() {
        return fileCreatedBy;
    }

    public void setFileCreatedBy(String fileCreatedBy) {
        this.fileCreatedBy = fileCreatedBy;
    }

    public int getTicketFileSize() {
        return ticketFileSize;
    }

    public void setTicketFileSize(int ticketFileSize) {
        this.ticketFileSize = ticketFileSize;
    }

    public Timestamp getTicketFileDate() {
        return ticketFileDate;
    }

    public void setTicketFileDate(Timestamp ticketFileDate) {
        this.ticketFileDate = ticketFileDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public FileUploadEnum getFileType() {
        return fileType;
    }

    public void setFileType(FileUploadEnum fileType) {
        this.fileType = fileType;
    }
}
