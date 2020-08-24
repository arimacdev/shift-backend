package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics;

import java.math.BigDecimal;

public class ProjectOverViewDto {
    private int totalProjects;
    private int leadsPending;
    private int leadsOngoing;
    private BigDecimal leadConversion;

    public ProjectOverViewDto() {
        this.leadConversion = new BigDecimal("0.00");
    }

    public int getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }

    public int getLeadsPending() {
        return leadsPending;
    }

    public void setLeadsPending(int leadsPending) {
        this.leadsPending = leadsPending;
    }

    public int getLeadsOngoing() {
        return leadsOngoing;
    }

    public void setLeadsOngoing(int leadsOngoing) {
        this.leadsOngoing = leadsOngoing;
    }

    public BigDecimal getLeadConversion() {
        return leadConversion;
    }

    public void setLeadConversion(BigDecimal leadConversion) {
        this.leadConversion = leadConversion;
    }
}
