package com.ibm.wfm.beans.ihub;

import java.math.BigDecimal;

public class Schedule {
    private String scheduleId;
    private String scheduleName;
    private BigDecimal weekTotalHours;
    private BigDecimal scheduledDays;
    private BigDecimal scheduledHours;
    private Boolean blocking;

    private BigDecimal mondayHours;
    private BigDecimal tuesdayHours;
    private BigDecimal wednesdayHours;
    private BigDecimal thursdayHours;
    private BigDecimal fridayHours;
    private BigDecimal saturdayHours;
    private BigDecimal sundayHours;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public BigDecimal getWeekTotalHours() {
        return weekTotalHours;
    }

    public void setWeekTotalHours(BigDecimal weekTotalHours) {
        this.weekTotalHours = weekTotalHours;
    }

    public BigDecimal getScheduledDays() {
        return scheduledDays;
    }

    public void setScheduledDays(BigDecimal scheduledDays) {
        this.scheduledDays = scheduledDays;
    }

    public BigDecimal getScheduledHours() {
        return scheduledHours;
    }

    public void setScheduledHours(BigDecimal scheduledHours) {
        this.scheduledHours = scheduledHours;
    }

    public Boolean getBlocking() {
        return blocking;
    }

    public void setBlocking(Boolean blocking) {
        this.blocking = blocking;
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