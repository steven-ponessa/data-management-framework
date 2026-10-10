package com.ibm.wfm.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.ibm.wfm.beans.ihub.Assignment;
import com.ibm.wfm.beans.ihub.AssignmentScheduleBundle;
import com.ibm.wfm.beans.ihub.Schedule;
import com.ibm.wfm.beans.ihub.ScheduleException;

public class AssignmentScheduleRepository {

    public AssignmentScheduleBundle loadAssignmentScheduleBundle(Connection conn, String assignmentId) throws SQLException {
        String sql = """
            SELECT
                  a.ID   AS ASSIGNMENT_ID,
                  a.NAME AS ASSIGNMENT_NAME,
                  a.PSE__START_DATE AS ASG_START_DT,
                  a.PSE__END_DATE   AS ASG_END_DT,
                  s.ID   AS SCHEDULE_ID,
                  s.NAME AS SCHEDULE_NAME,
                  s.PSE_WEEK_TOTAL_HOURS,
                  s.PSE_SCHEDULED_DAYS,
                  s.PSE_SCHEDULED_HOURS,
                  s.PSE_IS_BLOCKING,
                  s.PSE_MONDAY_HOURS    AS T_MO,
                  s.PSE_TUESDAY_HOURS   AS T_TU,
                  s.PSE_WEDNESDAY_HOURS AS T_WE,
                  s.PSE_THURSDAY_HOURS  AS T_TH,
                  s.PSE_FRIDAY_HOURS    AS T_FR,
                  s.PSE_SATURDAY_HOURS  AS T_SA,
                  s.PSE_SUNDAY_HOURS    AS T_SU,
                  e.ID                  AS EXCEPTION_ID,
                  e.PSE_DATE            AS EXC_START_DT,
                  e.PSE_END_DATE        AS EXC_END_DT,
                  e.LASTMODIFIEDDATE,
                  e.PSE_MONDAY_HOURS    AS X_MO,
                  e.PSE_TUESDAY_HOURS   AS X_TU,
                  e.PSE_WEDNESDAY_HOURS AS X_WE,
                  e.PSE_THURSDAY_HOURS  AS X_TH,
                  e.PSE_FRIDAY_HOURS    AS X_FR,
                  e.PSE_SATURDAY_HOURS  AS X_SA,
                  e.PSE_SUNDAY_HOURS    AS X_SU
            FROM ISC.PSE_ASSIGNMENT a
            JOIN ISC.PSE_SCHEDULE s
              ON s.ID = a.PSE__SCHEDULE
            LEFT JOIN ISC.PSE_SCHEDULE_EXCEPTION e
              ON e.PSE_SCHEDULE = s.ID
             AND COALESCE(e.ISDELETED, 0) = 0
            WHERE a.ID = ?
            ORDER BY a.ID,
                     s.ID,
                     e.PSE_DATE,
                     e.LASTMODIFIEDDATE,
                     e.ID
            WITH UR
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assignmentId);

            try (ResultSet rs = ps.executeQuery()) {
                AssignmentScheduleBundle bundle = null;
                List<String> seenExceptionIds = new ArrayList<>();

                while (rs.next()) {
                    if (bundle == null) {
                        bundle = new AssignmentScheduleBundle();

                        Assignment a = new Assignment();
                        a.setAssignmentId(rs.getString("ASSIGNMENT_ID"));
                        a.setAssignmentName(rs.getString("ASSIGNMENT_NAME"));

                        Date asgStart = rs.getDate("ASG_START_DT");
                        if (asgStart != null) {
                            a.setStartDate(asgStart.toLocalDate());
                        }

                        Date asgEnd = rs.getDate("ASG_END_DT");
                        if (asgEnd != null) {
                            a.setEndDate(asgEnd.toLocalDate());
                        }

                        Schedule s = new Schedule();
                        s.setScheduleId(rs.getString("SCHEDULE_ID"));
                        s.setScheduleName(rs.getString("SCHEDULE_NAME"));
                        s.setWeekTotalHours(rs.getBigDecimal("PSE_WEEK_TOTAL_HOURS"));
                        s.setScheduledDays(rs.getBigDecimal("PSE_SCHEDULED_DAYS"));
                        s.setScheduledHours(rs.getBigDecimal("PSE_SCHEDULED_HOURS"));
                        s.setBlocking(getNullableBoolean(rs, "PSE_IS_BLOCKING"));
                        s.setMondayHours(rs.getBigDecimal("T_MO"));
                        s.setTuesdayHours(rs.getBigDecimal("T_TU"));
                        s.setWednesdayHours(rs.getBigDecimal("T_WE"));
                        s.setThursdayHours(rs.getBigDecimal("T_TH"));
                        s.setFridayHours(rs.getBigDecimal("T_FR"));
                        s.setSaturdayHours(rs.getBigDecimal("T_SA"));
                        s.setSundayHours(rs.getBigDecimal("T_SU"));

                        bundle.setAssignment(a);
                        bundle.setSchedule(s);
                    }

                    String exceptionId = rs.getString("EXCEPTION_ID");
                    if (exceptionId != null && !seenExceptionIds.contains(exceptionId)) {
                        ScheduleException e = new ScheduleException();
                        e.setExceptionId(exceptionId);

                        Date excStart = rs.getDate("EXC_START_DT");
                        if (excStart != null) {
                            e.setStartDate(excStart.toLocalDate());
                        }

                        Date excEnd = rs.getDate("EXC_END_DT");
                        if (excEnd != null) {
                            e.setEndDate(excEnd.toLocalDate());
                        }

                        Timestamp lastMod = rs.getTimestamp("LASTMODIFIEDDATE");
                        if (lastMod != null) {
                            e.setLastModifiedDate(lastMod.toLocalDateTime());
                        }

                        e.setMondayHours(rs.getBigDecimal("X_MO"));
                        e.setTuesdayHours(rs.getBigDecimal("X_TU"));
                        e.setWednesdayHours(rs.getBigDecimal("X_WE"));
                        e.setThursdayHours(rs.getBigDecimal("X_TH"));
                        e.setFridayHours(rs.getBigDecimal("X_FR"));
                        e.setSaturdayHours(rs.getBigDecimal("X_SA"));
                        e.setSundayHours(rs.getBigDecimal("X_SU"));

                        bundle.getScheduleExceptions().add(e);
                        seenExceptionIds.add(exceptionId);
                    }
                }

                return bundle;
            }
        }
    }

    private Boolean getNullableBoolean(ResultSet rs, String columnName) throws SQLException {
        boolean value = rs.getBoolean(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }
}