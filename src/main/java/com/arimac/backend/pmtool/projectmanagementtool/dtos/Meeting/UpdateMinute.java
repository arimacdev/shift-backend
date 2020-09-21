package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import javax.validation.constraints.NotNull;

public class UpdateMinute {
    @NotNull
    private String description;
    private String remarks;
    @NotNull
    private String actionBy;
    @NotNull
    private boolean actionByGuest;
}
