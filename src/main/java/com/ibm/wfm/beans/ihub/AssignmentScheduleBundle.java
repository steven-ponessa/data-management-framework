package com.ibm.wfm.beans.ihub;

import java.util.ArrayList;
import java.util.List;

public class AssignmentScheduleBundle {
    private Assignment assignment;
    private Schedule schedule;
    private List<ScheduleException> scheduleExceptions = new ArrayList<>();

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<ScheduleException> getScheduleExceptions() {
        return scheduleExceptions;
    }

    public void setScheduleExceptions(List<ScheduleException> scheduleExceptions) {
        this.scheduleExceptions = scheduleExceptions;
    }
}