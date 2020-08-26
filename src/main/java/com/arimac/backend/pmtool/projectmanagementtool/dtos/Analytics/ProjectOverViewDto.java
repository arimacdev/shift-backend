package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics;

public class ProjectOverViewDto {
    private AspectSummary totalProjects;
    private AspectSummary leadsPending;
    private AspectSummary leadsOngoing;
    private AspectSummary leadConversion;

    public AspectSummary getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(AspectSummary totalProjects) {
        this.totalProjects = totalProjects;
    }

    public AspectSummary getLeadsPending() {
        return leadsPending;
    }

    public void setLeadsPending(AspectSummary leadsPending) {
        this.leadsPending = leadsPending;
    }

    public AspectSummary getLeadsOngoing() {
        return leadsOngoing;
    }

    public void setLeadsOngoing(AspectSummary leadsOngoing) {
        this.leadsOngoing = leadsOngoing;
    }

    public AspectSummary getLeadConversion() {
        return leadConversion;
    }

    public void setLeadConversion(AspectSummary leadConversion) {
        this.leadConversion = leadConversion;
    }
}
