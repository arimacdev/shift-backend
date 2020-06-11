package com.arimac.backend.pmtool.projectmanagementtool.dtos.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class CategoryAddDto {
    @NotEmpty(message = "Provide a Category Name")
    private String categoryName;


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "CategoryAddDto{" +
                "categoryName='" + categoryName + '\'' +
                '}';
    }
}
