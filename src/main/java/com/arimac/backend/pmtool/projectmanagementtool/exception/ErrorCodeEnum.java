package com.arimac.backend.pmtool.projectmanagementtool.exception;

public enum ErrorCodeEnum {
    ERROR("0000"),
    PENDING_USER("0001"),
    PENDING_GROUP("0002"),
    REJECTED_USER("0003"),
    INSOLAR_ERROR("0004"),
    PROCESSING("0005"),
    INACTIVE_GROUP("0006");

    private String value;

    private ErrorCodeEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}