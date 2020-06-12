package com.arimac.backend.pmtool.projectmanagementtool.dtos.Category;

import javax.validation.constraints.NotEmpty;

public class CategoryEditDto {
    private String categoryName;
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
