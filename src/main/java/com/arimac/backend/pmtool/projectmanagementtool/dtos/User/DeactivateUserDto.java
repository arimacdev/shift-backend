package com.arimac.backend.pmtool.projectmanagementtool.dtos.User;

public class DeactivateUserDto {
    private String adminId;
    private String userId;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DeactivateUserDto{" +
                "adminId='" + adminId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
