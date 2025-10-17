package company.structure;

import java.time.LocalTime;
import java.time.Duration;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class WorkSchedule {
    private final String scheduleId;
    private String scheduleName;
    private int workDaysPerWeek;
    private double hoursPerDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration breakDuration;
    private boolean isFlexible;
    private final Set<DayOfWeek> workingDays;

    public WorkSchedule(String scheduleId, String scheduleName, int workDaysPerWeek, 
                       double hoursPerDay, LocalTime startTime, LocalTime endTime, 
                       Duration breakDuration, boolean isFlexible) {
        validateScheduleInput(scheduleId, scheduleName, workDaysPerWeek, hoursPerDay, 
                            startTime, endTime, breakDuration);
        
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.workDaysPerWeek = workDaysPerWeek;
        this.hoursPerDay = hoursPerDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakDuration = breakDuration;
        this.isFlexible = isFlexible;
        this.workingDays = new HashSet<>();
        initializeDefaultWorkingDays();
    }

    // Public getters
    public String getScheduleId() { return scheduleId; }
    public String getScheduleName() { return scheduleName; }
    public int getWorkDaysPerWeek() { return workDaysPerWeek; }
    public double getHoursPerDay() { return hoursPerDay; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Duration getBreakDuration() { return breakDuration; }
    public boolean isFlexible() { return isFlexible; }
    public Set<DayOfWeek> getWorkingDays() { return Collections.unmodifiableSet(workingDays); }

    // Public methods for schedule management
    public void updateScheduleName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule name cannot be null or empty");
        }
        this.scheduleName = newName;
    }

    public void updateWorkingHours(LocalTime newStartTime, LocalTime newEndTime) {
        validateWorkingHours(newStartTime, newEndTime);
        this.startTime = newStartTime;
        this.endTime = newEndTime;
        updateHoursPerDay();
    }

    public void updateBreakDuration(Duration newBreakDuration) {
        if (newBreakDuration == null || newBreakDuration.isNegative()) {
            throw new IllegalArgumentException("Break duration cannot be null or negative");
        }
        this.breakDuration = newBreakDuration;
        updateHoursPerDay();
    }

    public void setFlexible(boolean flexible) {
        this.isFlexible = flexible;
    }

    public void addWorkingDay(DayOfWeek day) {
        if (workingDays.size() >= 7) {
            throw new IllegalStateException("Cannot add more than 7 working days");
        }
        workingDays.add(day);
        this.workDaysPerWeek = workingDays.size();
    }

    public void removeWorkingDay(DayOfWeek day) {
        if (workingDays.size() <= 1) {
            throw new IllegalStateException("Schedule must have at least one working day");
        }
        workingDays.remove(day);
        this.workDaysPerWeek = workingDays.size();
    }

    // Public utility methods
    public Duration getTotalWorkingTime() {
        return Duration.between(startTime, endTime).minus(breakDuration);
    }

    public Duration getWeeklyWorkingTime() {
        return getTotalWorkingTime().multipliedBy(workDaysPerWeek);
    }

    public boolean isWorkingDay(DayOfWeek day) {
        return workingDays.contains(day);
    }

    // Private helper methods
    private void initializeDefaultWorkingDays() {
        workingDays.add(DayOfWeek.MONDAY);
        workingDays.add(DayOfWeek.TUESDAY);
        workingDays.add(DayOfWeek.WEDNESDAY);
        workingDays.add(DayOfWeek.THURSDAY);
        workingDays.add(DayOfWeek.FRIDAY);
    }

    private void validateScheduleInput(String scheduleId, String scheduleName, 
                                     int workDaysPerWeek, double hoursPerDay,
                                     LocalTime startTime, LocalTime endTime, 
                                     Duration breakDuration) {
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID cannot be null or empty");
        }
        if (scheduleName == null || scheduleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule name cannot be null or empty");
        }
        if (workDaysPerWeek < 1 || workDaysPerWeek > 7) {
            throw new IllegalArgumentException("Work days per week must be between 1 and 7");
        }
        validateWorkingHours(startTime, endTime);
        if (breakDuration == null || breakDuration.isNegative()) {
            throw new IllegalArgumentException("Break duration cannot be null or negative");
        }
    }

    private void validateWorkingHours(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Working hours cannot be null");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    private void updateHoursPerDay() {
        Duration workingTime = getTotalWorkingTime();
        this.hoursPerDay = workingTime.toMinutes() / 60.0;
    }

    @Override
    public String toString() {
        return "WorkSchedule{" +
                "id='" + scheduleId + '\'' +
                ", name='" + scheduleName + '\'' +
                ", workDays=" + workDaysPerWeek +
                ", hoursPerDay=" + String.format("%.1f", hoursPerDay) +
                ", time=" + startTime + "-" + endTime +
                ", flexible=" + isFlexible +
                '}';
    }
}
