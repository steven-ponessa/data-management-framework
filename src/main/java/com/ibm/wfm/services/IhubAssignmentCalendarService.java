package com.ibm.wfm.services;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.ibm.wfm.beans.ihub.Assignment;
import com.ibm.wfm.beans.ihub.AssignmentCalendarDay;
import com.ibm.wfm.beans.ihub.AssignmentScheduleBundle;
import com.ibm.wfm.beans.ihub.Schedule;
import com.ibm.wfm.beans.ihub.ScheduleException;

public class IhubAssignmentCalendarService {

    public List<AssignmentCalendarDay> generate13WeekCalendar(AssignmentScheduleBundle bundle) {
        List<AssignmentCalendarDay> result = new ArrayList<>();

        if (bundle == null || bundle.getAssignment() == null || bundle.getSchedule() == null) {
            return result;
        }

        Assignment assignment = bundle.getAssignment();
        Schedule schedule = bundle.getSchedule();
        List<ScheduleException> exceptions = new ArrayList<>(bundle.getScheduleExceptions());

        exceptions.sort(
            Comparator.comparing(ScheduleException::getLastModifiedDate,
                    Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(ScheduleException::getStartDate,
                    Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(ScheduleException::getExceptionId,
                    Comparator.nullsLast(Comparator.reverseOrder()))
        );

        LocalDate start = nextMonday(LocalDate.now());
        LocalDate end = start.plusWeeks(13).minusDays(1);

        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            if (assignment.getStartDate() != null && d.isBefore(assignment.getStartDate())) {
                continue;
            }
            if (assignment.getEndDate() != null && d.isAfter(assignment.getEndDate())) {
                continue;
            }

            Optional<ScheduleException> matchingException = findWinningException(exceptions, d);

            BigDecimal hours;
            String source;
            String exceptionId = null;

            if (matchingException.isPresent()) {
                ScheduleException ex = matchingException.get();
                hours = getHoursForDayOfWeek(ex, d.getDayOfWeek());
                source = "EXCEPTION";
                exceptionId = ex.getExceptionId();
            } else {
                hours = getHoursForDayOfWeek(schedule, d.getDayOfWeek());
                source = "TEMPLATE";
            }

            AssignmentCalendarDay row = new AssignmentCalendarDay();
            row.setAssignmentId(assignment.getAssignmentId());
            row.setAssignmentName(assignment.getAssignmentName());
            row.setScheduleId(schedule.getScheduleId());
            row.setScheduleName(schedule.getScheduleName());
            row.setDate(d);
            row.setEffectiveHours(hours != null ? hours : BigDecimal.ZERO);
            row.setHoursSource(source);
            row.setExceptionId(exceptionId);

            result.add(row);
        }

        return result;
    }

    private Optional<ScheduleException> findWinningException(List<ScheduleException> exceptions, LocalDate date) {
        return exceptions.stream()
            .filter(e -> appliesToDate(e, date))
            .findFirst(); // already sorted descending by "winner" precedence
    }

    private boolean appliesToDate(ScheduleException e, LocalDate date) {
        if (e.getStartDate() == null) {
            return false;
        }

        LocalDate endDate = e.getEndDate() != null ? e.getEndDate() : e.getStartDate();
        return !date.isBefore(e.getStartDate()) && !date.isAfter(endDate);
    }

    private LocalDate nextMonday(LocalDate from) {
        int daysUntilMonday = (DayOfWeek.MONDAY.getValue() - from.getDayOfWeek().getValue() + 7) % 7;
        if (daysUntilMonday == 0) {
            daysUntilMonday = 7;
        }
        return from.plusDays(daysUntilMonday);
    }

    private BigDecimal getHoursForDayOfWeek(Schedule schedule, DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> nullSafe(schedule.getMondayHours());
            case TUESDAY -> nullSafe(schedule.getTuesdayHours());
            case WEDNESDAY -> nullSafe(schedule.getWednesdayHours());
            case THURSDAY -> nullSafe(schedule.getThursdayHours());
            case FRIDAY -> nullSafe(schedule.getFridayHours());
            case SATURDAY -> nullSafe(schedule.getSaturdayHours());
            case SUNDAY -> nullSafe(schedule.getSundayHours());
        };
    }

    private BigDecimal getHoursForDayOfWeek(ScheduleException scheduleException, DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> nullSafe(scheduleException.getMondayHours());
            case TUESDAY -> nullSafe(scheduleException.getTuesdayHours());
            case WEDNESDAY -> nullSafe(scheduleException.getWednesdayHours());
            case THURSDAY -> nullSafe(scheduleException.getThursdayHours());
            case FRIDAY -> nullSafe(scheduleException.getFridayHours());
            case SATURDAY -> nullSafe(scheduleException.getSaturdayHours());
            case SUNDAY -> nullSafe(scheduleException.getSundayHours());
        };
    }

    private BigDecimal nullSafe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}