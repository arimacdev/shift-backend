package com.arimac.backend.pmtool.projectmanagementtool.enumz;

public enum TaskGroupRoleEnum {
    owner("owner", 1),
    member("member", 2);

    private String roleKey;
    private int roleValue;

    TaskGroupRoleEnum(String roleKey, int roleValue) {
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