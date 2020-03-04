package com.arimac.backend.pmtool.projectmanagementtool.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UtilsService {

    public String getUUId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
