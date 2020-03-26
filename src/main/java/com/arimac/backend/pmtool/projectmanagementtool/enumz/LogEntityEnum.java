package com.arimac.backend.pmtool.projectmanagementtool.enumz;

public enum  LogEntityEnum {
    Project("Project",1),
    Task("Project",2),
    SubTask("Project",3);


    private String entity;
    private int entityId;

    LogEntityEnum(String entity, int entityId){
        this.entity = entity;
        this.entityId = entityId;
    }

    public String getEntity() {
        return entity;
    }

    public int getEntityId() {
        return entityId;
    }

}
