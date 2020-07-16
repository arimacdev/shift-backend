package com.arimac.backend.pmtool.projectmanagementtool.enumz;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum  WeightTypeEnum {
    time(1),
    story(2);

    private int weightId;

    private static final Map<Integer,WeightTypeEnum> WEIGHT_TYPE_ENUM_MAP;

    WeightTypeEnum (int weightId) {
        this.weightId = weightId;
    }

    public int getWeightId() {
        return this.weightId;
    }

    static {
        Map<Integer,WeightTypeEnum> map = new ConcurrentHashMap<Integer, WeightTypeEnum>();
        for (WeightTypeEnum instance : WeightTypeEnum.values()) {
            map.put(instance.getWeightId(),instance);
        }
        WEIGHT_TYPE_ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static WeightTypeEnum get (int weightId) {
        return WEIGHT_TYPE_ENUM_MAP.get(weightId);
    }
}
