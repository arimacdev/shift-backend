package com.arimac.backend.pmtool.projectmanagementtool.dtos.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class CategoryDto {
    @NotEmpty(message = "Provide a Category Name")
    private String categoryName;
    @NotEmpty
    private String categoryColorCode;


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColorCode() {
        return categoryColorCode;
    }

    public void setCategoryColorCode(String categoryColorCode) {
        this.categoryColorCode = categoryColorCode;
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryColorCode='" + categoryColorCode + '\'' +
                '}';
    }
}
