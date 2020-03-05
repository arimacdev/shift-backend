package com.arimac.backend.pmtool.projectmanagementtool.enumz;

public enum  ProjectRoleEnum {
    owner("owner", 1),
    admin("admin", 2),
    user("user", 3);

    private String roleKey;
    private int roleValue;

    ProjectRoleEnum(String roleKey, int roleValue) {
        this.roleKey = roleKey;
        this.roleValue = roleValue;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public int getRoleValue() {
        return roleValue;
    }

}
