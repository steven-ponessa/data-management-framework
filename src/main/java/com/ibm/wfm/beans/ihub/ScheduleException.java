package com.ibm.wfm.beans.ihub;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScheduleException {
    private String exceptionId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime lastModifiedDate;

    private BigDecimal mondayHours;
    private BigDecimal tuesdayHours;
    private BigDecimal wednesdayHours;
    private BigDecimal thursdayHours;
    private BigDecimal fridayHours;
    private BigDecimal saturdayHours;
    private BigDecimal sundayHours;

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public BigDecimal getMondayHours() {
        return mondayHours;
    }

    public void setMondayHours(BigDecimal mondayHours) {
        this.mondayHours = mondayHours;
    }

    public BigDecimal getTuesdayHours() {
        return tuesdayHours;
    }

    public void setTuesdayHours(BigDecimal tuesdayHours) {
        this.tuesdayHours = tuesdayHours;
    }

    public BigDecimal getWednesdayHours() {
        return wednesdayHours;
    }

    public void setWednesdayHours(BigDecimal wednesdayHours) {
        this.wednesdayHours = wednesdayHours;
    }

    public BigDecimal getThursdayHours() {
        return thursdayHours;
    }

    public void setThursdayHours(BigDecimal thursdayHours) {
        this.thursdayHours = thursdayHours;
    }

    public BigDecimal getFridayHours() {
        return fridayHours;
    }

    public void setFridayHours(BigDecimal fridayHours) {
        this.fridayHours = fridayHours;
    }

    public BigDecimal getSaturdayHours() {
        return saturdayHours;
    }

    public void setSaturdayHours(BigDecimal saturdayHours) {
        this.saturdayHours = saturdayHours;
    }

    public BigDecimal getSundayHours() {
        return sundayHours;
    }

    public void setSundayHours(BigDecimal sundayHours) {
        this.sundayHours = sundayHours;
    }
}