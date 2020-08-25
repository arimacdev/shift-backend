package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics;

public class ProjectOverViewDto<T> {
    private AspectSummary<T> totalProjects;
    private AspectSummary<T> leadsPending;
    private AspectSummary<T> leadsOngoing;
    private AspectSummary<T> leadConversion;

    public AspectSummary<T> getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(AspectSummary<T> totalProjects) {
        this.totalProjects = totalProjects;
    }

    public AspectSummary<T> getLeadsPending() {
        return leadsPending;
    }

    public void setLeadsPending(AspectSummary<T> leadsPending) {
        this.leadsPending = leadsPending;
    }

    public AspectSummary<T> getLeadsOngoing() {
        return leadsOngoing;
    }

    public void setLeadsOngoing(AspectSummary<T> leadsOngoing) {
        this.leadsOngoing = leadsOngoing;
    }

    public AspectSummary<T> getLeadConversion() {
        return leadConversion;
    }

    public void setLeadConversion(AspectSummary<T> leadConversion) {
        this.leadConversion = leadConversion;
    }
}
