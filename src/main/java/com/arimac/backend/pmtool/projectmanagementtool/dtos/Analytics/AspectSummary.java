package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.PerformanceEnum;

import java.math.BigDecimal;

public class AspectSummary<T> {
    private T value;
    private int days;
    private PerformanceEnum performance;
    private BigDecimal percentage;

    public AspectSummary() {
    }

    public AspectSummary(int days) {
        this.days = days;
        this.performance = PerformanceEnum.neutral;
        this.percentage = new BigDecimal("0.00");
    }

    public AspectSummary(T value, int days, PerformanceEnum performance, BigDecimal percentage) {
        this.value = value;
        this.days = days;
        this.performance = performance;
        this.percentage = percentage;
    }

    public AspectSummary(T value) {
        this.value = value;
        this.percentage = new BigDecimal("0.00");
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
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
