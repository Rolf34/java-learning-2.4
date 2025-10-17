package company.projects;

import company.empoloyees.Employee;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Objects;

public class TimeEntry {
    private final String timeEntryId;
    private final Employee employee;
    private final Project project;
    private final Task task;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private boolean isApproved;
    private final LocalDateTime createdAt;

    public TimeEntry(String timeEntryId, Employee employee, Project project, 
                    Task task, LocalDateTime startTime) {
        validateTimeEntryInput(timeEntryId, employee, project, task, startTime);
        
        this.timeEntryId = timeEntryId;
        this.employee = employee;
        this.project = project;
        this.task = task;
        this.startTime = startTime;
        this.isApproved = false;
        this.createdAt = LocalDateTime.now();
    }

    // Package-private methods
    void approve() {
        if (endTime == null) {
            throw new IllegalStateException("Cannot approve time entry without end time");
        }
        
        if (!isApproved) {
            this.isApproved = true;
            if (task != null) {
                task.logWork(getDuration().toHours());
            }
        }
    }

    void reject() {
        this.isApproved = false;
    }

    // Public methods for time entry information
    public String getTimeEntryId() { return timeEntryId; }
    public Employee getEmployee() { return employee; }
    public Project getProject() { return project; }
    public Task getTask() { return task; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getDescription() { return description; }
    public boolean isApproved() { return isApproved; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public Duration getDuration() {
        return endTime != null ? Duration.between(startTime, endTime) : Duration.ZERO;
    }

    public double getHours() {
        return getDuration().toMinutes() / 60.0;
    }

    // Public methods for updating time entry
    public void stopWork(LocalDateTime endTime) {
        validateEndTime(endTime);
        if (this.endTime == null) {
            this.endTime = endTime;
        } else {
            throw new IllegalStateException("Time entry has already been stopped");
        }
    }

    public void updateDescription(String description) {
        if (!isApproved) {
            this.description = description;
        } else {
            throw new IllegalStateException("Cannot update description of approved time entry");
        }
    }

    // Private validation methods
    private void validateTimeEntryInput(String timeEntryId, Employee employee, 
                                      Project project, Task task, LocalDateTime startTime) {
        if (timeEntryId == null || timeEntryId.trim().isEmpty()) {
            throw new IllegalArgumentException("Time entry ID cannot be null or empty");
        }
        Objects.requireNonNull(employee, "Employee cannot be null");
        Objects.requireNonNull(project, "Project cannot be null");
        Objects.requireNonNull(startTime, "Start time cannot be null");
        
        if (startTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start time cannot be in the future");
        }
    }

    private void validateEndTime(LocalDateTime endTime) {
        Objects.requireNonNull(endTime, "End time cannot be null");
        
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        if (endTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("End time cannot be in the future");
        }
    }

    @Override
    public String toString() {
        return "TimeEntry{" +
                "id='" + timeEntryId + '\'' +
                ", employee=" + employee.getFullName() +
                ", project=" + project.getName() +
                ", duration=" + String.format("%.2f hours", getHours()) +
                ", approved=" + isApproved +
                '}';
    }
}
