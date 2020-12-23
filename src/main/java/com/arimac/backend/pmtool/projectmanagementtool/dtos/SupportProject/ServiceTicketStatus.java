package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject;

public class ServiceTicketStatus {
    private int allRequests;
    private int resolvedRequests;
    private int pendingRequests;
    private int userRequests;

    public ServiceTicketStatus(int allRequests, int resolvedRequests, int pendingRequests, int userRequests) {
        this.allRequests = allRequests;
        this.resolvedRequests = resolvedRequests;
        this.pendingRequests = pendingRequests;
        this.userRequests = userRequests;
    }

    public ServiceTicketStatus() {
    }

    public int getAllRequests() {
        return allRequests;
    }

    public void setAllRequests(int allRequests) {
        this.allRequests = allRequests;
    }

    public int getResolvedRequests() {
        return resolvedRequests;
    }

    public void setResolvedRequests(int resolvedRequests) {
        this.resolvedRequests = resolvedRequests;
    }

    public int getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(int pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public int getUserRequests() {
        return userRequests;
    }

    public void setUserRequests(int userRequests) {
        this.userRequests = userRequests;
    }
}
