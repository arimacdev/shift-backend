package com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog;

import java.util.List;

public class ActivityLogCountResponse {
    private int ActivityLogCount;
    private List<ActivityLogResposeDto> activityLogList;

    public int getActivityLogCount() {
        return ActivityLogCount;
    }

    public void setActivityLogCount(int activityLogCount) {
        ActivityLogCount = activityLogCount;
    }

    public List<ActivityLogResposeDto> getActivityLogList() {
        return activityLogList;
    }

    public void setActivityLogList(List<ActivityLogResposeDto> activityLogList) {
        this.activityLogList = activityLogList;
    }
}
