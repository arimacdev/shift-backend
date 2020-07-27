package com.arimac.backend.pmtool.projectmanagementtool.dtos.Project;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.WeightTypeEnum;

import javax.validation.constraints.NotNull;

public class ProjectWeightUpdateDto {
    @NotNull
    private WeightTypeEnum weightType;

    public WeightTypeEnum getWeightType() {
        return weightType;
    }

    public void setWeightType(WeightTypeEnum weightType) {
        this.weightType = weightType;
    }

    @Override
    public String toString() {
        return "ProjectWeightUpdateDto{" +
                "weightType=" + weightType +
                '}';
    }
}
