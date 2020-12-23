package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject;

import java.util.List;
import java.util.Set;

public class ProjectTicketUsers {
    private List<ServiceTicketUser> serviceTicketUsers;
    private Set<String> internalUsers;

    public ProjectTicketUsers(List<ServiceTicketUser> serviceTicketUsers, Set<String> internalUsers) {
        this.serviceTicketUsers = serviceTicketUsers;
        this.internalUsers = internalUsers;
    }

    public List<ServiceTicketUser> getServiceTicketUsers() {
        return serviceTicketUsers;
    }

    public void setServiceTicketUsers(List<ServiceTicketUser> serviceTicketUsers) {
        this.serviceTicketUsers = serviceTicketUsers;
    }

    public Set<String> getInternalUsers() {
        return internalUsers;
    }

    public void setInternalUsers(Set<String> internalUsers) {
        this.internalUsers = internalUsers;
    }
}
