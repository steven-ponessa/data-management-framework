package com.ibm.wfm.beans.ihub;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AssignmentCalendarDay {
    private String assignmentId;
    private String assignmentName;
    private String scheduleId;
    private String scheduleName;
    private LocalDate date;
    private BigDecimal effectiveHours;
    private String hoursSource;   // TEMPLATE or EXCEPTION
    private String exceptionId;

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getEffectiveHours() {
        return effectiveHours;
    }

    public void setEffectiveHours(BigDecimal effectiveHours) {
        this.effectiveHours = effectiveHours;
    }

    public String getHoursSource() {
        return hoursSource;
    }

    public void setHoursSource(String hoursSource) {
        this.hoursSource = hoursSource;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }
}