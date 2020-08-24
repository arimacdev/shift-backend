package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.PerformanceEnum;

import java.math.BigDecimal;

public class AspectSummary {
    private int value;
    private int days;
    private PerformanceEnum performance;
    private BigDecimal percentage;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public PerformanceEnum getPerformance() {
        return performance;
    }

    public void setPerformance(PerformanceEnum performance) {
        this.performance = performance;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
